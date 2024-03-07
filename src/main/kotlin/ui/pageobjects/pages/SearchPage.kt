package ui.pageobjects.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page


class SearchPage(page: Page?) : BasePage(page!!) {
    fun searchBar(): Locator {
        return page.locator("#search input[type='text']")
    }

    fun findButton(): Locator {
        return page.locator("#search input[type='submit']")
    }

    fun searchForTerm(term: String?): SearchResultsPage {
        searchBar().type(term)
        val searchResultsPage = SearchResultsPage(this.page)
        searchResultsPage.waitForSearchResults { findButton().click() }
        return searchResultsPage
    }
}
