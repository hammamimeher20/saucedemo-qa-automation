package com.saucedemo.bdd.context;

import com.saucedemo.pages.*;
import org.openqa.selenium.WebDriver;

/**
 * État partagé entre les step definitions d'un même scénario Cucumber.
 * Injecté par constructeur dans chaque classe de steps via cucumber-picocontainer
 * (une instance de TestContext par scénario, recréée automatiquement).
 */
public class TestContext {

    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutStepOnePage checkoutStepOnePage;
    private CheckoutStepTwoPage checkoutStepTwoPage;
    private CheckoutCompletePage checkoutCompletePage;

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public ProductsPage getProductsPage() {
        return productsPage;
    }

    public void setProductsPage(ProductsPage productsPage) {
        this.productsPage = productsPage;
    }

    public CartPage getCartPage() {
        return cartPage;
    }

    public void setCartPage(CartPage cartPage) {
        this.cartPage = cartPage;
    }

    public CheckoutStepOnePage getCheckoutStepOnePage() {
        return checkoutStepOnePage;
    }

    public void setCheckoutStepOnePage(CheckoutStepOnePage checkoutStepOnePage) {
        this.checkoutStepOnePage = checkoutStepOnePage;
    }

    public CheckoutStepTwoPage getCheckoutStepTwoPage() {
        return checkoutStepTwoPage;
    }

    public void setCheckoutStepTwoPage(CheckoutStepTwoPage checkoutStepTwoPage) {
        this.checkoutStepTwoPage = checkoutStepTwoPage;
    }

    public CheckoutCompletePage getCheckoutCompletePage() {
        return checkoutCompletePage;
    }

    public void setCheckoutCompletePage(CheckoutCompletePage checkoutCompletePage) {
        this.checkoutCompletePage = checkoutCompletePage;
    }
}
