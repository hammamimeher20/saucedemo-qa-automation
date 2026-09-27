package com.saucedemo.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    /**
     * Capture un screenshot en base64, prêt à être embarqué directement
     * dans le rapport ExtentReports (aucun fichier externe à gérer).
     */
    public static String captureBase64(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (ClassCastException e) {
            System.err.println("Le driver ne supporte pas la capture d'écran : " + e.getMessage());
            return null;
        }
    }
}
