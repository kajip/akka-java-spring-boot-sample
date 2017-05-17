package jp.co.biglobe.isp.sample.pi.actor;

import akka.actor.*;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import javaslang.collection.List;
import jp.co.biglobe.isp.sample.pi.event.Calculate;
import jp.co.biglobe.isp.sample.pi.event.PiApproximation;
import jp.co.biglobe.isp.sample.pi.event.Result;
import jp.co.biglobe.isp.sample.pi.event.Work;

import java.time.Duration;
import java.time.LocalDateTime;


/**
 * 親子関係を持つActorのサンプル
 */
public class Master extends AbstractActor {

    private final Router  workerRouter;

    private final ActorRef listener;

    private final int nrOfMessages;
    private final int nrOfElements;

    private final LocalDateTime beginTimestamp = LocalDateTime.now();

    private double pi;

    private int nrOfResults;


    public Master(final int nrOfWorkers, int nrOfMessages, int nrOfElements, ActorRef listener) {
        this.nrOfMessages = nrOfMessages;
        this.nrOfElements = nrOfElements;
        this.listener = listener;

        // 子アクターの生成
        List<ActorRef> actorRefs = List.fill(nrOfWorkers,
            () -> getContext().actorOf(Props.create(Worker.class)));

        // 子アクターの監視を開始
        actorRefs.forEach(actorRef -> getContext().watch(actorRef));

        // 子アクターにJobを割り振るルーターの生成と子アクターの追加
        List<Routee> routees = actorRefs.map(ActorRefRoutee::new);

        this.workerRouter = new Router(new RoundRobinRoutingLogic(), routees);
    }

    /**
     * Actor が受け取ったメッセージをどうするか定義するメソッド。
     * Actorを呼び出すときに使った ActorRef#tellメソッドの引数で渡されたメッセージのクラスの型が match したメソッドが実行される。
     * @return
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()

            // 計算開始メッセージの受信
            .match(Calculate.class, this::startCalculation)

            // Workerから結果受信
            .match(Result.class, this::receiveResult)

            // 想定外のメッセージを受信したとき
            .matchAny(this::unhandled)

            .build();
    }

    // 計算開始処理
    private void startCalculation(Calculate calculate) {
        List.range(0, nrOfMessages)
            .forEach(start -> workerRouter.route(new Work(start, nrOfElements), getSelf()));
    }

    // Workerの計算結果取得処理
    private void receiveResult(Result result) {
        pi += result.getValue();
        nrOfResults += 1;

        if (nrOfResults < nrOfMessages) {
            return;
        }

        // Send the result to the listener
        Duration duration = Duration.between(beginTimestamp, LocalDateTime.now());
        listener.tell(new PiApproximation(pi, duration), getSelf());

        // Stops this actor and all its supervised children
        getContext().stop(getSelf());
    }


    // Workerの Actor定義
    public static class Worker extends AbstractActor {

        @Override
        public Receive createReceive() {
            return receiveBuilder()

                // Work型メッセージを受け取ったとき
                .match(Work.class, this::calculatePiFor)

                // 想定外のメッセージを受信したとき
                .matchAny(this::unhandled)

                .build();
        }

        private void calculatePiFor(Work work) {
            int start = work.getStart();
            int nrOfElements = work.getNrOfElements();

            double acc = 0.0;
            for (int i = start * nrOfElements; i <= ((start + 1) * nrOfElements - 1); i++) {
                acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
            }

            getSender().tell(new Result(acc), getSelf());
        }
    }
}
