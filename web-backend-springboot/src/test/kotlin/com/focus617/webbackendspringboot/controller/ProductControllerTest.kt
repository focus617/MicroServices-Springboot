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
import org.springframework.test.web.servlet.*

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
        fun `should return sorted products with default element number per page`() {
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
        fun `should return paged products with requested page`() {
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
    @DisplayName("GET /api/products/{id}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetProduct {

        @Test
        fun `should return the Product with the given id`() {
            // Given
            val id = 1

            // When/then
            mockMvc.get("$baseUrl/$id")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id") { value(id) }
                }
        }

        @Test
        fun `should return NOT FOUND if the given product id does not exist`() {
            // Given
            val invalidId = 9999

            // When/then
            mockMvc.get("$baseUrl/$invalidId")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }

    }

    @Nested
    @DisplayName("POST /api/products")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PostNewProduct {
        @Test
        fun `should add the new Product`() {
            // Given
            val newProduct =
                Product(
                    51,
                    "Code#51",
                    "Title NewProduct",
                    "Description NewProduct",
                    "https://focus617.com/200/200?188",
                    99.99
                )

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
                    jsonPath("$.code") { value(newProduct.code) }
                    jsonPath("$.description") { value(newProduct.description) }
                    jsonPath("$.image") { value(newProduct.image) }
                    jsonPath("$.price") { value(newProduct.price) }
                }

            //TODO: how to retrieve newProduct.id from above performPost
            mockMvc.get("$baseUrl/${newProduct.id}")
                .andExpect { content { json(objectMapper.writeValueAsString(newProduct)) } }
        }

        @Test
        fun `should return BAD REQUEST if Product with given product id already exist`() {
            // Given
            val invalidProduct = Product(1, "Code#111", "Title #1", "Description #1")

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

        @Test
        fun `should return ILLEGAL STATE if Product with given product CODE already exist`() {
            // Given
            val productWithDuplicatedCode = Product(111, "Code#1", "Title #1", "Description #1")

            // When
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(productWithDuplicatedCode)
            }

            // Then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    @DisplayName("PATCH /api/products")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PatchExistingProduct {

        @Test
        fun `should update an existing product`() {
            // Given
            val updatedProduct =
                Product(
                    2,
                    "Code#62",
                    "Title NewProduct",
                    "Description NewProduct",
                    "https://focus617.com/200/200?188",
                    99.99
                )

            // When
            val performPatchRequest = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedProduct)
            }

            // Then
            performPatchRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updatedProduct))
                    }
                }

            mockMvc.get("$baseUrl/${updatedProduct.id}")
                .andExpect { content { json(objectMapper.writeValueAsString(updatedProduct)) } }
        }

        @Test
        fun `should return NOT FOUND if no product with given id exists`() {
            // Given
            val invalidId = 9999
            val invalidProduct =
                Product(
                    invalidId,
                    "Code#Invalid",
                    "Title NewProduct",
                    "Description NewProduct",
                    "https://focus617.com/200/200?188",
                    99.99
                )


            // When
            val performPost = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidProduct)
            }

            // Then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }

    }

    @Nested
    @DisplayName("PUT /api/products/{id}?xxx=yyy")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PutExistingProduct {
        @Test
        fun `should return NOT FOUND if no product with given id exists`() {
            // Given
            val invalidId = 9999
            val invalidProduct =
                Product(
                    invalidId,
                    "Code#Invalid",
                    "Title NewProduct",
                    "Description NewProduct",
                    "https://focus617.com/200/200?188",
                    99.99
                )


            // When
            val performPost = mockMvc.put("$baseUrl/$invalidId?title=${invalidProduct.title}") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidProduct)
            }

            // Then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }

        @Test
        fun `should update an existing product with new title`() {
            val id = 1
            val newTitle = "new_title"

            // When/then
            mockMvc.put("$baseUrl/$id?title=$newTitle")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id") { value(id) }
                    jsonPath("$.title") { value(newTitle) }
                }
        }

        @Test
        fun `should update an existing product with new description`() {
            val id = 1
            val newDescription = "newDescription"

            // When/then
            mockMvc.put("$baseUrl/$id?description=$newDescription")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id") { value(id) }
                    jsonPath("$.description") { value(newDescription) }
                }
        }


        @Test
        fun `should update an existing product with maximum attributes`() {
            val updatingProduct =
                Product(
                    1,
                    "Code#1",
                    "Title NewTitle",
                    "Description NewDescription",
                    "https://focus617.com/200/200?188",
                    99.99
                )

            // When/then
            mockMvc.put(
                "$baseUrl/${updatingProduct.id}?title=${updatingProduct.title}&description=${updatingProduct.description}&image=${updatingProduct.image}&price=${updatingProduct.price}"
            )
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content { json(objectMapper.writeValueAsString(updatingProduct)) }
                }

            mockMvc.get("$baseUrl/${updatingProduct.id}")
                .andExpect { content { json(objectMapper.writeValueAsString(updatingProduct)) } }

        }

        @Test
        fun `should update product when price is NOT set to 0`() {
            val id = 1
            val newPrice = 9.99999

            // When/then
            mockMvc.put("$baseUrl/$id?price=$newPrice")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id") { value(id) }
                    jsonPath("$.price") { value(newPrice) }
                }
        }

        @Test
        fun `should NOT update product when price is set to 0`() {
            val id = 1

            // When/then
            mockMvc.put("$baseUrl/$id?price=0")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id") { value(id) }
                    jsonPath("$.price") { isNotEmpty() }
                }
        }

    }

    @Nested
    @DisplayName("DELETE /api/products/{id}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class DeleteExistingProduct {

        @Test
        fun `should delete the product with the given product id`() {
            // Given
            val productId = "3"

            // When/then
            mockMvc.delete("$baseUrl/$productId")
                .andDo { print() }
                .andExpect { status { isNoContent() } }

            mockMvc.get("$baseUrl/$productId")
                .andExpect { status { isNotFound() } }

        }

        @Test
        fun `should return NOT FOUND if no product with given id exists`() {
            // Given
            val invalidProductId = 9999

            // When/then
            mockMvc.delete("$baseUrl/$invalidProductId")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

}