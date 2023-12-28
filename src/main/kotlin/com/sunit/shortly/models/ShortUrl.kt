package com.sunit.shortly.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "shortUrls")
data class ShortUrl(

    @Id
    @Column(name = "hash", length = 7, nullable = false)
    val hash: String,

    @Column(name = "short_url", length = 255, nullable = false)
    val shortUrl: String,

    @Column(name = "long_url", length = 255, nullable = false)
    val longUrl: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "expiration_at", nullable = false)
    val expirationAt: LocalDateTime
)
