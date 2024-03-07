package ui.pageobjects.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class VideoPlayerPage(page: Page?) : BasePage(page!!) {
    fun playerWrapper(): Locator {
        return page.locator(".player_area")
    }

    fun videoTitle(): Locator {
        return page.locator("main h1")
    }
}
