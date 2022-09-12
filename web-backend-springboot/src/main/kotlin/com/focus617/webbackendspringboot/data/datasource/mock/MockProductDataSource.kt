package com.focus617.webbackendspringboot.data.datasource.mock

import com.focus617.webbackendspringboot.data.datasource.ProductDataSource
import com.focus617.webbackendspringboot.domain.model.Product
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository("Mock")
class MockProductDataSource : ProductDataSource {

    val products = mutableListOf<Product>()

    override fun findAll(): List<Product> = products

    override fun findAll(s: String, sort: Sort, page: Int, sizePerPage: Int): List<Product> =
        products.sortedBy { it.id }.windowed(sizePerPage, 1, true)[page]

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
        products.firstOrNull { it.id == id }?.let { products.remove(it) }
    }

    override fun existsById(id: Int): Boolean = products.any { it.id == id }

    override fun countSearch(s: String): Int {
        return products.size
    }
}