package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Couvre les comptes de test SauceDemo dédiés à des comportements
 * particuliers (verrouillage, bugs visuels, lenteur, erreurs serveur).
 */
public class SpecialUsersTests extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "locked_out_user doit être bloqué à la connexion")
    public void testLockedOutUser() {
        loginPage.attemptLogin(ConfigReader.get("user.locked"), password());
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"));
    }

    @Test(groups = {"regression"}, description = "problem_user se connecte mais affiche des anomalies visuelles")
    public void testProblemUser() {
        ProductsPage productsPage = loginPage.loginAs(ConfigReader.get("user.problem"), password());
        Assert.assertTrue(productsPage.getCurrentUrl().contains("inventory.html"));
        // Anomalie connue : problem_user affiche la même image pour tous les produits
        Assert.assertEquals(productsPage.getProductCount(), 6);
    }

    @Test(groups = {"regression"}, description = "performance_glitch_user se connecte avec un délai important")
    public void testPerformanceGlitchUser() {
        long start = System.currentTimeMillis();
        ProductsPage productsPage = loginPage.loginAs(ConfigReader.get("user.performance"), password());
        long elapsed = System.currentTimeMillis() - start;

        Assert.assertTrue(productsPage.getCurrentUrl().contains("inventory.html"));
        Assert.assertTrue(elapsed > 1000, "Le login doit être notablement plus lent qu'un utilisateur standard");
    }

    @Test(groups = {"regression"}, description = "error_user rencontre des erreurs applicatives (ex: tri KO)")
    public void testErrorUser() {
        ProductsPage productsPage = loginPage.loginAs(ConfigReader.get("user.error"), password());
        Assert.assertTrue(productsPage.getCurrentUrl().contains("inventory.html"));
        // Comportement connu : certaines actions (ex: remove du panier) échouent silencieusement
    }

    @Test(groups = {"regression"}, description = "visual_user se connecte avec des différences visuelles mineures")
    public void testVisualUser() {
        ProductsPage productsPage = loginPage.loginAs(ConfigReader.get("user.visual"), password());
        Assert.assertTrue(productsPage.getCurrentUrl().contains("inventory.html"));
        Assert.assertEquals(productsPage.getProductCount(), 6);
    }
}
