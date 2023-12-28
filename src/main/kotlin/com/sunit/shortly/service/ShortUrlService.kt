package com.sunit.shortly.service

import com.sunit.shortly.dto.ShortUrlDTO
import com.sunit.shortly.models.ShortUrl
import com.sunit.shortly.repository.ShortUrlRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDateTime

@Service
class ShortUrlService(val shortUrlRepository: ShortUrlRepository) {

    fun getLongUrlFromHash(hash: String): ShortUrl {
        return shortUrlRepository.findById(hash).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "No URL found for key $hash")
        }
    }

    fun createShortUrl(shortUrlDTO: ShortUrlDTO): ShortUrl {
        val hash = generateShortUrl(shortUrlDTO.longUrl)
        val shortUrlEntry = ShortUrl(
            hash = hash,
            shortUrl = shortUrlDTO.domain + hash,
            longUrl = shortUrlDTO.longUrl,
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
    }
}
