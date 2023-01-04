package dev.akif.cookbook

import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class CookBookApplication

fun main(args: Array<String>) {
    runApplication<CookBookApplication>(*args)
}
