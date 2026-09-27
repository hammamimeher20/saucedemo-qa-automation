package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class ProductsPage extends BasePage {

    private final By pageTitle = By.cssSelector(".title");
    private final By inventoryItems = By.cssSelector(".inventory_item");
    private final By itemNames = By.cssSelector(".inventory_item_name");
    private final By itemPrices = By.cssSelector(".inventory_item_price");
    private final By itemImages = By.cssSelector(".inventory_item_img img");
    private final By sortDropdown = By.cssSelector(".product_sort_container");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");
    private final By cartLink = By.cssSelector(".shopping_cart_link");
    private final By hamburgerMenuBtn = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By aboutLink = By.id("about_sidebar_link");
    private final By resetLink = By.id("reset_sidebar_link");
    private final By closeMenuBtn = By.id("react-burger-cross-btn");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitleText() {
        return getText(pageTitle);
    }

    public int getProductCount() {
        return waitVisibleAll(inventoryItems).size();
    }

    public List<String> getProductNames() {
        return waitVisibleAll(itemNames).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public List<Double> getProductPrices() {
        return waitVisibleAll(itemPrices).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    public int getProductImageCount() {
        return waitVisibleAll(itemImages).size();
    }

    public boolean allImagesHaveValidSrc() {
        return waitVisibleAll(itemImages).stream()
                .allMatch(img -> {
                    String src = img.getAttribute("src");
                    return src != null && !src.isBlank();
                });
    }

    public ProductDetailPage openProductByName(String name) {
        driver.findElement(By.xpath("//div[@class='inventory_item_name' and text()='" + name + "']")).click();
        return new ProductDetailPage(driver);
    }

    public ProductsPage sortBy(String visibleOptionText) {
        Select select = new Select(waitVisible(sortDropdown));
        select.selectByVisibleText(visibleOptionText);
        return this;
    }

    public ProductsPage addProductToCartByIndex(int index) {
        // Cible le bouton DANS la carte produit d'index fixe (.inventory_item),
        // au lieu de re-filtrer globalement sur "add-to-cart" qui rétrécit à chaque clic.
        List<WebElement> items = driver.findElements(inventoryItems);
        items.get(index).findElement(By.cssSelector("button")).click();
        return this;
    }

    public ProductsPage removeProductByIndex(int index) {
        List<WebElement> items = driver.findElements(inventoryItems);
        items.get(index).findElement(By.cssSelector("button")).click();
        return this;
    }

    public String getCartBadgeCount() {
        return isDisplayed(cartBadge) ? getText(cartBadge) : "0";
    }

    public boolean isCartBadgeDisplayed() {
        return isDisplayed(cartBadge);
    }

    public CartPage goToCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    public ProductsPage openHamburgerMenu() {
        click(hamburgerMenuBtn);
        waitVisible(logoutLink);
        return this;
    }

    public void closeHamburgerMenu() {
        click(closeMenuBtn);
    }

    public boolean isHamburgerMenuOpen() {
        return isDisplayed(logoutLink);
    }

    public LoginPage logout() {
        openHamburgerMenu();
        click(logoutLink);
        waitVisible(By.id("login-button")); // attend que la page de login soit réellement chargée
        return new LoginPage(driver);
    }

    public void clickAbout() {
        openHamburgerMenu();
        click(aboutLink);
    }

    public ProductsPage resetAppState() {
        openHamburgerMenu();
        click(resetLink);
        closeMenuBtn();
        return this;
    }

    private void closeMenuBtn() {
        if (isDisplayed(closeMenuBtn)) {
            click(closeMenuBtn);
        }
    }
}
