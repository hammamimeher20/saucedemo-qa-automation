package com.saucedemo.bdd.steps;

import com.saucedemo.bdd.context.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class CheckoutSteps {

    private final TestContext context;

    public CheckoutSteps(TestContext context) {
        this.context = context;
    }

    @And("l'utilisateur a ajouté le premier produit au panier")
    public void userAddsFirstProductToCart() {
        context.getProductsPage().addProductToCartByIndex(0);
        context.setCartPage(context.getProductsPage().goToCart());
    }

    @When("l'utilisateur passe au checkout avec le prénom {string}, le nom {string} et le code postal {string}")
    public void userProceedsToCheckout(String firstName, String lastName, String postalCode) {
        var checkoutStepOne = context.getCartPage().goToCheckout();
        context.setCheckoutStepOnePage(checkoutStepOne);

        boolean hasError = firstName.isEmpty() || lastName.isEmpty() || postalCode.isEmpty();
        if (hasError) {
            checkoutStepOne.attemptContinue(firstName, lastName, postalCode);
        } else {
            context.setCheckoutStepTwoPage(checkoutStepOne.fillInfoAndContinue(firstName, lastName, postalCode));
        }
    }

    @And("l'utilisateur finalise la commande")
    public void userFinishesOrder() {
        context.setCheckoutCompletePage(context.getCheckoutStepTwoPage().finishOrder());
    }

    @Then("le message de confirmation {string} est affiché")
    public void confirmationMessageIsDisplayed(String expectedMessage) {
        Assert.assertTrue(context.getCheckoutCompletePage().isOrderConfirmed());
        Assert.assertEquals(context.getCheckoutCompletePage().getConfirmationMessage(), expectedMessage);
    }

    @Then("un message d'erreur du checkout contenant {string} est affiché")
    public void checkoutErrorMessageContains(String expectedFragment) {
        Assert.assertTrue(context.getCheckoutStepOnePage().isErrorDisplayed());
        Assert.assertTrue(context.getCheckoutStepOnePage().getErrorMessage().contains(expectedFragment),
                "Message attendu contenant '" + expectedFragment + "' mais reçu : "
                        + context.getCheckoutStepOnePage().getErrorMessage());
    }
}
