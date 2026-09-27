package com.saucedemo.bdd.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.saucedemo.bdd",
        plugin = {
                "pretty",
                "html:test-output/cucumber-reports/cucumber.html",
                "json:test-output/cucumber-reports/cucumber.json"
        },
        monochrome = true
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {

    @Override
    public boolean useDataProviderWithScenarios() {
        return true; // exécution séquentielle des scénarios (evite les conflits de driver partagé)
    }
}
