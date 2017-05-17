package jp.co.biglobe.isp.sample.pi.actor;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import jp.co.biglobe.isp.sample.pi.event.PiApproximation;

public class Listener extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()

            // Work型メッセージを受け取ったとき
            .match(PiApproximation.class, this::printResult)

            // 想定外のメッセージを受信したとき
            .matchAny(this::unhandled)

            .build();
    }

    private void printResult(PiApproximation result) {
        logger.info("Pi approximation: {}", result.getPi());
        logger.info("time: {}", result.getDuration().toMillis());

        // 計算が終わったので、Akkaシステムをシャットダウン
        getContext().system().terminate();
    }
}
