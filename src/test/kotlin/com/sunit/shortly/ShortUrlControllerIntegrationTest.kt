package com.sunit.shortly

import com.fasterxml.jackson.databind.ObjectMapper
import com.sunit.shortly.models.ShortUrl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ShortUrlControllerIntegrationTest {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	private fun createShortUrl(shortUrlDTO: String): ResultActions {
		val result = mockMvc.perform(
			post("/api/url/create")
				.content(shortUrlDTO)
				.contentType(MediaType.APPLICATION_JSON)
		)

		return result
	}

	@Test
	fun `test short url is created`() {
		val shortUrlDTO = """
			{
				"domain": "http://example.com/",
				"longUrl": "http://google.com/"
			}
			""".trimIndent()


		val result = createShortUrl(shortUrlDTO)
			.andExpect(status().isCreated)
			.andReturn()

		val json = result.response.contentAsString
		val shortUrlEntry = objectMapper.readValue(json, ShortUrl::class.java)

		assertTrue(shortUrlEntry.hash.isNotBlank())
		assertEquals(7, shortUrlEntry.hash.length)
	}

	@Test
	fun `test short url creation fails for empty domain`() {
		val shortUrlDTO = """
			{
				"domain": "",
				"longUrl": "http://google.com/"
			}
			""".trimIndent()

		createShortUrl(shortUrlDTO)
			.andExpect(status().isBadRequest)
	}

	@Test
	fun `test short url creation fails for empty long url`() {
		val shortUrlDTO = """
			{
				"domain": "http://www.example.com/",
				"longUrl": ""
			}
			""".trimIndent()

		createShortUrl(shortUrlDTO)
			.andExpect(status().isBadRequest)
	}

	@Test
	fun `test short url creation fails for invalid long url`() {
		val shortUrlDTO = """
			{
				"domain": "http://www.example.com/",
				"longUrl": "htt://www.google.com/"
			}
			""".trimIndent()

		createShortUrl(shortUrlDTO)
			.andExpect(status().isBadRequest)
	}

	@Test
	fun `test opening short url works`() {
		val shortUrlDTO = """
			{
				"domain": "http://example.com/",
				"longUrl": "http://google.com/"
			}
			""".trimIndent()
		val result = createShortUrl(shortUrlDTO)
			.andReturn()

		val json = result.response.contentAsString
		val shortUrlEntry = objectMapper.readValue(json, ShortUrl::class.java)

		mockMvc.perform(get("/${shortUrlEntry.hash}"))
			.andExpect(status().is3xxRedirection)
			.andExpect(header().exists("Location"))
			.andExpect(redirectedUrl(shortUrlEntry.longUrl))
	}

	@Test
	fun `test opening short url fails for invalid input`() {
		val testHashes = listOf("abc", "abcdefgh")

		testHashes.forEach { hash ->
			mockMvc.perform(get("/$hash"))
				.andExpect(status().isBadRequest)
		}
	}

	@Test
	fun `test opening short url fails for non-existing input`() {
		val hash = "abcdefg"
		mockMvc.perform(get("/$hash"))
			.andExpect(status().isNotFound)
	}
}
