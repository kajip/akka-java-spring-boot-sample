package jp.co.biglobe.isp.sample.akka;

import akka.actor.Actor;
import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * SpringFrameworkのApplicationContext経由でActorを生成するプロデューサー。
 *
 * ここ（https://www.linkedin.com/pulse/spring-boot-akka-part-1-aliaksandr-liakh）みて作ったけど、
 * Extension を implementsする必要があるのだろうか。。。
 */
@Component
public class SpringFrameworkExtention implements Extension {

    private final ApplicationContext  applicationContext;

    @Autowired
    public SpringFrameworkExtention(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Props props(Class<? extends Actor> actorBeanType) {
        return Props.create(SpringActorProducer.class, applicationContext, actorBeanType);
    }
}
