package com.focus617.webbackendspringboot.data.dtos

data class PaginatedResponse(
    val data: List<Any>,
    val total: Int,
    val page: Int,
    val last_page: Int
)