package com.focus617.webbackendspringboot.domain.repository

import com.focus617.webbackendspringboot.data.datasource.ProductDataSource
import com.focus617.webbackendspringboot.domain.model.Product
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class ProductRepository(@Qualifier("Database") private val dataSource: ProductDataSource) {
    fun findAll(): List<Product> = dataSource.findAll()

    fun findAll(s: String, sort: String, page: Int, sizePerPage: Int = 10): List<Product> {
        val direction = when (sort) {
            "asc" -> Sort.by(Sort.Direction.ASC, "price")
            "desc" -> Sort.by(Sort.Direction.DESC, "price")
            else -> Sort.unsorted()
        }
        return dataSource.findAll(s, direction, page, sizePerPage)
    }

    fun findById(id: Int): Product {
        return dataSource.findById(id) ?: throw NoSuchElementException("Could not find a Product with id=$id")
    }

    fun create(product: Product): Product {

        val products = dataSource.findAll()

        if (products.any { it.id == product.id }) {
            throw IllegalArgumentException("Product with ID ${product.id} already exists.")
        } else if (products.any { it.code == product.code }) {
            throw IllegalArgumentException("Product CODE ${product.code} already taken.")
        }

        return dataSource.create(product)
    }

    fun update(product: Product): Product {
        val products = dataSource.findAll()
        if (products.any { it.id == product.id }) {
            return dataSource.update(product)
        } else {
            throw NoSuchElementException("Could not find a product with id=${product.id}")
        }
    }

    fun deleteById(id: Int) {
        val products = dataSource.findAll()
        products.firstOrNull { it.id == id }
            ?: throw NoSuchElementException("Could not find a product with id=${id}")

        dataSource.deleteById(id)
    }

    fun countSearch(s: String): Int = dataSource.countSearch(s)
}