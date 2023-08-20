package api;

import common.FileUtils;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;

public class APITest {
    @BeforeAll
    static void beforeAll() {
        deleteAllureResults();
        setDefaultConfigurations();
    }

    private static void setDefaultConfigurations() {
        RestAssured.port = 8080;
        RestAssured.filters(new AllureRestAssured());
    }

    private static void deleteAllureResults() {
        String allureResultsPath = "allure-results";

        // Create a File object for the folder
        File allureResultsFolder = new File(allureResultsPath);

        // Check if the folder exists
        if (allureResultsFolder.exists()) {
            // If it exists, delete it
            FileUtils.deleteFolder(allureResultsFolder);
        }
    }
}
