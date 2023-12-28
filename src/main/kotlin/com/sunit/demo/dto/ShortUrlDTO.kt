package com.sunit.demo.dto

import org.hibernate.validator.constraints.URL

data class ShortUrlDTO(
    @field:URL(message = "Invalid URL")
    val longUrl: String
)
