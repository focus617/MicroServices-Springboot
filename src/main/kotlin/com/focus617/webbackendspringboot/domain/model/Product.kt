package com.focus617.webbackendspringboot.domain.model

import javax.persistence.*

@Table(name = "product")
@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0,

    @Column(name = "code", nullable = false)
    var code: String = "",

    @Column
    var title: String = "",

    @Column
    var description: String = "",

    @Column
    var image: String = "",

    @Column
    var price: Double = 0.0

)
