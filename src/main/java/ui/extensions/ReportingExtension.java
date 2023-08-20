package ui.extensions;

import com.microsoft.playwright.Page;
import common.exceptions.MissingSystemPropertyException;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import ui.BrowserFactory;

import java.io.ByteArrayInputStream;

public class ReportingExtension implements AfterEachCallback {
    @Override
    public void afterEach(ExtensionContext extensionContext) throws MissingSystemPropertyException {
        byte[] screenshot = BrowserFactory.getPage(null).screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(screenshot));
    }
}
