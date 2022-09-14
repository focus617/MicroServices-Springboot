package com.focus617.webbackendspringboot.controller

import com.focus617.webbackendspringboot.domain.interactors.ProductService
import com.focus617.webbackendspringboot.domain.model.Product
import org.springframework.data.domain.Page
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ThymeleafController(private val service: ProductService) {

    @GetMapping("/index")
    fun index(model: Model): String {
        model.addAttribute("welcome", "Hello, this is test page for Thymeleaf.")
        return "index"
    }

    @GetMapping("/api/v1/products/pages/parameters")
    fun getPageWithSort(
        model: Model,
        @RequestParam("field", defaultValue = "price") field: String,
        @RequestParam("sort", defaultValue = "asc") sort: String,
        @RequestParam("page", defaultValue = "1") currentPage: Int,
        @RequestParam("limit", defaultValue = "10") limit: Int
    ): String {
        val page: Page<Product> = service.getProductsInPageWithSorting(field, sort, currentPage, limit)

        val totalPages = page.totalPages
        val totalItems = page.totalElements
        val products: List<Product> = page.content

        model.addAttribute("welcome", "Hello, this is test page for Thymeleaf.")
        model.addAttribute("currentPage", currentPage)
        model.addAttribute("totalPages", totalPages)
        model.addAttribute("totalItems", totalItems)
        model.addAttribute("products", products)

        return "index"
    }

}