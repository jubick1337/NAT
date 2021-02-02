package com.nat.nat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.view.MustacheViewResolver
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ResourceLoader
import org.springframework.web.servlet.ViewResolver

@SpringBootApplication
class NatApplication

fun main(args: Array<String>) {
    runApplication<NatApplication>(*args)
}
