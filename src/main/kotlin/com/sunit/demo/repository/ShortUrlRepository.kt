package com.sunit.demo.repository

import com.sunit.demo.models.ShortUrl
import org.springframework.data.jpa.repository.JpaRepository

interface ShortUrlRepository : JpaRepository<ShortUrl, String>