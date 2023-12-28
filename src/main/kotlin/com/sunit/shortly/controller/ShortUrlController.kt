package com.sunit.shortly.controller

import com.sunit.shortly.dto.ShortUrlDTO
import com.sunit.shortly.models.ShortUrl
import com.sunit.shortly.service.ShortUrlService
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView

@RestController
class ShortUrlController(private val shortUrlService: ShortUrlService) {

    @PostMapping("/api/url/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createShortUrl(@RequestBody @Valid shortUrlDTO: ShortUrlDTO): ShortUrl {
        return shortUrlService.createShortUrl(shortUrlDTO)
    }

    @GetMapping("/{hash}")
    fun openShortUrl(@PathVariable @Size(min = 7, max = 7) hash: String): RedirectView {
        val longUrl = shortUrlService.getLongUrlFromHash(hash).longUrl
        return RedirectView(longUrl)
    }

}
