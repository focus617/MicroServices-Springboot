package com.focus617.webbackendspringboot.domain.interactors.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class CustomerConfig {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate = builder.build()

}