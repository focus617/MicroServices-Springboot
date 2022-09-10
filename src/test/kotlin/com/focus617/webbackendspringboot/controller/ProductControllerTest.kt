package com.focus617.webbackendspringboot.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.focus617.webbackendspringboot.domain.model.Product
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class ProductControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    val baseUrl = "/api/products"

    @Nested
    @DisplayName("GET /api/products")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetProducts {
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

        @Test
        fun `should return sorted products with default product number per page`() {
            // When/then
            mockMvc.get("$baseUrl/backend?sort=desc")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.data.size()") { value(10) }
                    jsonPath("$.page") { value(1) }
//                    jsonPath("$.last_page") { value(("$.total".toInt())/10 + 1) }
                }
        }

        @Test
        fun `should return paged products with requestd page`() {
            // When/then
            mockMvc.get("$baseUrl/backend?sort=asc&page=2")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.page") { value(2) }
                }
        }
    }

    @Nested
    @DisplayName("GET /api/Product/{id}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetProduct {

        @Test
        fun `should return the Product with the given id`() {
            // Given
            val id = 101

            // When/then
            mockMvc.get("$baseUrl/$id")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id") { value(101) }
                }
        }

        @Test
        fun `should return NOT FOUND if the given product id does not exist`() {
            // Given
            val invalidId = 999999999

            // When/then
            mockMvc.get("$baseUrl/$invalidId")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }

    }

    @Nested
    @DisplayName("POST /api/Products")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PostNewProduct {
        @Test
        fun `should add the new Product`() {
            // Given
            val newProduct =
                Product(0, "Title NewProduct", "Description NewProduct", "http://focus617.com/200/200?188", 99.99)

            // When
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newProduct)
            }

            // Then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.title") { value("Title NewProduct") }
                    jsonPath("$.description") { value(newProduct.description) }
                    jsonPath("$.image") { value(newProduct.image) }
                    jsonPath("$.price") { value(newProduct.price) }
                }

//            mockMvc.get("$baseUrl/${newProduct.id}")
//                .andExpect { content { json(objectMapper.writeValueAsString(newProduct)) } }

        }

        @Test
        fun `should return BAD REQUEST if Product with given product id already exist`() {
            // Given
            val invalidProduct = Product(101, "Title_Invalid", "Description_Invalid")

            // When
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidProduct)
            }

            // Then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }

        }
    }

}