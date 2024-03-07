package ui

import com.microsoft.playwright.Browser
import com.microsoft.playwright.assertions.PlaywrightAssertions
import common.SystemPropertyReader.readSystemProperty
import common.exceptions.MissingSystemPropertyException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import ui.BrowserFactory.browser
import ui.BrowserFactory.getPage
import ui.pageobjects.fragments.NavigationBar
import ui.pageobjects.pages.SearchPage

@Execution(ExecutionMode.CONCURRENT)
internal class VimeoSearchTest : TestFixtures() {
    private var searchPage: SearchPage? = null

    @BeforeEach
    @Throws(MissingSystemPropertyException::class)
    fun setUp() {
        email = readSystemProperty("email")
        password = readSystemProperty("password")
        page!!.navigate(URL)
        searchPage = SearchPage(page)
    }

    @Test
    fun searchResultsPagination() {
        // Perform a search that should have only a few result pages
        val searchResultsPage = searchPage!!.searchForTerm("selenium java test automation")

        // Verify results
        PlaywrightAssertions.assertThat(searchResultsPage.searchResultItems()).hasCount(18)

        // Verify pagination
        PlaywrightAssertions.assertThat(searchResultsPage.paginationDots()).isHidden()
        PlaywrightAssertions.assertThat(searchResultsPage.paginationNextButton()).isVisible()
        PlaywrightAssertions.assertThat(searchResultsPage.paginationPrevButton()).isHidden()

        searchResultsPage.clickNext()
        PlaywrightAssertions.assertThat(searchResultsPage.paginationNextButton()).isVisible()
        PlaywrightAssertions.assertThat(searchResultsPage.paginationPrevButton()).isVisible()

        searchResultsPage.clickLastPagination()
        PlaywrightAssertions.assertThat(searchResultsPage.paginationPrevButton()).isVisible()
        PlaywrightAssertions.assertThat(searchResultsPage.paginationNextButton()).isHidden()
    }

    @Test
    fun searchResultNavigationToVideo() {
        // Perform a search
        val searchResultsPage = searchPage!!.searchForTerm("playwright")

        // Navigate to the first video via thumbnail
        var searchResultItem = searchResultsPage.videoResultItems()[0]
        var expectedVideoTitle = searchResultItem.title().innerText()
        var videoPlayerPage = searchResultItem.clickThumbnail()

        // Verify that navigation was successful
        PlaywrightAssertions.assertThat(videoPlayerPage.playerWrapper()).isVisible()
        PlaywrightAssertions.assertThat(videoPlayerPage.videoTitle()).containsText(expectedVideoTitle)
        PlaywrightAssertions.assertThat(page).hasTitle("$expectedVideoTitle on Vimeo")

        // Navigate back
        page!!.goBack()
        PlaywrightAssertions.assertThat(searchResultItem.thumbnailImage()).isVisible()

        // Navigate to last video on the page via video title
        searchResultItem = searchResultsPage.videoResultItems()[searchResultsPage.videoResultItems().size - 1]
        expectedVideoTitle = searchResultItem.title().innerText()
        videoPlayerPage = searchResultItem.clickTitle()

        // Verify that navigation was successful
        PlaywrightAssertions.assertThat(videoPlayerPage.playerWrapper()).isVisible()
        PlaywrightAssertions.assertThat(videoPlayerPage.videoTitle()).containsText(expectedVideoTitle)
        PlaywrightAssertions.assertThat(page).hasTitle("$expectedVideoTitle on Vimeo")
    }

    @Test
    fun searchResultsForDifferentCategories() {
        // Perform a search for Videos

        val searchResultsPage = searchPage!!.searchForTerm("playwright")

        // Verify results
        PlaywrightAssertions.assertThat(page).hasURL(URL + "?q=playwright")
        PlaywrightAssertions.assertThat(page).hasTitle("playwright in videos on Vimeo")
        PlaywrightAssertions.assertThat(searchResultsPage.searchResultItems()).not().hasCount(0)

        // Change to On Demand
        searchResultsPage.clickFilter("On Demand")

        // Verify results
        PlaywrightAssertions.assertThat(page).hasURL(URL + "/ondemand?q=playwright")
        PlaywrightAssertions.assertThat(page).hasTitle("playwright in on demand on Vimeo | Vimeo")
        PlaywrightAssertions.assertThat(searchResultsPage.searchResultItems()).not().hasCount(0)

        // Change to People
        searchResultsPage.clickFilter("People")

        // Verify results
        PlaywrightAssertions.assertThat(page).hasURL(URL + "/people?q=playwright")
        PlaywrightAssertions.assertThat(page).hasTitle("playwright in people on Vimeo | Vimeo")
        PlaywrightAssertions.assertThat(searchResultsPage.searchResultItems()).not().hasCount(0)

        // Change to Channels
        searchResultsPage.clickFilter("Channels")

        // Verify results
        PlaywrightAssertions.assertThat(page).hasURL(URL + "/channel?q=playwright")
        PlaywrightAssertions.assertThat(page).hasTitle("playwright in channels on Vimeo | Vimeo")
        PlaywrightAssertions.assertThat(searchResultsPage.searchResultItems()).not().hasCount(0)

        // Change to Groups
        searchResultsPage.clickFilter("Groups")

        // Verify results
        PlaywrightAssertions.assertThat(page).hasURL(URL + "/group?q=playwright")
        PlaywrightAssertions.assertThat(page).hasTitle("playwright in groups on Vimeo | Vimeo")
        PlaywrightAssertions.assertThat(searchResultsPage.searchResultItems()).not().hasCount(0)
    }

    @Test
    fun searchResultsAsGuestAndLoggedInUser() {
        // Perform a search as a guest

        val searchResultsPage = searchPage!!.searchForTerm("playwright")

        // Verify results as a guest
        PlaywrightAssertions.assertThat(page).hasURL(URL + "?q=playwright")
        PlaywrightAssertions.assertThat(page).hasTitle("playwright in videos on Vimeo")
        PlaywrightAssertions.assertThat(searchResultsPage.searchResultItems()).not().hasCount(0)

        // Log in with the test user
        searchResultsPage.navigationBar.clickLogin().performLogin(email, password)

        // Perform a search as the test user
        searchResultsPage.navigationBar.searchForTerm(NavigationBar.SearchType.Vimeo, "playwright")

        // Verify results as a logged-in user
        PlaywrightAssertions.assertThat(page).hasURL(URL + "?q=playwright")
        PlaywrightAssertions.assertThat(page).hasTitle("playwright in videos on Vimeo")
        PlaywrightAssertions.assertThat(searchResultsPage.searchResultItems()).not().hasCount(0)
    }

    @Test
    @Throws(MissingSystemPropertyException::class)
    fun searchResultsOnMobileView() {
        // Emulate mobile view
        val browser = browser
        val browserContext = browser.newContext(Browser.NewContextOptions()
                .setViewportSize(375, 667)
                .setDeviceScaleFactor(2.0)
                .setIsMobile(true))
        page!!.close()
        page = browserContext.newPage()
        page = getPage(page)

        // Perform a search
        page?.navigate(URL)
        searchPage = SearchPage(page)
        val searchResultsPage = searchPage!!.searchForTerm("playwright")

        // Verify search results on mobile view
        PlaywrightAssertions.assertThat(searchResultsPage.searchResultItems()).not().hasCount(0)
    }

    companion object {
        private var email: String? = null
        private var password: String? = null
        private const val URL = "https://vimeo.com/search"
    }
}
