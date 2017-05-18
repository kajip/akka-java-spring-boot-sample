package jp.co.biglobe.isp.sample.config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfiguration {

    @Value("sample-akka")
    String  actorSystemName;

    @Bean
    public ActorSystem actorSystem(Config config) {
        return ActorSystem.create(actorSystemName, config);
    }

    @Bean
    public Config akkaConfig() {
        return ConfigFactory.load();
    }
}
