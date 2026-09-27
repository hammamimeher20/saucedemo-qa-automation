package com.saucedemo.base;

import com.saucedemo.listeners.TestListener;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@Listeners(TestListener.class)
public abstract class BaseTest {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected WebDriver driver;
    protected LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        // -Dbrowser=... (CLI/CI) prend le pas sur le paramètre testng.xml, qui prime sur "chrome" par défaut.
        String resolvedBrowser = System.getProperty("browser", browser);
        log.info("=== Démarrage du test — navigateur : {} ===", resolvedBrowser);
        driver = DriverFactory.initDriver(resolvedBrowser);
        loginPage = new LoginPage(driver).open(ConfigReader.get("base.url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    protected String stdUser() {
        return ConfigReader.get("user.standard");
    }

    protected String password() {
        return ConfigReader.get("user.password");
    }

    public WebDriver getDriver() {
        return driver;
    }
}
