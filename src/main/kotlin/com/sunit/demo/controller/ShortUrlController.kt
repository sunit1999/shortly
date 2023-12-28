package com.sunit.demo.controller

import com.sunit.demo.dto.ShortUrlDTO
import com.sunit.demo.models.ShortUrl
import com.sunit.demo.service.ShortUrlService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ShortUrlController(private val shortUrlService: ShortUrlService) {

    @PostMapping("/api/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createShortUrl(@RequestBody @Valid shortUrlDTO: ShortUrlDTO): ShortUrl {
        return shortUrlService.createShortUrl(shortUrlDTO.longUrl)
    }
}
