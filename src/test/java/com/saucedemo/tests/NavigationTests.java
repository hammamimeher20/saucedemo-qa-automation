package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.ProductsPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Set;

public class NavigationTests extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setUp")
    public void loginBeforeEachTest() {
        productsPage = loginPage.loginAs(stdUser(), password());
    }

    @Test(groups = {"sanity", "regression"}, description = "Ouvrir et fermer le menu hamburger")
    public void testHamburgerMenu() {
        productsPage.openHamburgerMenu();
        Assert.assertTrue(productsPage.isHamburgerMenuOpen());
        productsPage.closeHamburgerMenu();
    }

    @Test(groups = {"regression"}, description = "Naviguer vers About depuis le menu")
    public void testAboutLink() {
        String originalWindow = driver.getWindowHandle();
        productsPage.clickAbout();

        // About redirige en dehors de saucedemo.com (saucelabs.com)
        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        localWait.until(d -> driver.getWindowHandles().size() > 1 || !driver.getCurrentUrl().contains("saucedemo.com"));

        Set<String> windows = driver.getWindowHandles();
        if (windows.size() > 1) {
            windows.stream().filter(w -> !w.equals(originalWindow)).findFirst()
                    .ifPresent(handle -> driver.switchTo().window(handle));
            Assert.assertTrue(driver.getCurrentUrl().contains("saucelabs.com"));
        } else {
            Assert.assertTrue(driver.getCurrentUrl().contains("saucelabs.com"));
        }
    }

    @Test(groups = {"sanity", "regression"}, description = "Reset App State via le menu")
    public void testResetAppState() {
        productsPage.addProductToCartByIndex(0);
        productsPage.addProductToCartByIndex(1);
        Assert.assertEquals(productsPage.getCartBadgeCount(), "2");

        productsPage.resetAppState();
        Assert.assertFalse(productsPage.isCartBadgeDisplayed(), "Le panier doit être vidé après Reset App State");
    }

    @Test(groups = {"smoke", "regression"}, description = "Logout depuis la page produits")
    public void testLogoutFromMenu() {
        loginPage = productsPage.logout();
        Assert.assertTrue(loginPage.isLoginButtonDisplayed());
    }
}
