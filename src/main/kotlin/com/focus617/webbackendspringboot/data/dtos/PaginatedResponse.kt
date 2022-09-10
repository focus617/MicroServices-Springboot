package com.focus617.webbackendspringboot.data.dtos

data class PaginatedResponse(
    val data: List<Any>,
    val totalElements: Int,
    val elementSizePerPage: Int,
    val page: Int,
    val totalPages: Int
)