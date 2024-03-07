package api

import common.ResourceFileReader.Companion.instance
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.IOException

internal class MeterReadingsTest : APITest() {
    @Test
    @Throws(IOException::class)
    fun validMeterReadingSubmission() {
        val validMeterReadings = resourceFileReader!!.readContentOfResource("validMeterReading_1.json")
        postMeterReadings(validMeterReadings, 201)
    }

    @Test
    @Throws(IOException::class)
    fun invalidMeterReadingSubmission() {
        val invalidMeterReadings = resourceFileReader!!.readContentOfResource("invalidMeterReading_1.json")
        postMeterReadings(invalidMeterReadings, 400)
    }

    private fun postMeterReadings(input: String, expectedStatusCode: Int): ValidatableResponse {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(input)
                .`when`()
                .post(METER_READINGS_ENDPOINT)
                .then()
                .statusCode(expectedStatusCode)
    }

    companion object {
        private const val METER_READINGS_ENDPOINT = "/meterreadings/list"
        private val resourceFileReader = instance

        @BeforeAll
        @Throws(IOException::class)
        fun setupTestData() {
            createProfileWithFractions()
        }

        @Throws(IOException::class)
        private fun createProfileWithFractions() {
            val profile = resourceFileReader!!.readContentOfResource("initial_profile_1.json")
            val fractions = resourceFileReader.readContentOfResource("initial_fractions_1.json")
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(profile)
                    .`when`()
                    .post("/profiles")
                    .then()
                    .statusCode(201)

            for (i in 1..10) {
                RestAssured.given()
                        .`when`()
                        .delete("/profiles/fractions/$i")
            }

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(fractions)
                    .`when`()
                    .post("/profiles/1/fractions")
                    .then()
                    .statusCode(201)
        }
    }
}
