package ui;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import common.exceptions.MissingSystemPropertyException;
import common.SystemPropertyReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ui.pageobjects.fragments.NavigationBar;
import ui.pageobjects.fragments.searchresults.VideoResultItem;
import ui.pageobjects.pages.SearchPage;
import ui.pageobjects.pages.SearchResultsPage;
import ui.pageobjects.pages.VideoPlayerPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class VimeoSearchTest extends TestFixtures {
    private static String email;
    private static String password;
    private static final String URL = "https://vimeo.com/search";

    private SearchPage searchPage;

    @BeforeEach
    public void setUp() throws MissingSystemPropertyException {
        email = SystemPropertyReader.readSystemProperty("email");
        password = SystemPropertyReader.readSystemProperty("password");
        page.navigate(URL);
        searchPage = new SearchPage(page);
    }

    @Test
    public void searchResultsPagination() {
        // Perform a search that should have only a few result pages
        SearchResultsPage searchResultsPage = searchPage.searchForTerm("selenium java test automation");

        // Verify results
        assertThat(searchResultsPage.searchResultItems()).hasCount(18);

        // Verify pagination
        assertThat(searchResultsPage.paginationDots()).isHidden();
        assertThat(searchResultsPage.paginationNextButton()).isVisible();
        assertThat(searchResultsPage.paginationPrevButton()).isHidden();

        searchResultsPage.clickNext();
        assertThat(searchResultsPage.paginationNextButton()).isVisible();
        assertThat(searchResultsPage.paginationPrevButton()).isVisible();

        searchResultsPage.clickLastPagination();
        assertThat(searchResultsPage.paginationPrevButton()).isVisible();
        assertThat(searchResultsPage.paginationNextButton()).isHidden();
    }

    @Test
    public void searchResultNavigationToVideo() {
        // Perform a search
        SearchResultsPage searchResultsPage = searchPage.searchForTerm("playwright");

        // Navigate to the first video via thumbnail
        VideoResultItem searchResultItem = searchResultsPage.videoResultItems().get(0);
        String expectedVideoTitle = searchResultItem.title().innerText();
        VideoPlayerPage videoPlayerPage = searchResultItem.clickThumbnail();

        // Verify that navigation was successful
        assertThat(videoPlayerPage.playerWrapper()).isVisible();
        assertThat(videoPlayerPage.videoTitle()).containsText(expectedVideoTitle);
        assertThat(page).hasTitle(expectedVideoTitle + " on Vimeo");

        // Navigate back
        page.goBack();
        assertThat(searchResultItem.thumbnailImage()).isVisible();

        // Navigate to last video on the page via video title
        searchResultItem = searchResultsPage.videoResultItems().get(searchResultsPage.videoResultItems().size() - 1);
        expectedVideoTitle = searchResultItem.title().innerText();
        videoPlayerPage = searchResultItem.clickTitle();

        // Verify that navigation was successful
        assertThat(videoPlayerPage.playerWrapper()).isVisible();
        assertThat(videoPlayerPage.videoTitle()).containsText(expectedVideoTitle);
        assertThat(page).hasTitle(expectedVideoTitle + " on Vimeo");
    }

    @Test
    public void searchResultsForDifferentCategories() {

        // Perform a search for Videos
        SearchResultsPage searchResultsPage = searchPage.searchForTerm("playwright");

        // Verify results
        assertThat(page).hasURL(URL + "?q=playwright");
        assertThat(page).hasTitle("playwright in videos on Vimeo");
        assertThat(searchResultsPage.searchResultItems()).not().hasCount(0);

        // Change to On Demand
        searchResultsPage.clickFilter("On Demand");

        // Verify results
        assertThat(page).hasURL(URL + "/ondemand?q=playwright");
        assertThat(page).hasTitle("playwright in on demand on Vimeo | Vimeo");
        assertThat(searchResultsPage.searchResultItems()).not().hasCount(0);

        // Change to People
        searchResultsPage.clickFilter("People");

        // Verify results
        assertThat(page).hasURL(URL + "/people?q=playwright");
        assertThat(page).hasTitle("playwright in people on Vimeo | Vimeo");
        assertThat(searchResultsPage.searchResultItems()).not().hasCount(0);

        // Change to Channels
        searchResultsPage.clickFilter("Channels");

        // Verify results
        assertThat(page).hasURL(URL + "/channel?q=playwright");
        assertThat(page).hasTitle("playwright in channels on Vimeo | Vimeo");
        assertThat(searchResultsPage.searchResultItems()).not().hasCount(0);

        // Change to Groups
        searchResultsPage.clickFilter("Groups");

        // Verify results
        assertThat(page).hasURL(URL + "/group?q=playwright");
        assertThat(page).hasTitle("playwright in groups on Vimeo | Vimeo");
        assertThat(searchResultsPage.searchResultItems()).not().hasCount(0);
    }

    @Test
    public void searchResultsAsGuestAndLoggedInUser() {

        // Perform a search as a guest
        SearchResultsPage searchResultsPage = searchPage.searchForTerm("playwright");

        // Verify results as a guest
        assertThat(page).hasURL(URL + "?q=playwright");
        assertThat(page).hasTitle("playwright in videos on Vimeo");
        assertThat(searchResultsPage.searchResultItems()).not().hasCount(0);

        // Log in with the test user
        searchResultsPage.getNavigationBar().clickLogin().performLogin(email, password);

        // Perform a search as the test user
        searchResultsPage.getNavigationBar().searchForTerm(NavigationBar.SearchType.Vimeo, "playwright");

        // Verify results as a logged-in user
        assertThat(page).hasURL(URL + "?q=playwright");
        assertThat(page).hasTitle("playwright in videos on Vimeo");
        assertThat(searchResultsPage.searchResultItems()).not().hasCount(0);
    }

    @Test
    public void searchResultsOnMobileView() throws MissingSystemPropertyException {
        // Emulate mobile view
        Browser browser = BrowserFactory.getBrowser();
        BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(375, 667)
                .setDeviceScaleFactor(2)
                .setIsMobile(true));
        page.close();
        page = browserContext.newPage();
        BrowserFactory.getPage(page);

        // Perform a search
        page.navigate(URL);
        searchPage = new SearchPage(page);
        SearchResultsPage searchResultsPage = searchPage.searchForTerm("playwright");

        // Verify search results on mobile view
        assertThat(searchResultsPage.searchResultItems()).not().hasCount(0);
    }
}
