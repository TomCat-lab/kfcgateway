package io.github.tomcatlab.kfcgateway;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class HelloHandler {
  Mono<ServerResponse> handle(ServerRequest request){
    String url = "http://localhost:9052/kfcrpc";
    String requestJson = """
            {
              "service": "com.cola.kfcrpc.demo.api.UserService",
              "methodSign": "findById@2_int_java.lang.String",
              "args": [21,"cola"]
            }
            """;
    Mono<String> entity = WebClient.create(url).post().contentType(MediaType.asMediaType(MediaType.APPLICATION_JSON)).bodyValue(requestJson).retrieve().bodyToMono(String.class);
    return ServerResponse.ok().contentType(MediaType.asMediaType(MediaType.APPLICATION_JSON)).body(entity, String.class);
  }
}
