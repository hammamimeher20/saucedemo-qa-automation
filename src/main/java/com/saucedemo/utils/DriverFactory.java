package com.saucedemo.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Fournit un WebDriver thread-safe (ThreadLocal) pour permettre
 * l'exécution parallèle des tests TestNG sans conflit d'instance.
 *
 * Supporte aussi l'exécution distante (Selenium Grid / Docker) en passant
 * -Dgrid.url=http://localhost:4444/wd/hub (voir docker-compose.yml).
 */
public class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver initDriver(String browser) {
        String browserName = (browser == null || browser.isBlank()) ? "chrome" : browser.toLowerCase();
        String gridUrl = System.getProperty("grid.url");

        WebDriver driver = (gridUrl != null && !gridUrl.isBlank())
                ? createRemoteDriver(browserName, gridUrl)
                : createLocalDriver(browserName);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driverThreadLocal.set(driver);
        log.info("Driver '{}' initialisé ({})", browserName, gridUrl != null ? "Grid : " + gridUrl : "local");
        return driver;
    }

    private static WebDriver createLocalDriver(String browserName) {
        switch (browserName) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();
            case "chrome-headless":
                WebDriverManager.chromedriver().setup();
                ChromeOptions headlessOptions = new ChromeOptions();
                headlessOptions.addArguments("--headless=new", "--window-size=1920,1080");
                return new ChromeDriver(headlessOptions);
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                return new ChromeDriver(options);
        }
    }

    private static WebDriver createRemoteDriver(String browserName, String gridUrl) {
        try {
            URL hubUrl = new URL(gridUrl);
            if ("firefox".equals(browserName)) {
                FirefoxOptions options = new FirefoxOptions();
                return new RemoteWebDriver(hubUrl, options);
            }
            ChromeOptions options = new ChromeOptions();
            if ("chrome-headless".equals(browserName)) {
                options.addArguments("--headless=new");
            }
            return new RemoteWebDriver(hubUrl, options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL du Selenium Grid invalide : " + gridUrl, e);
        }
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        if (driverThreadLocal.get() != null) {
            log.info("Fermeture du driver");
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
        }
    }
}
