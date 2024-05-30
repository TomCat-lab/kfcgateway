package io.github.tomcatlab.kfcgateway.config;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.registry.kk.KkRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

import static io.github.tomcatlab.kfcgateway.GatewayPlugin.GATEWAY_PREFIX;

@Configuration
public class GatewayConfig {
    @Bean
    public RegistryCenter registryCenter(){
        return new KkRegistryCenter();
    }

    @Bean
    ApplicationRunner runner(@Autowired ApplicationContext applicationContext){
        return args -> {
            SimpleUrlHandlerMapping handlerMapping = applicationContext.getBean(SimpleUrlHandlerMapping.class);
            Properties mappings = new Properties();
            mappings.put(GATEWAY_PREFIX+"/**","gatewayWebHandler");
            handlerMapping.setMappings(mappings);
            handlerMapping.initApplicationContext();
            System.out.println("kkrpc gateway start");
        };
    }
}
