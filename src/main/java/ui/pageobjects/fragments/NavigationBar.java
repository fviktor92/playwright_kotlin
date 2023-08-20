package ui.pageobjects.fragments;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import ui.pageobjects.fragments.modals.LoginModal;
import ui.pageobjects.pages.SearchResultsPage;

public class NavigationBar {
    private final Page page;

    public NavigationBar(Page page) {
        this.page = page;
    }

    public Locator loginButton() {
        return this.page.locator("[data-menu-id='login']");
    }

    public Locator searchTypeSelect() {
        return this.page.locator("#select-container select");
    }
    public Locator searchInput() {
        return this.page.locator("#topnav-search");
    }

    public Locator searchButton() {
        return this.page.locator(".search-ui-test_searchbtn");
    }

    public SearchResultsPage searchForTerm(SearchType searchType, String term) {
        this.searchTypeSelect().selectOption(searchType.name());
        this.searchInput().type(term);
        this.page.keyboard().press("Enter");
        return new SearchResultsPage(this.page);
    }

    public LoginModal clickLogin() {
        this.loginButton().click();
        return new LoginModal(this.page);
    }

    public enum SearchType {
        MyLibrary, Vimeo
    }
}
