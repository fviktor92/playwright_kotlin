package ui;

import com.microsoft.playwright.*;
import common.exceptions.MissingSystemPropertyException;
import common.SystemPropertyReader;

public class BrowserFactory {
    private static final ThreadLocal<Playwright> playwrightThread = new ThreadLocal<>();
    private static final ThreadLocal<BrowserType> browserTypeThread = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThread = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> browserContextThread = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThread = new ThreadLocal<>();

    public static synchronized Page getPage(Page page) throws MissingSystemPropertyException {
        if (page != null) {
            pageThread.set(page);
        } else if (playwrightThread.get() == null && page == null) {
            Playwright playwright = Playwright.create();
            playwrightThread.set(playwright);
            page = createPage(playwright);
            pageThread.set(page);
        }
        return pageThread.get();
    }

    public static synchronized Browser getBrowser() {
        // FIXME
        return browserThread.get();
    }

    public static synchronized void closePage() {
        Page page = pageThread.get();
        if (page != null) {
            page.close();
            pageThread.remove();
        }
    }

    public static synchronized void closePlaywright() {
        Playwright playwright = playwrightThread.get();
        if (playwright != null) {
            playwright.close();
            playwrightThread.remove();
        }
    }

    public static synchronized void closeBrowserContext() {
        BrowserContext browserContext = browserContextThread.get();
        if (browserContext != null) {
            browserContext.close();
            browserContextThread.remove();
        }
    }

    public static synchronized void closeBrowser() {
        Browser browser = browserThread.get();
        if (browser != null) {
            browser.close();
            browserThread.remove();
        }
    }

    private static synchronized Page createPage(Playwright playwright) throws MissingSystemPropertyException {
        BrowserType browserType = getBrowserType(playwright);
        Browser browser = browserType.launch();
        BrowserContext context = browser.newContext();;

        browserTypeThread.set(browserType);
        browserThread.set(browser);
        browserContextThread.set(context);
        return context.newPage();
    }

    private static synchronized BrowserType getBrowserType(Playwright playwright) throws MissingSystemPropertyException {
        String browserName = SystemPropertyReader.readSystemProperty("browserName");

        switch (browserName) {
            case "chromium":
                return playwright.chromium();
            case "webkit":
                return playwright.webkit();
            case "firefox":
                return playwright.firefox();
            default:
                throw new IllegalArgumentException("Invalid 'browserName' property: " + browserName);
        }
    }
}
