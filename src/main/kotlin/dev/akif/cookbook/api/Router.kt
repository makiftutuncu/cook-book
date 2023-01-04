package dev.akif.cookbook.api

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration(proxyBeanMethods = false)
class Router {
    @Bean
    fun route(handler: Handler): RouterFunction<ServerResponse> =
        coRouter {
            GET("/recipes", handler::getAll)
            POST("/recipes", handler::create)
            PUT("/recipes/{id}", handler::update)
            DELETE("/recipes/{id}", handler::delete)
        }
}
