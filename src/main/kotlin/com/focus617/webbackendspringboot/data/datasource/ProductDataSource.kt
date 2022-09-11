package com.focus617.webbackendspringboot.data.datasource

import com.focus617.webbackendspringboot.domain.model.Product
import org.springframework.data.domain.Sort

interface ProductDataSource {
    fun findAll(): List<Product>
    fun findAll(s: String, sort: Sort, page: Int, sizePerPage: Int = 10): List<Product>
    fun findById(id: Int): Product?
    fun create(product: Product): Product
    fun update(product: Product): Product
    fun deleteById(id: Int): Unit
    fun countSearch(s: String): Int
}