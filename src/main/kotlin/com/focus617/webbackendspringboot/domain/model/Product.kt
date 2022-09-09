package com.focus617.webbackendspringboot.domain.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id = 0

    @Column
    var title = ""

    @Column
    var description = ""

    @Column
    var image = ""

    @Column
    var price  = 0.0

}
