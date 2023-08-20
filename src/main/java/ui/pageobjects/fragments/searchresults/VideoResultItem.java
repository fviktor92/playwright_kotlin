package ui.pageobjects.fragments.searchresults;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import ui.pageobjects.pages.VideoPlayerPage;

public class VideoResultItem {
    private final Page page;
    private final Locator wrapper;

    public VideoResultItem(Page page, Locator wrapper) {
        this.page = page;
        this.wrapper = wrapper;
    }

    public Locator thumbnailImage() {
        return this.getWrapper().locator(".iris_thumbnail img");
    }

    public Locator title() {
        return this.getWrapper().locator(".iris_video-vital__title");
    }

    public Locator userInfo() {
        return this.getWrapper().locator(".iris_userinfo");
    }

    public Locator getWrapper() {
        return this.wrapper;
    }

    public VideoPlayerPage clickThumbnail() {
        this.thumbnailImage().click();
        return new VideoPlayerPage(this.page);
    }

    public VideoPlayerPage clickTitle() {
        this.title().click();
        return new VideoPlayerPage(this.page);
    }
}
