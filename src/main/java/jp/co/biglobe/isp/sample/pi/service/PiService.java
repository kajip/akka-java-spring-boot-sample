package jp.co.biglobe.isp.sample.pi.service;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Inbox;
import jp.co.biglobe.isp.sample.pi.actor.Listener;
import jp.co.biglobe.isp.sample.pi.actor.Master;
import jp.co.biglobe.isp.sample.pi.event.Calculate;


public class PiService {

    // Create an Akka system
    ActorSystem system = ActorSystem.create("PiSystem");

    // actors and messages ...
    public void calculate(final int nrOfWorkers, final int nrOfElements, final int nrOfMessages) {

        // Listener アクター の生成
        final ActorRef listener = system.actorOf(Props.create(Listener.class), "listener");

        // Master アクター の生成。アクターの引数付きコンストラクタを呼び出したい場合、Props.createメソッドで指定する
        ActorRef master = system.actorOf(
            Props.create(Master.class, nrOfWorkers, nrOfMessages, nrOfElements, listener), "master");

        // Akkaシステムの外側からAkkaのActorを呼び出すサンプル
        Inbox inbox = Inbox.create(system);
        // Master アクター に Calculateメッセージを送信
        inbox.send(master, new Calculate());
    }
}
