package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Login avec un utilisateur valide")
    public void testLoginValidUser() {
        ProductsPage productsPage = loginPage.loginAs(stdUser(), password());
        Assert.assertTrue(productsPage.getCurrentUrl().contains("inventory.html"),
                "L'URL doit contenir inventory.html après un login valide");
        Assert.assertEquals(productsPage.getPageTitleText(), "Products");
    }

    @Test(groups = {"smoke", "regression"}, description = "Login avec un mot de passe incorrect")
    public void testLoginWrongPassword() {
        loginPage.attemptLogin(stdUser(), "wrong_password");
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"));
    }

    @Test(groups = {"sanity", "regression"}, description = "Login avec un utilisateur inexistant")
    public void testLoginNonExistentUser() {
        loginPage.attemptLogin("ghost_user", password());
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("do not match"));
    }

    @Test(groups = {"smoke", "regression"}, description = "Login avec des champs vides")
    public void testLoginEmptyFields() {
        loginPage.attemptLogin("", "");
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"));
    }

    @Test(groups = {"regression"}, description = "Login avec uniquement le nom d'utilisateur")
    public void testLoginUsernameOnly() {
        loginPage.attemptLogin(stdUser(), "");
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"));
    }

    @Test(groups = {"regression"}, description = "Login avec uniquement le mot de passe")
    public void testLoginPasswordOnly() {
        loginPage.attemptLogin("", password());
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"));
    }

    @Test(groups = {"smoke", "regression"}, description = "Déconnexion depuis le menu hamburger")
    public void testLogout() {
        ProductsPage productsPage = loginPage.loginAs(stdUser(), password());
        loginPage = productsPage.logout();
        Assert.assertTrue(loginPage.getCurrentUrl().endsWith("saucedemo.com/") ,
                "Le logout doit ramener à la page de login");
        Assert.assertTrue(loginPage.isLoginButtonDisplayed());
    }
}
