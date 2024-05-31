package io.github.tomcatlab.kfcgateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Class: GatewayPlugin
 * Author: cola
 * Date: 2024/5/29
 * Description:
 */

public interface GatewayPlugin {
    String GATEWAY_PREFIX = "/gw";
    void start();
    void  stop();
    String getName();
    boolean support(ServerWebExchange exchange);
    Mono<Void> handle(ServerWebExchange exchange ,GatewayPluginChain pluginChain);
}
