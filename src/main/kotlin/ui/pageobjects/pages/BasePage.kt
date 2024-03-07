package ui.pageobjects.pages

import com.microsoft.playwright.Page
import ui.pageobjects.fragments.NavigationBar

abstract class BasePage(protected val page: Page) {
    val navigationBar: NavigationBar = NavigationBar(this.page)
}
