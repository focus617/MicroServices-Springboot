package com.focus617.webbackendspringboot.data.datasource

import com.focus617.webbackendspringboot.data.dao.ProductDao
import com.focus617.webbackendspringboot.domain.model.Product
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository("Database")
class DatabaseDataSource(private val productDao: ProductDao) : ProductDataSource {

    override fun findAll(): List<Product> = productDao.findAll()

    override fun findAll(s: String, sort: Sort, page: Int, sizePerPage: Int): List<Product> =
        productDao.search(s, PageRequest.of(page - 1, sizePerPage, sort))

    override fun findById(id: Int): Product? = productDao.findAllById(listOf(id))[0]

    override fun create(product: Product): Product = productDao.save(product)

    override fun update(product: Product): Product {
        if (!productDao.existsById(product.id)) {
            throw NoSuchElementException("Could not find a Product with ID=${product.id}")
        }
        return productDao.save(product)
    }

    override fun deleteById(id: Int) {
        if (!productDao.existsById(id)){
            throw NoSuchElementException("Could not find a Product with ID=${id}")
        }
        else {
            productDao.deleteAllById(listOf(id))
        }
    }

    override fun countSearch(s: String): Int = productDao.countSearch(s)

}