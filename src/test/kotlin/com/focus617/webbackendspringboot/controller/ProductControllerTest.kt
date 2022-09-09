package com.focus617.webbackendspringboot.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
internal class ProductControllerTest@Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    
    val baseUrl = "/api/products"

    @Nested
    @DisplayName("GET /api/products")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetProducts{
        @Test
        fun `should return all products`() {
            // When/then
            mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].title") { value("Title #1") }
                }
        }
    }
    
}