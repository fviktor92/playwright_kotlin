package ui

import com.microsoft.playwright.Page
import common.FileUtils.deleteFolder
import common.exceptions.MissingSystemPropertyException
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import ui.BrowserFactory.closeBrowser
import ui.BrowserFactory.closeBrowserContext
import ui.BrowserFactory.closePage
import ui.BrowserFactory.closePlaywright
import ui.BrowserFactory.getPage
import ui.extensions.ReportingExtension
import java.io.File

@ExtendWith(ReportingExtension::class)
internal open class TestFixtures {
    @JvmField
    var page: Page? = null

    @BeforeEach
    @Throws(MissingSystemPropertyException::class)
    fun beforeEach() {
        page = getPage(null)
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll(): Unit {
            val allureResultsPath = "allure-results"

            // Create a File object for the folder
            val allureResultsFolder = File(allureResultsPath)

            // Check if the folder exists
            if (allureResultsFolder.exists()) {
                // If it exists, delete it
                deleteFolder(allureResultsFolder)
            }
        }

        @JvmStatic
        @AfterAll
        fun afterAll(): Unit {
            closePage()
            closeBrowserContext()
            closeBrowser()
            closePlaywright()
        }
    }
}
