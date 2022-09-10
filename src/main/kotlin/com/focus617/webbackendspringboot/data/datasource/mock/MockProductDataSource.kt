package com.focus617.webbackendspringboot.data.datasource.mock

import com.focus617.webbackendspringboot.data.datasource.ProductDataSource
import com.focus617.webbackendspringboot.domain.model.Product
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository("Mock")
class MockProductDataSource : ProductDataSource {

    val products = mutableListOf<Product>(
        Product(1, "Title #1", "Description #1", "http://focus617.com/200/200?1", 19.99),
        Product(2, "Title #2", "Description #2", "http://focus617.com/200/200?2", 29.99),
        Product(3, "Title #3", "Description #3", "http://focus617.com/200/200?3", 39.99)
    )

    override fun findAll(): List<Product> = products

    override fun findAll(s: String, sort: Sort, page: Int, sizePerPage: Int): List<Product> {
        return products
    }

    override fun findById(id: Int): Product? = products.firstOrNull { it.id == id }

    override fun create(product: Product): Product {
        products.add(product)
        return product
    }

    override fun update(product: Product): Product {
        val currentProduct = products.firstOrNull { it.id == product.id }
        products.remove(currentProduct)
        products.add(product)
        return product
    }

    override fun deleteById(id: Int) {
        products.firstOrNull { it.id == id } ?.let{ products.remove(it) }
    }

    override fun countSearch(s: String): Int {
        return products.size
    }
}