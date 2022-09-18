package com.focus617.webbackendspringboot.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
@ConditionalOnProperty(value = arrayOf("springfox.documentation.enabled"), havingValue = "true", matchIfMissing = true)
class Swagger3Config {

    // 对文档摘要信息的描述
    private fun apiInfo(): ApiInfo = ApiInfoBuilder()
        .title("API document for project web-backend-springboot")
        .description("RESTful API definition by Swagger3.0")
        .termsOfServiceUrl("http://focus617.com")
        .version("1.0")
        .build()

    // 配置Swagger的Docket的Bean实例
    @Bean
    fun createRestApi(): Docket = Docket(DocumentationType.OAS_30)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.focus617"))
        // 扫描BasePackage包下的“/api/”路径下的内容，作为构建接口文档的目标
        .paths(PathSelectors.regex("/api/.*"))
        .build()
}