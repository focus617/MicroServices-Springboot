package com.focus617.webbackendspringboot.domain.interactors

import com.focus617.webbackendspringboot.domain.repository.ProductRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestTemplate

internal class ProductServiceTest {

    private val productRepository: ProductRepository = mockk(relaxed = true)
    private val productService = ProductService(productRepository, RestTemplate())

    @Test
    fun `should call repository findAll() to retrieve products`() {
        // Given
        every { productRepository.findAll() } returns emptyList()   // if pay attention to return value

        // When
        productService.getProducts()

        // Then
        verify(exactly = 1) { productRepository.findAll() }
    }
}