package ui.pageobjects.fragments.modals;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginModal {
    private final Page page;

    public LoginModal(Page page) {
        this.page = page;
    }

    public Locator title() {
        return this.page.locator("#dialog-title");
    }

    public Locator emailInput() {
        return this.page.locator("#signup_email");
    }

    public Locator passwordInput() {
        return this.page.locator("#login_password");
    }

    public Locator loginButton() {
        return this.page.locator("input[type='submit']");
    }

    public void performLogin(String email, String password) {
        this.emailInput().type(email);
        this.passwordInput().type(password);
        this.loginButton().click();
    }
}
