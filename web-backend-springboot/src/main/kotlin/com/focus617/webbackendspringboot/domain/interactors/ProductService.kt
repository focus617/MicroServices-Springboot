package com.focus617.webbackendspringboot.domain.interactors

import com.focus617.webbackendspringboot.domain.interactors.dtos.PaginatedResponse
import com.focus617.webbackendspringboot.domain.interactors.dtos.ProductRegistrationRequest
import com.focus617.webbackendspringboot.domain.model.Product
import com.focus617.webbackendspringboot.domain.repository.ProductRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val restTemplate: RestTemplate
) {
    private val log: Logger = LoggerFactory.getLogger(ProductService::class.java)

    fun getProducts(): Collection<Product> = productRepository.findAll()

    fun getProduct(id: Int): Product = productRepository.findById(id)

    fun getProductsWithParameters(s: String, sort: String, page: Int): Any {
        val sizePerPage = 10
        val totalElements = productRepository.countSearch(s)

        return PaginatedResponse(
            data = productRepository.findOnePage(s, sort, page, sizePerPage),
            totalElements,
            sizePerPage,
            page,
            totalPages = (totalElements / sizePerPage) + 1
        )
    }

    fun getProductsInPage(page: Int): Page<Product> = productRepository.findOnePage(page)

    fun registerNewProduct(request: ProductRegistrationRequest): Product {
        log.info("Product registration/creation request received: {}", request)

        val product = productRepository.create(
            Product(
                0,
                request.code,
                request.title,
                request.description,
                request.image,
                request.price
            )
        )

        /** Communicate with other microservice **/
//        restTemplate.getForObject(
//            "http://localhost:8080/api/products/{productId}",
//            PaginatedResponse::class.java,
//            product.id
//        )

        return product
    }

    fun updateProduct(product: Product): Product = productRepository.update(product)

    fun updateProduct(id: Int, title: String?, description: String?, image: String?, price: Double): Product =
        productRepository.update(id, title, description, image, price)

    fun deleteProduct(id: Int) = productRepository.deleteById(id)

}