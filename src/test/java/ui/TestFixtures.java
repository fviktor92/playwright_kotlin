package ui;

import com.microsoft.playwright.Page;
import common.FileUtils;
import common.exceptions.MissingSystemPropertyException;
import org.junit.jupiter.api.*;
import ui.extensions.ReportingExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

@ExtendWith(ReportingExtension.class)
class TestFixtures {
    Page page;

    @BeforeAll
    static void beforeAll() {
        String allureResultsPath = "allure-results";

        // Create a File object for the folder
        File allureResultsFolder = new File(allureResultsPath);

        // Check if the folder exists
        if (allureResultsFolder.exists()) {
            // If it exists, delete it
            FileUtils.deleteFolder(allureResultsFolder);
        }
    }

    @AfterAll
    static void afterAll() {
        BrowserFactory.closePage();
        BrowserFactory.closeBrowserContext();
        BrowserFactory.closeBrowser();
        BrowserFactory.closePlaywright();
    }

    @BeforeEach
    void beforeEach() throws MissingSystemPropertyException {
        page = BrowserFactory.getPage(null);
    }
}
