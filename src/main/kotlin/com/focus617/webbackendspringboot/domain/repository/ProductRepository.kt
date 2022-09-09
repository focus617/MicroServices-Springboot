package com.focus617.webbackendspringboot.domain.repository

import com.focus617.webbackendspringboot.domain.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<Product, Int> {
}