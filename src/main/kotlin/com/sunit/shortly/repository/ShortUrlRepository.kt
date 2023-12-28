package com.sunit.shortly.repository

import com.sunit.shortly.models.ShortUrl
import org.springframework.data.jpa.repository.JpaRepository

interface ShortUrlRepository : JpaRepository<ShortUrl, String>