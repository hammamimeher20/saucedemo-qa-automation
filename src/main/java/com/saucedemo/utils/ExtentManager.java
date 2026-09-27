package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Fournit une instance unique (singleton) d'ExtentReports pour toute
 * l'exécution de la suite. Le rapport HTML est généré sous
 * test-output/extent-reports/, avec un nom horodaté par run, et affiche
 * la date/heure d'exécution en clair dans l'en-tête et le panneau System Info.
 */
public class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            LocalDateTime now = LocalDateTime.now();
            String fileTimestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(now);
            String displayTimestamp = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(now);
            String reportPath = "test-output/extent-reports/SauceDemo_Report_" + fileTimestamp + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("SauceDemo QA Automation Report - " + displayTimestamp);
            sparkReporter.config().setReportName("Résultats d'exécution - Selenium/TestNG (" + displayTimestamp + ")");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Projet", "SauceDemo QA Automation");
            extent.setSystemInfo("Framework", "Selenium 4 + TestNG 7 + POM");
            extent.setSystemInfo("Environnement", "https://www.saucedemo.com");
            extent.setSystemInfo("Date d'exécution", displayTimestamp);
        }
        return extent;
    }
}
