package com.focus617.webbackendspringboot.domain.interactors

import com.focus617.webbackendspringboot.domain.interactors.dtos.PaginatedResponse
import com.focus617.webbackendspringboot.domain.model.Product
import com.focus617.webbackendspringboot.domain.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getProducts(): Collection<Product> = productRepository.findAll()

    fun getProduct(id: Int): Product = productRepository.findById(id)

    fun getBackendProducts(s: String, sort: String, page: Int): Any {
        val sizePerPage = 10
        val total = productRepository.countSearch(s)

        return PaginatedResponse(
            data = productRepository.findAll(s, sort, page, sizePerPage),
            total,
            sizePerPage,
            page,
            totalPages = (total / sizePerPage) + 1
        )
    }

    fun addNewProduct(product: Product): Product = productRepository.create(product)

    fun updateProduct(product: Product): Product = productRepository.update(product)

    fun updateProduct(id: Int, title: String?, description: String?, image: String?, price: Double): Product =
        productRepository.update(id, title, description, image, price)

    fun deleteProduct(id: Int) = productRepository.deleteById(id)

}