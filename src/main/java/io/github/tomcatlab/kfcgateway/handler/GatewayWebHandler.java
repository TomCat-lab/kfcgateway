package io.github.tomcatlab.kfcgateway.handler;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import io.github.tomcatlab.kfcgateway.DefaultGatewayPluginChain;
import io.github.tomcatlab.kfcgateway.GatewayPlugin;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component("gatewayWebHandler")
public class GatewayWebHandler  implements WebHandler {
    @Autowired
    List<GatewayPlugin> gatewayPlugins;
    @Override
    public reactor.core.publisher.Mono<Void> handle(ServerWebExchange exchange) {
        System.out.println(" ====> Kfc gateway web handler ...");
        String mock ="""
                    {"result":"no plugin"}
                    """;
        if (CollectionUtils.isEmpty(gatewayPlugins)){
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
        }

       return new DefaultGatewayPluginChain(gatewayPlugins).handle(exchange);

//        for (GatewayPlugin gatewayPlugin : gatewayPlugins) {
//            if (gatewayPlugin.support(exchange)){
//                return gatewayPlugin.handle(exchange);
//            }
//        }

//        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
    }
}
