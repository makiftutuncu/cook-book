package dev.akif.cookbook

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@EnableR2dbcRepositories
@SpringBootApplication
class CookBookApplication

fun main(args: Array<String>) {
    SpringApplication.run(CookBookApplication::class.java, *args)
}
