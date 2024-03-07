package ui.pageobjects.fragments

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import ui.pageobjects.fragments.modals.LoginModal
import ui.pageobjects.pages.SearchResultsPage

class NavigationBar(private val page: Page) {
    fun loginButton(): Locator {
        return page.locator("[data-menu-id='login']")
    }

    fun searchTypeSelect(): Locator {
        return page.locator("#select-container select")
    }

    fun searchInput(): Locator {
        return page.locator("#topnav-search")
    }

    fun searchButton(): Locator {
        return page.locator(".search-ui-test_searchbtn")
    }

    fun searchForTerm(searchType: SearchType, term: String?): SearchResultsPage {
        searchTypeSelect().selectOption(searchType.name)
        searchInput().type(term)
        page.keyboard().press("Enter")
        return SearchResultsPage(this.page)
    }

    fun clickLogin(): LoginModal {
        loginButton().click()
        return LoginModal(this.page)
    }

    enum class SearchType {
        MyLibrary, Vimeo
    }
}
