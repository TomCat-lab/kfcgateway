package io.github.tomcatlab.kfcgateway;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class GatewayHandler {
  @Autowired
  RegistryCenter registryCenter;
  Mono<ServerResponse> handle(ServerRequest request){
    // 1. 通过请求路径或者服务名
    String service = request.path().substring(4);
    ServiceMeta serviceMeta = ServiceMeta.builder().name(service)
            .app("app1").env("dev").namespace("public").build();
    // 2. 通过rc拿到所有活着的服务实例
    List<InstanceMeta> instanceMetas = registryCenter.fetchAll(serviceMeta);
    // 3. 先简化处理，或者第一个实例url
    String url = instanceMetas.get(0).toUrl();
    Mono<String> requestJson = request.bodyToMono(String.class);
    Mono<String> entity = WebClient.create(url).post().contentType(MediaType.asMediaType(MediaType.APPLICATION_JSON)).body(requestJson,String.class).retrieve().bodyToMono(String.class);
    return ServerResponse.ok().contentType(MediaType.asMediaType(MediaType.APPLICATION_JSON)).body(entity, String.class);
//    return ServerResponse.ok().body(Mono.just("Hello Gateway!"), String.class);
  }
}
