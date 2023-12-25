package com.sunit.demo.dto

import jakarta.validation.constraints.NotBlank

data class PersonDTO(
    val id: Long?,

    @field:NotBlank(message = "First name must not be blank")
    val firstName: String,

    @field:NotBlank(message = "Last name must not be blank")
    val lastName: String
)
