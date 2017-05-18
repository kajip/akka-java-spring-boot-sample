package jp.co.biglobe.isp.sample.pi.actor;

import akka.actor.AbstractActor;
import jp.co.biglobe.isp.sample.pi.event.Result;
import jp.co.biglobe.isp.sample.pi.event.Work;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

// Workerの Actor定義
@Component
@Scope("prototype")
public class Worker extends AbstractActor {

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
