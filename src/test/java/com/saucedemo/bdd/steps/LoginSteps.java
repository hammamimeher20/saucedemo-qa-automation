package com.saucedemo.bdd.steps;

import com.saucedemo.bdd.context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps {

    private final TestContext context;

    public LoginSteps(TestContext context) {
        this.context = context;
    }

    @Given("l'utilisateur est sur la page de login SauceDemo")
    public void userIsOnLoginPage() {
        Assert.assertTrue(context.getLoginPage().isLoginButtonDisplayed());
    }

    @When("l'utilisateur se connecte avec {string} et {string}")
    public void userLogsInWith(String username, String password) {
        context.getLoginPage().attemptLogin(username, password);
    }

    @Then("l'utilisateur est redirigé vers la page produits")
    public void userIsRedirectedToProducts() {
        Assert.assertTrue(context.getLoginPage().getCurrentUrl().contains("inventory.html"));
    }

    @Then("un message d'erreur contenant {string} est affiché")
    public void errorMessageContains(String expectedFragment) {
        Assert.assertTrue(context.getLoginPage().isErrorDisplayed());
        Assert.assertTrue(context.getLoginPage().getErrorMessage().contains(expectedFragment),
                "Message attendu contenant '" + expectedFragment + "' mais reçu : "
                        + context.getLoginPage().getErrorMessage());
    }

    @Given("l'utilisateur est connecté avec {string} et {string}")
    public void userIsLoggedInWith(String username, String password) {
        context.setProductsPage(context.getLoginPage().loginAs(username, password));
    }

    @When("l'utilisateur se déconnecte")
    public void userLogsOut() {
        context.setLoginPage(context.getProductsPage().logout());
    }

    @Then("l'utilisateur revient sur la page de login")
    public void userIsBackOnLoginPage() {
        Assert.assertTrue(context.getLoginPage().isLoginButtonDisplayed());
    }
}
