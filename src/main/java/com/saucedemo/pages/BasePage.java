package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Classe mère de toutes les Page Objects.
 * Centralise les actions Selenium communes (attentes explicites, clics, saisies)
 * et journalise chaque action via SLF4J/Logback.
 */
public abstract class BasePage {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> waitVisibleAll(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        log.info("Clic sur {}", locator);
        waitClickable(locator).click();
    }

    protected void type(By locator, String text) {
        log.info("Saisie sur {} : '{}'", locator, maskIfSensitive(locator, text));
        WebElement element = waitVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        String text = waitVisible(locator).getText();
        log.debug("Texte lu sur {} : '{}'", locator, text);
        return text;
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    private String maskIfSensitive(By locator, String value) {
        String locatorStr = locator.toString().toLowerCase();
        return locatorStr.contains("password") ? "******" : value;
    }
}
