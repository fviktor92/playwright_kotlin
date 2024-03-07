package ui.pageobjects.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Locator.FilterOptions
import com.microsoft.playwright.Page
import com.microsoft.playwright.Response
import com.microsoft.playwright.options.LoadState
import ui.pageobjects.fragments.searchresults.VideoResultItem
import java.util.regex.Pattern
import java.util.stream.Collectors

class SearchResultsPage(page: Page?) : BasePage(page!!) {
    fun resultsCounter(): Locator {
        return page.locator(".results_count")
    }

    fun videoResultItems(): List<VideoResultItem> {
        val searchResultItems = searchResultItems()
        return searchResultItems.all().stream().map { wrapper: Locator? -> VideoResultItem(this.page, wrapper!!) }.collect(Collectors.toList())
    }

    fun searchResultItems(): Locator {
        return page.locator(".iris_p_infinite__item")
    }

    fun paginationDots(): Locator {
        return paginationWrapper.locator(".iris_pagination__dots")
    }

    fun paginationNextButton(): Locator {
        return paginationWrapper.getByText("Next")
    }

    fun paginationPrevButton(): Locator {
        return paginationWrapper.getByText("Prev")
    }

    fun paginationButton(pageNumber: Int): Locator {
        return paginationNumberButtons().getByText(pageNumber.toString())
    }

    fun paginationNumberButtons(): Locator {
        return paginationWrapper.locator(".iris_pagination__list-item")
                .filter(FilterOptions().setHasNotText(Pattern.compile("Prev|Next")))
    }

    fun clickFilter(label: String?): SearchResultsPage {
        val filterButton = page.locator(".iris_rail-filter__list-item", Page.LocatorOptions().setHasText(label))
        waitForSearchResults { filterButton.click() }
        return this
    }

    fun clickPagination(pageNumber: Int): SearchResultsPage {
        waitForSearchResults { paginationButton(pageNumber).click() }
        return this
    }

    fun clickLastPagination(): SearchResultsPage {
        waitForSearchResults { paginationNumberButtons().last().click() }
        return this
    }

    fun clickNext(): SearchResultsPage {
        waitForSearchResults { paginationNextButton().click() }
        return this
    }

    fun clickPrevious(): SearchResultsPage {
        waitForSearchResults { paginationPrevButton().click() }
        return this
    }

    fun waitForSearchResults(callback: Runnable?) {
        page.waitForResponse({ response: Response -> response.url().contains("/search") }, callback)
        page.waitForLoadState(LoadState.DOMCONTENTLOADED)
    }

    private val paginationWrapper: Locator
        get() = page.locator("#pagination")
}
