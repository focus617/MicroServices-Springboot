package com.focus617.webbackendspringboot.data.datasource

import com.focus617.webbackendspringboot.data.dao.ProductDao
import com.focus617.webbackendspringboot.domain.model.Product
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository("Database")
class DatabaseProductDataSource(private val productDao: ProductDao) : ProductDataSource {

    override fun findAll(): List<Product> = productDao.findAll()

    override fun findOnePage(s: String, sort: Sort, pageNumber: Int, sizePerPage: Int): List<Product> =
        productDao.search(s, PageRequest.of(pageNumber - 1, sizePerPage, sort))

    override fun findById(id: Int): Product? {
        val products = productDao.findAllById(listOf(id))
        return if (products.size != 0) products[0] else null
    }

    override fun create(product: Product): Product = productDao.saveAndFlush(product)

    override fun update(product: Product): Product = productDao.saveAndFlush(product)

    override fun deleteById(id: Int) = productDao.deleteAllById(listOf(id))

    override fun existsById(id: Int): Boolean = productDao.existsById(id)

    override fun countSearch(s: String): Int = productDao.countSearch(s)

}