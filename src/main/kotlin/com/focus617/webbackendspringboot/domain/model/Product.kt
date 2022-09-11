package com.focus617.webbackendspringboot.domain.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Product (

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0,

    @Column
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
