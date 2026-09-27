package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private final By completeHeader = By.cssSelector(".complete-header");
    private final By backHomeButton = By.id("back-to-products");
    private final By ponyExpressImage = By.cssSelector(".pony_express");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public String getConfirmationMessage() {
        return getText(completeHeader);
    }

    public boolean isOrderConfirmed() {
        return isDisplayed(completeHeader) && isDisplayed(ponyExpressImage);
    }

    public ProductsPage backToProducts() {
        click(backHomeButton);
        return new ProductsPage(driver);
    }
}
