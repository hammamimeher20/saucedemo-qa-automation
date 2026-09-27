package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Comparator;
import java.util.List;

public class SortTests extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setUp")
    public void loginBeforeEachTest() {
        productsPage = loginPage.loginAs(stdUser(), password());
    }

    @Test(groups = {"sanity", "regression"}, description = "Tri des produits A -> Z")
    public void testSortAZ() {
        List<String> names = productsPage.sortBy("Name (A to Z)").getProductNames();
        List<String> expected = names.stream().sorted().toList();
        Assert.assertEquals(names, expected);
    }

    @Test(groups = {"regression"}, description = "Tri des produits Z -> A")
    public void testSortZA() {
        List<String> names = productsPage.sortBy("Name (Z to A)").getProductNames();
        List<String> expected = names.stream().sorted(Comparator.reverseOrder()).toList();
        Assert.assertEquals(names, expected);
    }

    @Test(groups = {"regression"}, description = "Tri par prix croissant")
    public void testSortPriceLowToHigh() {
        List<Double> prices = productsPage.sortBy("Price (low to high)").getProductPrices();
        List<Double> expected = prices.stream().sorted().toList();
        Assert.assertEquals(prices, expected);
    }

    @Test(groups = {"regression"}, description = "Tri par prix décroissant")
    public void testSortPriceHighToLow() {
        List<Double> prices = productsPage.sortBy("Price (high to low)").getProductPrices();
        List<Double> expected = prices.stream().sorted(Comparator.reverseOrder()).toList();
        Assert.assertEquals(prices, expected);
    }
}
