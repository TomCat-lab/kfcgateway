package io.github.tomcatlab.kfcgateway.plugin;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import io.github.tomcatlab.kfcgateway.AbstractGatewayPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
/**
 * Class: KfcRpcPlugin
 * Author: cola
 * Date: 2024/5/31
 * Description: KfcRpcPlugin
 */

@Component("kfcrpc")
public class KfcRpcPlugin extends AbstractGatewayPlugin {
    private String prefix = GATEWAY_PREFIX + "/"+NAME+"/";
    @Autowired
    RegistryCenter registryCenter;
    public static final String NAME = "kfcrpc";

    @Override
    public boolean doSupport(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(prefix);
    }

    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange) {
        System.out.println("=======>>>>>>> [KFCRpcPlugin] ...");
        // 1. 通过请求路径或者服务名
        String service = exchange.getRequest().getPath().value().substring(prefix.length());
        ServiceMeta serviceMeta = ServiceMeta.builder().name(service)
                .app("app1").env("dev").namespace("public").build();
        // 2. 通过rc拿到所有活着的服务实例
        List<InstanceMeta> instanceMetas = registryCenter.fetchAll(serviceMeta);
        // 3. 先简化处理，或者第一个实例url
        String url = instanceMetas.get(0).toUrl();
        Flux<DataBuffer> body = exchange.getRequest().getBody();
        Mono<String> entity = WebClient.create(url).post().contentType(MediaType.asMediaType(MediaType.APPLICATION_JSON)).body(body,DataBuffer.class).retrieve().bodyToMono(String.class);
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return entity.flatMap(x->exchange.getResponse().writeWith(Mono.just( exchange.getResponse().bufferFactory().wrap(x.getBytes()))));
    }

    @Override
    public String getName() {
        return NAME;
    }
}
