package com.sunit.demo.service

import com.sunit.demo.exception.ResourceNotFoundException
import com.sunit.demo.models.ShortUrl
import com.sunit.demo.repository.ShortUrlRepository
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDateTime

@Service
class ShortUrlService(val shortUrlRepository: ShortUrlRepository) {

    fun getLongUrlFromKey(shortUrlKey: String): ShortUrl {
        return shortUrlRepository.findById(shortUrlKey).orElseThrow {
            throw ResourceNotFoundException("No URL found for key $shortUrlKey")
        }
    }

    fun createShortUrl(longUrl: String): ShortUrl {
        val key = generateShortUrl(longUrl)
        val shortUrlEntry = ShortUrl(
            key = key,
            shortUrl = BASE_URL + key,
            longUrl = longUrl,
            createdAt = LocalDateTime.now(),
            expirationAt = LocalDateTime.now().plusYears(100)
        )

        return shortUrlRepository.save(shortUrlEntry)
    }

    private fun generateShortUrl(longUrl: String): String {
        val md5Bytes = MessageDigest.getInstance("MD5").digest(longUrl.toByteArray())
        val md5Hex = md5Bytes.joinToString("") { "%02x".format(it) }

        val base62Encoded = base62Encode(md5Hex)

        return base62Encoded.take(URL_LENGTH)
    }

    private fun base62Encode(input: String): String {
        val base62Chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

        // Step 1: Convert input string to byte array, basically ASCII numbers
        val inputBytes = input.toByteArray()

        // Step 2: Convert byte array to numeric value
        val numericValue = BigInteger(1, inputBytes)

        // Step 3: Convert numeric value to Base62 string
        var result = ""
        var remaining = numericValue

        while (remaining > BigInteger.ZERO) {
            val remainder = remaining.mod(BigInteger.valueOf(62))
            result = base62Chars[remainder.toInt()] + result
            remaining /= BigInteger.valueOf(62)
        }

        return result.ifEmpty { "0" }
    }

    companion object {
        const val URL_LENGTH = 7
        const val BASE_URL = "http://localhost:8080/"
    }
}

