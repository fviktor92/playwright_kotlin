package ui.pageobjects.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import ui.pageobjects.fragments.searchresults.VideoResultItem;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchResultsPage extends BasePage {
    public SearchResultsPage(Page page) {
        super(page);
    }

    public Locator resultsCounter() {
        return this.page.locator(".results_count");
    }

    public List<VideoResultItem> videoResultItems() {
        Locator searchResultItems = searchResultItems();
        return searchResultItems.all().stream().map(wrapper -> new VideoResultItem(this.page, wrapper)).collect(Collectors.toList());
    }

    public Locator searchResultItems() {
        return this.page.locator(".iris_p_infinite__item");
    }

    public Locator paginationDots() {
        return this.getPaginationWrapper().locator(".iris_pagination__dots");
    }

    public Locator paginationNextButton() {
        return this.getPaginationWrapper().getByText("Next");
    }

    public Locator paginationPrevButton() {
        return this.getPaginationWrapper().getByText("Prev");
    }

    public Locator paginationButton(int pageNumber) {
        return this.paginationNumberButtons().getByText(String.valueOf(pageNumber));
    }

    public Locator paginationNumberButtons() {
        return this.getPaginationWrapper().locator(".iris_pagination__list-item")
                .filter(new Locator.FilterOptions().setHasNotText(Pattern.compile("Prev|Next")));
    }

    public SearchResultsPage clickFilter(String label) {
        Locator filterButton = this.page.locator(".iris_rail-filter__list-item", new Page.LocatorOptions().setHasText(label));
        waitForSearchResults(() -> filterButton.click());
        return this;
    }

    public SearchResultsPage clickPagination(int pageNumber) {
        waitForSearchResults(() -> this.paginationButton(pageNumber).click());
        return this;
    }

    public SearchResultsPage clickLastPagination() {
        waitForSearchResults(() -> this.paginationNumberButtons().last().click());
        return this;
    }

    public SearchResultsPage clickNext() {
        waitForSearchResults(() -> this.paginationNextButton().click());
        return this;
    }

    public SearchResultsPage clickPrevious() {
        waitForSearchResults(() -> this.paginationPrevButton().click());
        return this;
    }

    void waitForSearchResults(Runnable callback) {
        this.page.waitForResponse(response -> response.url().contains("/search"), callback);
        this.page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    private Locator getPaginationWrapper() {
        return this.page.locator("#pagination");
    }
}
