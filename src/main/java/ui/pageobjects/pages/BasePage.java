package ui.pageobjects.pages;

import com.microsoft.playwright.Page;
import ui.pageobjects.fragments.NavigationBar;

public abstract class BasePage {
    protected final Page page;
    private NavigationBar navigationBar;

    public BasePage(Page page) {
        this.page = page;
        this.navigationBar = new NavigationBar(this.page);
    }

    public NavigationBar getNavigationBar() {
        return this.navigationBar;
    }
}
