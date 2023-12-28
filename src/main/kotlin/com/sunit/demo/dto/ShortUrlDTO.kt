package com.sunit.demo.dto

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.URL

data class ShortUrlDTO(
    @field:NotBlank(message = "Missing Domain")
    @field:URL(message = "Invalid Domain URL")
    val domain: String,

    @field:NotBlank(message = "Missing Long URL")
    @field:URL(message = "Invalid Long URL")
    val longUrl: String
)
