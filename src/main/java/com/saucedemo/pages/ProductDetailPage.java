package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailPage extends BasePage {

    private final By productName = By.cssSelector(".inventory_details_name");
    private final By productPrice = By.cssSelector(".inventory_details_price");
    private final By backButton = By.id("back-to-products");
    private final By addToCartButton = By.cssSelector("button[id^='add-to-cart']");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        return getText(productName);
    }

    public String getProductPrice() {
        return getText(productPrice);
    }

    public boolean isDetailPageDisplayed() {
        return isDisplayed(productName) && isDisplayed(productPrice);
    }

    public ProductDetailPage addToCart() {
        click(addToCartButton);
        return this;
    }

    public ProductsPage backToProducts() {
        click(backButton);
        return new ProductsPage(driver);
    }
}
