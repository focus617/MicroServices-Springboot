package com.focus617.webbackendspringboot.controller

import com.focus617.webbackendspringboot.domain.interactors.ProductService
import com.focus617.webbackendspringboot.domain.interactors.dtos.ProductRegistrationRequest
import com.focus617.webbackendspringboot.domain.model.Product
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
class ProductController(private val service: ProductService) {

    @GetMapping
    fun getProducts(): Collection<Product> = service.getProducts()

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: Int): Product = service.getProduct(id)

    @GetMapping("/parameters")
    fun getProductsWithParameters(
        @RequestParam("s", defaultValue = "") s: String,
        @RequestParam("sort", defaultValue = "") sort: String,
        @RequestParam("page", defaultValue = "1") page: Int
    ): Any = service.getBackendProducts(s, sort, page)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addProduct(@RequestBody request: ProductRegistrationRequest): Product {
        return service.registerNewProduct(request)
    }

    @PatchMapping
    fun updateProduct(@RequestBody product: Product): Product = service.updateProduct(product)

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Int,
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) description: String?,
        @RequestParam(required = false) image: String?,
        @RequestParam(required = false, defaultValue = "0.000") price: Double
    ): Product = service.updateProduct(id, title, description, image, price)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBank(@PathVariable id: Int): Unit = service.deleteProduct(id)

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

}