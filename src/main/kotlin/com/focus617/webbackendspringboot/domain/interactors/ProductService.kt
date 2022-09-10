package com.focus617.webbackendspringboot.domain.interactors

import com.focus617.webbackendspringboot.domain.interactors.dtos.PaginatedResponse
import com.focus617.webbackendspringboot.domain.model.Product
import com.focus617.webbackendspringboot.domain.repository.ProductRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getProducts(): Collection<Product> = productRepository.findAll()

    fun getProduct(id: Int): Product {
        val foundProducts = productRepository.findAllById(listOf(id))

        if (foundProducts.size == 0) throw NoSuchElementException("Could not find a Product with ID=$id")
        else return foundProducts[0]
    }

    fun getBackendProducts(s: String, sort: String, page: Int): Any {
        val direction = when (sort) {
            "asc" -> Sort.by(Sort.Direction.ASC, "price")
            "desc" -> Sort.by(Sort.Direction.DESC, "price")
            else -> Sort.unsorted()
        }

        val sizePerPage = 10
        val total = productRepository.countSearch(s)

        return PaginatedResponse(
            data = productRepository.search(s, PageRequest.of(page - 1, sizePerPage, direction)),
            total,
            sizePerPage,
            page,
            totalPages = (total / sizePerPage) + 1
        )
    }

    fun addProduct(product: Product): Product {

        val products = productRepository.findAll()
        if (products.any { it.id == product.id }) {
            throw IllegalArgumentException("Product with ID ${product.id} already exists.")
        }

        return productRepository.save(product)
    }

    fun updateProduct(product: Product): Product {

        if (!productRepository.existsById(product.id)){
            throw NoSuchElementException("Could not find a Product with ID=${product.id}")
        }
        else {
            productRepository.save(product)
            return product
        }

    }

    fun deleteProduct(id: Int) {

        if (!productRepository.existsById(id)){
            throw NoSuchElementException("Could not find a Product with ID=${id}")
        }
        else {
            productRepository.deleteAllById(listOf(id))
        }
    }
}