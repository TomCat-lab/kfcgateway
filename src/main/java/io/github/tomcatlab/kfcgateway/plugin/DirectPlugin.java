package io.github.tomcatlab.kfcgateway.plugin;

import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import io.github.tomcatlab.kfcgateway.AbstractGatewayPlugin;
import io.github.tomcatlab.kfcgateway.GatewayPluginChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.Name;
import java.util.List;

/**
 * Class: DirectPlugin
 * Author: cola
 * Date: 2024/5/29
 * Description: direct proxy plugin
 */
@Component("direct")
public class DirectPlugin extends AbstractGatewayPlugin {
    public static final String NAME = "direct";
    private String prefix = GATEWAY_PREFIX + "/"+NAME+"/";
    @Override
    public boolean doSupport(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(prefix);
    }

    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange , GatewayPluginChain pluginChain) {
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getResponse().getHeaders().set("kfc.gw.version","v1.0.0");
        String backend = exchange.getRequest().getQueryParams().getFirst("backend");
        Flux<DataBuffer> body = exchange.getRequest().getBody();
        if (backend == null || backend.isEmpty()){
            return exchange.getResponse().writeWith(Mono.from(body)).then();
        }
        Mono<String> entity = WebClient.create(backend).post().contentType(MediaType.asMediaType(MediaType.APPLICATION_JSON)).body(body,DataBuffer.class).retrieve().bodyToMono(String.class);

        return entity.flatMap(x->exchange.getResponse().writeWith(Mono.just( exchange.getResponse().bufferFactory().wrap(x.getBytes()))))
                .then(pluginChain.handle(exchange));
    }

    @Override
    public String getName() {
        return NAME;
    }
}
