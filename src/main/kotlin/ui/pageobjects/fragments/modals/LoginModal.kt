package ui.pageobjects.fragments.modals

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class LoginModal(private val page: Page) {
    fun title(): Locator {
        return page.locator("#dialog-title")
    }

    fun emailInput(): Locator {
        return page.locator("#signup_email")
    }

    fun passwordInput(): Locator {
        return page.locator("#login_password")
    }

    fun loginButton(): Locator {
        return page.locator("input[type='submit']")
    }

    fun performLogin(email: String?, password: String?) {
        emailInput().type(email)
        passwordInput().type(password)
        loginButton().click()
    }
}
