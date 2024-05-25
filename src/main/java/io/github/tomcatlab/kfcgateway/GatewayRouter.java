package io.github.tomcatlab.kfcgateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class GatewayRouter {
    @Autowired
    HelloHandler helloHandler;

    @Autowired
    GatewayHandler gatewayHandler;
//    @Bean
//    public RouterFunction<?> userRouter(){
//        return route(GET("/user/{id}"), request -> ServerResponse.ok().body(Mono.just("hello gateway"),String.class));
//    }

    @Bean
    public RouterFunction<?> hello(){
        return route(GET("/hello"),helloHandler::handle);
    }

    @Bean
    public RouterFunction<?> gwRouter(){
        return route(GET("/gw").or(POST("/gw/**")),gatewayHandler::handle);
    }
}
