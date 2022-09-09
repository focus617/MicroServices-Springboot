package com.focus617.webbackendspringboot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootApplication
class WebBackendSpringbootApplication(
	@Autowired private val jdbcTemplate: JdbcTemplate
)

fun main(args: Array<String>) {
	runApplication<WebBackendSpringbootApplication>(*args)
}
