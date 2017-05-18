package jp.co.biglobe.isp.sample.pi.service;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import jp.co.biglobe.isp.sample.akka.SpringFrameworkExtention;
import jp.co.biglobe.isp.sample.pi.actor.Master;
import jp.co.biglobe.isp.sample.pi.event.Calculate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PiService {

    // Create an Akka system
    private final ActorSystem system;

    private final SpringFrameworkExtention springFrameworkExtention;


    @Autowired
    public PiService(ActorSystem system, SpringFrameworkExtention springFrameworkExtention) {
        this.system = system;
        this.springFrameworkExtention = springFrameworkExtention;
    }

    // actors and messages ...
    public void calculate() {

        // Master アクター の生成。アクターの引数付きコンストラクタを呼び出したい場合、Props.createメソッドで指定する
        ActorRef master = system.actorOf(springFrameworkExtention.props(Master.class), "master");

        // Akkaシステムの外側からAkkaのActorを呼び出すサンプル
        Inbox inbox = Inbox.create(system);
        // Master アクター に Calculateメッセージを送信
        inbox.send(master, new Calculate(10000, 10000));
    }
}
