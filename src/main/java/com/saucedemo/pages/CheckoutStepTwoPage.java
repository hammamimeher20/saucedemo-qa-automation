package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class CheckoutStepTwoPage extends BasePage {

    private final By itemNames = By.cssSelector(".inventory_item_name");
    private final By subtotalLabel = By.cssSelector(".summary_subtotal_label");
    private final By taxLabel = By.cssSelector(".summary_tax_label");
    private final By totalLabel = By.cssSelector(".summary_total_label");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getSummaryItemNames() {
        return waitVisibleAll(itemNames).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public String getSubtotal() {
        return getText(subtotalLabel);
    }

    public String getTax() {
        return getText(taxLabel);
    }

    public String getTotal() {
        return getText(totalLabel);
    }

    public CheckoutCompletePage finishOrder() {
        click(finishButton);
        return new CheckoutCompletePage(driver);
    }

    public ProductsPage cancel() {
        click(cancelButton);
        return new ProductsPage(driver);
    }
}
