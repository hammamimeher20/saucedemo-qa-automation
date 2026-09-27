package com.saucedemo.bdd.hooks;

import com.saucedemo.bdd.context.TestContext;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.DriverFactory;
import com.saucedemo.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Cycle de vie de chaque scénario : initialise un driver dédié avant,
 * journalise l'horodatage de début/fin dans le rapport Cucumber, capture
 * un screenshot et ferme le driver après (attache la preuve en cas d'échec).
 */
public class Hooks {

    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUp(Scenario scenario) {
        scenario.log("Scénario démarré le : " + now());

        String browser = System.getProperty("browser", "chrome");
        var driver = DriverFactory.initDriver(browser);
        context.setDriver(driver);
        context.setLoginPage(new LoginPage(driver).open(ConfigReader.get("base.url")));
    }

    @After
    public void tearDown(Scenario scenario) {
        scenario.log("Scénario terminé le : " + now() + " — statut : " + scenario.getStatus());

        if (scenario.isFailed() && context.getDriver() != null) {
            byte[] screenshot = java.util.Base64.getDecoder()
                    .decode(ScreenshotUtils.captureBase64(context.getDriver()));
            scenario.attach(screenshot, "image/png", "Échec - " + scenario.getName());
        }
        DriverFactory.quitDriver();
    }

    private String now() {
        return TIMESTAMP_FORMAT.format(LocalDateTime.now());
    }
}
