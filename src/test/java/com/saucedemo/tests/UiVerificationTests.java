package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UiVerificationTests extends BaseTest {

    @Test(groups = {"smoke", "sanity"}, description = "Vérifier le titre de la page de login")
    public void testPageTitle() {
        Assert.assertEquals(loginPage.getPageTitle(), "Swag Labs");
    }

    @Test(groups = {"sanity", "regression"}, description = "Vérifier la présence du logo SauceDemo")
    public void testLogoDisplayed() {
        Assert.assertTrue(loginPage.isLogoDisplayed());
    }

    @Test(groups = {"smoke", "sanity"}, description = "Vérifier l'URL correcte après login")
    public void testCorrectUrlAfterLogin() {
        ProductsPage productsPage = loginPage.loginAs(stdUser(), password());
        Assert.assertEquals(productsPage.getCurrentUrl(), "https://www.saucedemo.com/inventory.html");
    }

    @Test(groups = {"regression"}, description = "Vérifier que les boutons clés sont visibles")
    public void testButtonsVisible() {
        Assert.assertTrue(loginPage.isLoginButtonDisplayed());
        ProductsPage productsPage = loginPage.loginAs(stdUser(), password());
        productsPage.addProductToCartByIndex(0);
        Assert.assertTrue(productsPage.isCartBadgeDisplayed());
    }

    @Test(groups = {"regression"}, description = "Vérifier les messages d'erreur et leur bouton de fermeture")
    public void testErrorMessageAndCloseButton() {
        loginPage.attemptLogin("bad_user", "bad_pass");
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.isErrorCloseButtonDisplayed());
    }
}
