package ui.pageobjects.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class VideoPlayerPage extends BasePage {

    public VideoPlayerPage(Page page) {
        super(page);
    }

    public Locator playerWrapper() {
        return this.page.locator(".player_area");
    }

    public Locator videoTitle() {
        return this.page.locator("main h1");
    }
}
