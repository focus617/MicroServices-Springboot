package com.focus617.webbackendspringboot.domain.interactors.dtos

data class ProductRegistrationRequest(
    var code: String = "",
    var title: String = "",
    var description: String = "",
    var image: String = "",
    var price: Double = 0.0
)