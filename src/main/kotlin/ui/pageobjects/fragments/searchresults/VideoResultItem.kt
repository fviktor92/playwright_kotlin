package ui.pageobjects.fragments.searchresults

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import ui.pageobjects.pages.VideoPlayerPage

class VideoResultItem(private val page: Page, private val wrapper: Locator) {
    fun thumbnailImage(): Locator {
        return wrapper.locator(".iris_thumbnail img")
    }

    fun title(): Locator {
        return wrapper.locator(".iris_video-vital__title")
    }

    fun userInfo(): Locator {
        return wrapper.locator(".iris_userinfo")
    }

    fun clickThumbnail(): VideoPlayerPage {
        thumbnailImage().click()
        return VideoPlayerPage(this.page)
    }

    fun clickTitle(): VideoPlayerPage {
        title().click()
        return VideoPlayerPage(this.page)
    }
}
