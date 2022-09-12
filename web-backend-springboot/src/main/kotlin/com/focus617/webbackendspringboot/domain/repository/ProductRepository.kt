package com.focus617.webbackendspringboot.domain.repository

import com.focus617.webbackendspringboot.data.datasource.ProductDataSource
import com.focus617.webbackendspringboot.domain.model.Product
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

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

        if (dataSource.existsById(product.id)) {
            throw IllegalArgumentException("Product with ID ${product.id} already exists.")
        } else if (existsByCode(product.code)) {
            throw IllegalArgumentException("Product CODE ${product.code} already taken.")
        }

        return dataSource.create(product)
    }

    fun update(product: Product): Product {
        if (!dataSource.existsById(product.id)) {
            throw NoSuchElementException("Could not find a product with id=${product.id}")
        } else if (existsByCode(product.code)) {
            throw IllegalArgumentException("Product CODE ${product.code} already taken.")
        }

        return dataSource.update(product)
    }

    @Transactional
    fun update(id: Int, title: String?, description: String?, image: String?, price: Double): Product {
        val product = dataSource.findById(id)
            ?: throw NoSuchElementException("Could not find a product with id=${id}")

        if (!title.isNullOrEmpty() && (!Objects.equals(product.title, title))) {
            product.title = title
        }

        if (!description.isNullOrEmpty() && (!Objects.equals(product.description, description))) {
            product.description = description
        }

        if (!image.isNullOrEmpty() && (!Objects.equals(product.image, image))) {
            product.image = image
        }

        if ((price != 0.000) && (!Objects.equals(product.price, price))) {
            product.price = price
        }

        return product
    }

    fun deleteById(id: Int) {
        if (dataSource.existsById(id)) {
            dataSource.deleteById(id)
        } else {
            throw NoSuchElementException("Could not find a product with id=${id}")
        }
    }

    fun countSearch(s: String): Int = dataSource.countSearch(s)


    private fun existsByCode(code: String): Boolean = dataSource.findAll().any { it.code == code }
}