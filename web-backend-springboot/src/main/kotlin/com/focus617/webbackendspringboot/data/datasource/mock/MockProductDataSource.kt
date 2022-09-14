package com.focus617.webbackendspringboot.data.datasource.mock

import com.focus617.webbackendspringboot.data.datasource.ProductDataSource
import com.focus617.webbackendspringboot.domain.model.Product
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository("Mock")
class MockProductDataSource : ProductDataSource {

    val products = mutableListOf<Product>()
    private var lastId: Int = 1

    override fun findAll(): List<Product> = products

    override fun findOnePage(s: String, sort: Sort, pageNumber: Int, sizePerPage: Int): List<Product> =
        products.sortedBy { it.id }.windowed(sizePerPage, 1, true)[pageNumber]

    override fun findById(id: Int): Product? = products.firstOrNull { it.id == id }

    override fun create(product: Product): Product {
        product.id = lastId++
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