package jp.co.biglobe.isp.sample.pi.actor;

import akka.actor.*;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import javaslang.collection.List;
import jp.co.biglobe.isp.sample.akka.SpringFrameworkExtention;
import jp.co.biglobe.isp.sample.pi.event.Calculate;
import jp.co.biglobe.isp.sample.pi.event.PiApproximation;
import jp.co.biglobe.isp.sample.pi.event.Result;
import jp.co.biglobe.isp.sample.pi.event.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 親子関係を持つActorのサンプル
 */
@Component
@Scope("prototype")
public class Master extends AbstractActor {

    private final SpringFrameworkExtention  springFrameworkExtention;

    private Router  workerRouter;


    @Value("4")
    private int nrOfWorkers;

    private int nrOfMessages;

    private final LocalDateTime beginTimestamp = LocalDateTime.now();

    private double pi;

    private int nrOfResults;


    @Autowired
    public Master(SpringFrameworkExtention springFrameworkExtention) {
       this.springFrameworkExtention = springFrameworkExtention;
    }

    @PostConstruct
    public void postConstruct() {
        // 子アクターの生成
        List<ActorRef> actorRefs = List.fill(nrOfWorkers,
            () -> getContext().actorOf(springFrameworkExtention.props(Worker.class)));

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

        this.nrOfMessages = calculate.getNrOfMessages();

        List.range(0, nrOfMessages)
            .forEach(start -> workerRouter.route(new Work(start, calculate.getNrOfElements()), getSelf()));
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
        ActorRef listener = getContext().actorOf(springFrameworkExtention.props(Listener.class));

        listener.tell(new PiApproximation(pi, duration), getSelf());

        // Stops this actor and all its supervised children
        getContext().stop(getSelf());
    }
}
