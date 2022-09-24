package com.focus617.webbackendspringboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
class WebBackendSpringbootApplication

fun main(args: Array<String>) {
    runApplication<WebBackendSpringbootApplication>(*args)
}
