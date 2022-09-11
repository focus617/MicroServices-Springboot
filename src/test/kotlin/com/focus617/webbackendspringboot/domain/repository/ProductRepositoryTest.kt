package com.focus617.webbackendspringboot.domain.repository

import com.focus617.webbackendspringboot.data.datasource.mock.MockProductDataSource
import com.focus617.webbackendspringboot.domain.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

internal class ProductRepositoryTest {

    private val mockDataSource = MockProductDataSource()
    private val repository = ProductRepository(mockDataSource)

    private val product101 =
        Product(101, "Code#101", "Title #1", "Description #1", "http://focus617.com/200/200?1", 19.99)
    private val product102 =
        Product(102, "Code#102", "Title #2", "Description #2", "http://focus617.com/200/200?2", 29.99)
    private val product103 =
        Product(103, "Code#103", "Title #3", "Description #3", "http://focus617.com/200/200?3", 39.99)

    @BeforeEach
    fun setUp() {
        mockDataSource.products.add(product101)
        mockDataSource.products.add(product102)
        mockDataSource.products.add(product103)
    }

    @Nested
    @DisplayName("findAll")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class FindAll {

        @Test
        fun `should provide a collection of products`() {
            // When
            val products = repository.findAll()

            // Then
            assertThat(products).isNotEmpty
            assertThat(products.size).isEqualTo(mockDataSource.products.size)
        }

        @Test
        fun `should provide some mock data`() {
            // When
            val products = repository.findAll()

            // Then
            assertThat(products).allMatch { it.title.isNotBlank() }
            assertThat(products).anyMatch { it.price != 0.0 }
            assertThat(products).anyMatch { it.description.startsWith("Description") }
        }

    }

    @Nested
    @DisplayName("findById")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class FindById {

        @Test
        fun `should return the Product with the given id `() {
            // When
            val product = repository.findById(product102.id)

            // Then
            assertThat(product.id).isEqualTo(product102.id)
            assertThat(product).isEqualTo(product102)
        }

        @Test
        fun `should return null if the given product id does not exist`() {
            // Given
            val invalidId = 9999

            // When / Then
            val exceptionThrown = Assertions.assertThrows(
                NoSuchElementException::class.java,
                { repository.findById(invalidId) },
                "java.util.NoSuchElementException was expected"
            )
            assertThat(exceptionThrown).hasMessageContaining("Could not find a Product with id=$invalidId")
        }
    }
}