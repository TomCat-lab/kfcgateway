package io.github.tomcatlab.kfcgateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Slf4j
public abstract class AbstractGatewayPlugin implements GatewayPlugin {

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange ,GatewayPluginChain pluginChain) {
        boolean isSupport = support(exchange);
        log.info("this plugin :{} is support:{} ",getName(),isSupport);
        return isSupport ? doHandle(exchange,pluginChain) : pluginChain.handle(exchange);
    }

    @Override
    public boolean support(ServerWebExchange exchange) {
        return doSupport(exchange);
    }
    public abstract boolean doSupport(ServerWebExchange exchange);
    public abstract Mono<Void> doHandle(ServerWebExchange exchange ,GatewayPluginChain pluginChain) ;

}
