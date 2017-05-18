package jp.co.biglobe.isp.sample.akka;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

/**
 * SpringFrameworkのApplicationContext経由でActorを生成するプロデューサー
 */
@RequiredArgsConstructor
public class SpringActorProducer implements IndirectActorProducer {

    private final ApplicationContext  context;

    private final Class<? extends Actor>  actorBeanType;

    @Override
    public Actor produce() {
        return context.getBean(actorBeanType);
    }

    @Override
    public Class<? extends Actor> actorClass() {
        return actorBeanType;
    }
}
