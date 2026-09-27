package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CartTests extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setUp")
    public void loginBeforeEachTest() {
        productsPage = loginPage.loginAs(stdUser(), password());
    }

    @Test(groups = {"smoke", "regression"}, description = "Ajouter un produit au panier")
    public void testAddOneProduct() {
        productsPage.addProductToCartByIndex(0);
        Assert.assertEquals(productsPage.getCartBadgeCount(), "1");
    }

    @Test(groups = {"sanity", "regression"}, description = "Ajouter plusieurs produits au panier")
    public void testAddMultipleProducts() {
        productsPage.addProductToCartByIndex(0);
        productsPage.addProductToCartByIndex(1);
        productsPage.addProductToCartByIndex(2);
        Assert.assertEquals(productsPage.getCartBadgeCount(), "3");
    }

    @Test(groups = {"sanity", "regression"}, description = "Supprimer un produit du panier")
    public void testRemoveProduct() {
        productsPage.addProductToCartByIndex(0);
        productsPage.addProductToCartByIndex(1);
        Assert.assertEquals(productsPage.getCartBadgeCount(), "2");

        productsPage.removeProductByIndex(0);
        Assert.assertEquals(productsPage.getCartBadgeCount(), "1");
    }

    @Test(groups = {"smoke", "regression"}, description = "Vérifier le badge du panier")
    public void testCartBadgeUpdates() {
        Assert.assertFalse(productsPage.isCartBadgeDisplayed(), "Le badge ne doit pas apparaître si le panier est vide");
        productsPage.addProductToCartByIndex(0);
        Assert.assertTrue(productsPage.isCartBadgeDisplayed());
        Assert.assertEquals(productsPage.getCartBadgeCount(), "1");
    }

    @Test(groups = {"regression"}, description = "Vérifier le contenu détaillé du panier")
    public void testCartContent() {
        String firstProductName = productsPage.getProductNames().get(0);
        productsPage.addProductToCartByIndex(0);

        CartPage cartPage = productsPage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 1);
        Assert.assertTrue(cartPage.isProductInCart(firstProductName));
    }
}
