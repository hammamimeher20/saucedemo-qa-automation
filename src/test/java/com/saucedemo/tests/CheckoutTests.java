package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.CheckoutStepTwoPage;
import com.saucedemo.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutTests extends BaseTest {

    private CheckoutStepOnePage checkoutStepOnePage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setUp")
    public void addProductAndGoToCheckout() {
        ProductsPage productsPage = loginPage.loginAs(stdUser(), password());
        productsPage.addProductToCartByIndex(0);
        CartPage cartPage = productsPage.goToCart();
        checkoutStepOnePage = cartPage.goToCheckout();
    }

    @Test(groups = {"smoke", "regression"}, description = "Checkout avec des informations valides")
    public void testCheckoutValidInfo() {
        CheckoutStepTwoPage stepTwo = checkoutStepOnePage.fillInfoAndContinue("Meher", "Hammami", "1000");
        Assert.assertTrue(stepTwo.getCurrentUrl().contains("checkout-step-two.html"));
    }

    @Test(groups = {"regression"}, description = "Checkout avec le champ prénom vide")
    public void testCheckoutEmptyFirstName() {
        checkoutStepOnePage.attemptContinue("", "Hammami", "1000");
        Assert.assertTrue(checkoutStepOnePage.isErrorDisplayed());
        Assert.assertTrue(checkoutStepOnePage.getErrorMessage().contains("First Name is required"));
    }

    @Test(groups = {"regression"}, description = "Checkout avec le champ nom vide")
    public void testCheckoutEmptyLastName() {
        checkoutStepOnePage.attemptContinue("Meher", "", "1000");
        Assert.assertTrue(checkoutStepOnePage.isErrorDisplayed());
        Assert.assertTrue(checkoutStepOnePage.getErrorMessage().contains("Last Name is required"));
    }

    @Test(groups = {"regression"}, description = "Checkout avec le code postal vide")
    public void testCheckoutEmptyPostalCode() {
        checkoutStepOnePage.attemptContinue("Meher", "Hammami", "");
        Assert.assertTrue(checkoutStepOnePage.isErrorDisplayed());
        Assert.assertTrue(checkoutStepOnePage.getErrorMessage().contains("Postal Code is required"));
    }

    @Test(groups = {"sanity", "regression"}, description = "Vérifier le récapitulatif de la commande")
    public void testOrderSummary() {
        CheckoutStepTwoPage stepTwo = checkoutStepOnePage.fillInfoAndContinue("Meher", "Hammami", "1000");
        Assert.assertEquals(stepTwo.getSummaryItemNames().size(), 1);
        Assert.assertTrue(stepTwo.getSubtotal().contains("Item total"));
        Assert.assertTrue(stepTwo.getTax().contains("Tax"));
        Assert.assertTrue(stepTwo.getTotal().contains("Total"));
    }

    @Test(groups = {"smoke", "regression"}, description = "Finaliser la commande")
    public void testFinishOrder() {
        CheckoutStepTwoPage stepTwo = checkoutStepOnePage.fillInfoAndContinue("Meher", "Hammami", "1000");
        CheckoutCompletePage completePage = stepTwo.finishOrder();
        Assert.assertTrue(completePage.getCurrentUrl().contains("checkout-complete.html"));
    }

    @Test(groups = {"smoke", "regression"}, description = "Vérifier le message de confirmation Thank you for your order!")
    public void testThankYouMessage() {
        CheckoutStepTwoPage stepTwo = checkoutStepOnePage.fillInfoAndContinue("Meher", "Hammami", "1000");
        CheckoutCompletePage completePage = stepTwo.finishOrder();

        Assert.assertTrue(completePage.isOrderConfirmed());
        Assert.assertEquals(completePage.getConfirmationMessage(), "Thank you for your order!");
    }
}
