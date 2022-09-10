package com.focus617.webbackendspringboot.data.datasource.mock

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MockDataSourceTest {

    private val mockDataSource = MockProductDataSource()

    @Test
    fun `should provide a collection of products`() {
        // When
        val products = mockDataSource.findAll()

        // Then
        assertThat(products).isNotEmpty
        assertThat(products.size).isEqualTo(mockDataSource.products.size)
    }

    @Test
    fun `should provide some mock data`() {
        // When
        val products = mockDataSource.findAll()

        // Then
        assertThat(products).allMatch { it.title.isNotBlank() }
        assertThat(products).anyMatch { it.price != 0.0 }
        assertThat(products).anyMatch { it.description.startsWith("Description") }
    }
}