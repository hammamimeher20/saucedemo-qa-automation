package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By errorButton = By.cssSelector(".error-button");
    private final By logo = By.cssSelector(".login_logo");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl);
        return this;
    }

    public ProductsPage loginAs(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        return new ProductsPage(driver);
    }

    /** Utilisé quand on s'attend à rester sur la page de login (cas d'erreur). */
    public LoginPage attemptLogin(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        return this;
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public boolean isErrorCloseButtonDisplayed() {
        return isDisplayed(errorButton);
    }

    public boolean isLogoDisplayed() {
        return isDisplayed(logo);
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed(loginButton);
    }
}
