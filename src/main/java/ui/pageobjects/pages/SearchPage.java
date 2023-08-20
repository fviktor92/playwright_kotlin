package ui.pageobjects.pages;


import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class SearchPage extends BasePage {
    public SearchPage(Page page) {
        super(page);
    }

    public Locator searchBar() {
        return this.page.locator("#search input[type='text']");
    }

    public Locator findButton() {
        return this.page.locator("#search input[type='submit']");
    }

    public SearchResultsPage searchForTerm(String term) {
        this.searchBar().type(term);
        SearchResultsPage searchResultsPage = new SearchResultsPage(this.page);
        searchResultsPage.waitForSearchResults(() -> findButton().click());
        return searchResultsPage;
    }
}
