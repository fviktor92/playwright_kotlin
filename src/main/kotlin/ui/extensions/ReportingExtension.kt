package ui.extensions

import com.microsoft.playwright.Page
import common.exceptions.MissingSystemPropertyException
import io.qameta.allure.Allure
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import ui.BrowserFactory
import java.io.ByteArrayInputStream

class ReportingExtension : AfterEachCallback {
    @Throws(MissingSystemPropertyException::class)
    override fun afterEach(extensionContext: ExtensionContext) {
        val screenshot = BrowserFactory.getPage(null)?.screenshot(Page.ScreenshotOptions().setFullPage(true))
        Allure.addAttachment("Screenshot", ByteArrayInputStream(screenshot))
    }
}
