package io.github.tomcatlab.kfcgateway;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;

public class GatewayWebHandler  implements WebHandler {
    @Override
    public reactor.core.publisher.Mono<Void> handle(ServerWebExchange exchange) {
//        exchange.getResponse().writeWith(Mono.just("Hello World"))
        return null;
    }
}
