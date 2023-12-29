package com.sunit.shortly.dto

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.URL

data class ShortUrlDTO(
    @field:NotBlank(message = "Missing Long URL")
    @field:URL(message = "Invalid Long URL")
    val longUrl: String
)
