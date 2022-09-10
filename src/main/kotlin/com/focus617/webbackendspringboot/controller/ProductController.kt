package com.focus617.webbackendspringboot.controller

import com.focus617.webbackendspringboot.data.dtos.PaginatedResponse
import com.focus617.webbackendspringboot.domain.model.Product
import com.focus617.webbackendspringboot.domain.repository.ProductRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController(private val productRepository: ProductRepository) {

    @GetMapping
    fun getProducts(): Collection<Product> = this.productRepository.findAll()

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: Int): Product{
        val foundProducts = this.productRepository.findAllById(listOf(id))

        if(foundProducts.size==0) throw NoSuchElementException("Could not find a Product with ID=$id")
        else return foundProducts[0]
    }

    @GetMapping("/backend")
    fun getBackendProducts(
        @RequestParam("s", defaultValue = "") s: String,
        @RequestParam("sort", defaultValue = "") sort: String,
        @RequestParam("page", defaultValue = "1") page: Int
    ): Any {
        val direction = when (sort) {
            "asc" -> Sort.by(Sort.Direction.ASC, "price")
            "desc" -> Sort.by(Sort.Direction.DESC, "price")
            else -> Sort.unsorted()
        }

        val sizePerPage = 10
        val total = this.productRepository.countSearch(s)

        return PaginatedResponse(
            data = this.productRepository.search(s, PageRequest.of(page - 1, sizePerPage, direction)),
            total,
            sizePerPage,
            page,
            totalPages = (total / sizePerPage) + 1
        )
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addProduct(@RequestBody product: Product): Product {

        val products = this.productRepository.findAll()
        if (products.any { it.id == product.id }) {
            throw IllegalArgumentException("Product with ID ${product.id} already exists.")
        }

        return this.productRepository.save(product)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

}