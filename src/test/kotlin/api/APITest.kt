package api

import common.FileUtils.deleteFolder
import io.qameta.allure.restassured.AllureRestAssured
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeAll
import java.io.File

open class APITest {
    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll(): Unit {
            deleteAllureResults()
            setDefaultConfigurations()
        }

        private fun setDefaultConfigurations() {
            RestAssured.port = 8080
            RestAssured.filters(AllureRestAssured())
        }

        private fun deleteAllureResults() {
            val allureResultsPath = "allure-results"

            // Create a File object for the folder
            val allureResultsFolder = File(allureResultsPath)

            // Check if the folder exists
            if (allureResultsFolder.exists()) {
                // If it exists, delete it
                deleteFolder(allureResultsFolder)
            }
        }
    }
}
