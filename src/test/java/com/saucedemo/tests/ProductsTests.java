package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.ProductDetailPage;
import com.saucedemo.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class ProductsTests extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setUp")
    public void loginBeforeEachTest() {
        productsPage = loginPage.loginAs(stdUser(), password());
    }

    @Test(groups = {"smoke", "sanity"}, description = "Vérifier que les 6 produits sont affichés")
    public void testSixProductsDisplayed() {
        Assert.assertEquals(productsPage.getProductCount(), 6);
    }

    @Test(groups = {"regression"}, description = "Vérifier le nom et le prix des produits")
    public void testProductNamesAndPrices() {
        List<String> names = productsPage.getProductNames();
        List<Double> prices = productsPage.getProductPrices();

        Assert.assertEquals(names.size(), 6);
        Assert.assertEquals(prices.size(), 6);
        prices.forEach(price -> Assert.assertTrue(price > 0, "Le prix doit être positif"));
        names.forEach(name -> Assert.assertFalse(name.isBlank(), "Le nom ne doit pas être vide"));
    }

    @Test(groups = {"regression"}, description = "Vérifier les images des produits")
    public void testProductImages() {
        Assert.assertEquals(productsPage.getProductImageCount(), 6);
        Assert.assertTrue(productsPage.allImagesHaveValidSrc(), "Chaque image doit avoir un attribut src valide");
    }

    @Test(groups = {"regression"}, description = "Ouvrir la page de détail d'un produit")
    public void testOpenProductDetail() {
        String productName = productsPage.getProductNames().get(0);
        ProductDetailPage detailPage = productsPage.openProductByName(productName);

        Assert.assertTrue(detailPage.isDetailPageDisplayed());
        Assert.assertEquals(detailPage.getProductName(), productName);
    }

    @Test(groups = {"sanity", "regression"}, description = "Retour à la liste des produits depuis le détail")
    public void testBackToProductsList() {
        String productName = productsPage.getProductNames().get(0);
        ProductDetailPage detailPage = productsPage.openProductByName(productName);
        ProductsPage backToList = detailPage.backToProducts();

        Assert.assertEquals(backToList.getProductCount(), 6);
        Assert.assertTrue(backToList.getCurrentUrl().contains("inventory.html"));
    }
}
