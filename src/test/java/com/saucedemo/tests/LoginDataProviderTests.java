package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Démontre l'approche Data-Driven Testing avec TestNG :
 * les mêmes assertions sont rejouées pour plusieurs jeux de données,
 * au lieu de dupliquer une méthode de test par cas.
 */
public class LoginDataProviderTests extends BaseTest {

    @DataProvider(name = "invalidLoginCombinations")
    public Object[][] invalidLoginCombinations() {
        return new Object[][]{
                {"standard_user", "wrong_password", "Username and password do not match"},
                {"ghost_user", "secret_sauce", "Username and password do not match"},
                {"", "", "Username is required"},
                {"standard_user", "", "Password is required"},
                {"", "secret_sauce", "Username is required"},
                {"locked_out_user", "secret_sauce", "locked out"},
        };
    }

    @Test(dataProvider = "invalidLoginCombinations", groups = {"regression"},
            description = "Login invalide piloté par données (username, password, message attendu)")
    public void testInvalidLoginCombinations(String username, String password, String expectedErrorFragment) {
        loginPage.attemptLogin(username, password);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Un message d'erreur est attendu pour : " + username + " / " + password);
        Assert.assertTrue(loginPage.getErrorMessage().contains(expectedErrorFragment),
                "Message attendu contenant '" + expectedErrorFragment + "' mais reçu : " + loginPage.getErrorMessage());
    }

    @DataProvider(name = "validUsers")
    public Object[][] validUsers() {
        return new Object[][]{
                {"standard_user"},
                {"problem_user"},
                {"visual_user"},
        };
    }

    @Test(dataProvider = "validUsers", groups = {"regression"},
            description = "Login réussi piloté par données pour plusieurs comptes valides")
    public void testValidUsersCanLogin(String username) {
        var productsPage = loginPage.loginAs(username, "secret_sauce");
        Assert.assertTrue(productsPage.getCurrentUrl().contains("inventory.html"),
                "Le compte " + username + " doit atteindre la page produits");
    }
}
