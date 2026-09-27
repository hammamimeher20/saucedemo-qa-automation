package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutStepOnePage extends BasePage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
    }

    public CheckoutStepTwoPage fillInfoAndContinue(String firstName, String lastName, String postalCode) {
        if (!firstName.isEmpty()) type(firstNameInput, firstName);
        if (!lastName.isEmpty()) type(lastNameInput, lastName);
        if (!postalCode.isEmpty()) type(postalCodeInput, postalCode);
        click(continueButton);
        return new CheckoutStepTwoPage(driver);
    }

    /** Utilisé pour les cas d'erreur où le flux reste bloqué sur l'étape 1. */
    public CheckoutStepOnePage attemptContinue(String firstName, String lastName, String postalCode) {
        if (!firstName.isEmpty()) type(firstNameInput, firstName);
        if (!lastName.isEmpty()) type(lastNameInput, lastName);
        if (!postalCode.isEmpty()) type(postalCodeInput, postalCode);
        click(continueButton);
        return this;
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }
}
