package com.focus617.webbackendspringboot.domain.repository

import com.focus617.webbackendspringboot.data.datasource.ProductDataSource
import com.focus617.webbackendspringboot.domain.model.Product
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class ProductRepository(@Qualifier("Mock") private val dataSource: ProductDataSource) {
    fun findAll(): List<Product> = dataSource.findAll()

    fun findAll(s: String, sort: String, page: Int, sizePerPage: Int = 10): List<Product>{
        val direction = when (sort) {
            "asc" -> Sort.by(Sort.Direction.ASC, "price")
            "desc" -> Sort.by(Sort.Direction.DESC, "price")
            else -> Sort.unsorted()
        }
        return dataSource.findAll(s, direction, page, sizePerPage)
    }

    fun findById(id: Int): Product? = dataSource.findById(id)

    fun create(product: Product): Product {
        val products = dataSource.findAll()
        if (products.any { it.id == product.id }) {
            throw IllegalArgumentException("Product with ID ${product.id} already exists.")
        }
        return dataSource.create(product)
    }

    fun update(product: Product): Product = dataSource.update(product)
    fun deleteById(id: Int): Unit = dataSource.deleteById(id)
    fun countSearch(s: String): Int = dataSource.countSearch(s)
}