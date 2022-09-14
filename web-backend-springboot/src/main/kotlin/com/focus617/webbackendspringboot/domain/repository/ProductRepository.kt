package com.focus617.webbackendspringboot.domain.repository

import com.focus617.webbackendspringboot.data.datasource.ProductDataSource
import com.focus617.webbackendspringboot.domain.model.Product
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class ProductRepository(@Qualifier("Database") private val dataSource: ProductDataSource) {

    private val log: Logger = LoggerFactory.getLogger(ProductRepository::class.java)

    fun findAll(): List<Product> = dataSource.findAll()

    fun findOnePage(s: String, sort: String, page: Int, sizePerPage: Int = 10): List<Product> {
        val direction = when (sort) {
            "asc" -> Sort.by(Sort.Direction.ASC, "price")
            "desc" -> Sort.by(Sort.Direction.DESC, "price")
            else -> Sort.unsorted()
        }
        return dataSource.findOnePage(s, direction, page, sizePerPage)
    }

    fun findOnePage(pageNumber: Int, sizePerPage: Int = 10): Page<Product> =
        dataSource.findOnePage(pageNumber, sizePerPage)

    fun findById(id: Int): Product {
        return dataSource.findById(id) ?: throw NoSuchElementException("Could not find a Product with id=$id")
    }

    fun create(product: Product): Product {
        if (existsByCode(product.code)) {
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