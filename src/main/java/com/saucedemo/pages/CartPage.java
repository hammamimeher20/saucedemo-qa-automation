package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private final By cartItems = By.cssSelector(".cart_item");
    private final By itemNames = By.cssSelector(".inventory_item_name");
    private final By itemPrices = By.cssSelector(".inventory_item_price");
    private final By itemQuantities = By.cssSelector(".cart_quantity");
    private final By removeButtons = By.cssSelector("button[id^='remove']");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public List<String> getCartItemNames() {
        return waitVisibleAll(itemNames).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public List<String> getCartItemPrices() {
        return waitVisibleAll(itemPrices).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public boolean isProductInCart(String name) {
        return getCartItemNames().contains(name);
    }

    public CartPage removeItemByIndex(int index) {
        driver.findElements(removeButtons).get(index).click();
        return this;
    }

    public ProductsPage continueShopping() {
        click(continueShoppingButton);
        return new ProductsPage(driver);
    }

    public CheckoutStepOnePage goToCheckout() {
        click(checkoutButton);
        return new CheckoutStepOnePage(driver);
    }
}
