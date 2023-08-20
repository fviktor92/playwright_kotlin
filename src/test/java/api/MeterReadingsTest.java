package api;

import common.ResourceFileReader;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;

class MeterReadingsTest extends APITest {

    private static final String METER_READINGS_ENDPOINT = "/meterreadings/list";
    private static final ResourceFileReader resourceFileReader = ResourceFileReader.getInstance();

    @BeforeAll
    static void setupTestData() throws IOException {
        createProfileWithFractions();
    }

    @Test
    void validMeterReadingSubmission() throws IOException {
        String validMeterReadings = resourceFileReader.readContentOfResource("validMeterReading_1.json");
        postMeterReadings(validMeterReadings, 201);
    }

    @Test
    void invalidMeterReadingSubmission() throws IOException {
        String invalidMeterReadings = resourceFileReader.readContentOfResource("invalidMeterReading_1.json");
        postMeterReadings(invalidMeterReadings, 400);
    }

    private ValidatableResponse postMeterReadings(String input, int expectedStatusCode) {
        return given()
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .post(METER_READINGS_ENDPOINT)
                .then()
                .statusCode(expectedStatusCode);
    }

    private static void createProfileWithFractions() throws IOException {
        String profile = resourceFileReader.readContentOfResource("initial_profile_1.json");
        String fractions = resourceFileReader.readContentOfResource("initial_fractions_1.json");
        given()
                .contentType(ContentType.JSON)
                .body(profile)
                .when()
                .post("/profiles")
                .then()
                .statusCode(201);

        for (int i = 1; i <= 10; i++) {
            given()
                    .when()
                    .delete("/profiles/fractions/" + i);
        }

        given()
                .contentType(ContentType.JSON)
                .body(fractions)
                .when()
                .post("/profiles/1/fractions")
                .then()
                .statusCode(201);
    }
}
