package com.focus617.webbackendspringboot.data.seeders

import com.focus617.webbackendspringboot.domain.model.Product
import com.focus617.webbackendspringboot.domain.repository.ProductRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class DataSeeder (private val productRepository: ProductRepository) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        for (i in 1..50) {
            val product = Product()
            product.id = i                          // used only for MockDataSource
            product.title = "Title #" + i
            product.description = "Description #" + (i + 1)
            product.image = "http://focus617.com/200/200?" + Random.nextInt(10000)
            product.price = Random.nextDouble(10.0, 100.0)
            this.productRepository.create(product)
        }
    }

}