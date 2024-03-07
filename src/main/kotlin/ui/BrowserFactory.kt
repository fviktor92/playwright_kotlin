package ui

import com.microsoft.playwright.*
import common.SystemPropertyReader.readSystemProperty
import common.exceptions.MissingSystemPropertyException

object BrowserFactory {
    private val playwrightThread = ThreadLocal<Playwright?>()
    private val browserTypeThread = ThreadLocal<BrowserType>()
    private val browserThread = ThreadLocal<Browser>()
    private val browserContextThread = ThreadLocal<BrowserContext>()
    private val pageThread = ThreadLocal<Page?>()

    
    @Synchronized
    @Throws(MissingSystemPropertyException::class)
    fun getPage(page: Page?): Page? {
        var page = page
        if (page != null) {
            pageThread.set(page)
        } else if (playwrightThread.get() == null) {
            val playwright = Playwright.create()
            playwrightThread.set(playwright)
            page = createPage(playwright)
            pageThread.set(page)
        }
        return pageThread.get()
    }

    
    @get:Synchronized
    val browser: Browser
        get() =// FIXME
            browserThread.get()

    
    @Synchronized
    fun closePage() {
        val page = pageThread.get()
        if (page != null) {
            page.close()
            pageThread.remove()
        }
    }
    
    @Synchronized
    fun closePlaywright() {
        val playwright = playwrightThread.get()
        if (playwright != null) {
            playwright.close()
            playwrightThread.remove()
        }
    }
    
    @Synchronized
    fun closeBrowserContext() {
        val browserContext = browserContextThread.get()
        if (browserContext != null) {
            browserContext.close()
            browserContextThread.remove()
        }
    }

    @Synchronized
    fun closeBrowser() {
        val browser = browserThread.get()
        if (browser != null) {
            browser.close()
            browserThread.remove()
        }
    }

    @Synchronized
    @Throws(MissingSystemPropertyException::class)
    private fun createPage(playwright: Playwright): Page {
        val browserType = getBrowserType(playwright)
        val browser = browserType.launch()
        val context = browser.newContext()



        browserTypeThread.set(browserType)
        browserThread.set(browser)
        browserContextThread.set(context)
        return context.newPage()
    }

    @Synchronized
    @Throws(MissingSystemPropertyException::class)
    private fun getBrowserType(playwright: Playwright): BrowserType {
        val browserName = readSystemProperty("browserName")

        return when (browserName) {
            "chromium" -> playwright.chromium()
            "webkit" -> playwright.webkit()
            "firefox" -> playwright.firefox()
            else -> throw IllegalArgumentException("Invalid 'browserName' property: $browserName")
        }
    }
}
