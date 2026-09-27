package com.saucedemo.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.saucedemo.base.BaseTest;
import com.saucedemo.utils.ExtentManager;
import com.saucedemo.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Relie l'exécution TestNG à ExtentReports : un ExtentTest par méthode,
 * avec statut, groupes, horodatage explicite (début/fin) et capture
 * d'écran automatique en cas d'échec.
 * Thread-safe via ThreadLocal (nécessaire avec parallel="tests" dans testng.xml).
 */
public class TestListener implements ITestListener {

    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        ExtentTest test = extent.createTest(testName, description);
        for (String group : result.getMethod().getGroups()) {
            test.assignCategory(group);
        }
        test.info("Démarré le : " + now());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test réussi : " + result.getMethod().getMethodName()
                + " — Terminé le : " + now());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = extentTest.get();
        test.log(Status.FAIL, "Test échoué le " + now() + " : " + result.getThrowable());

        WebDriver driver = extractDriver(result);
        if (driver != null) {
            String base64Screenshot = ScreenshotUtils.captureBase64(driver);
            if (base64Screenshot != null) {
                test.fail("Capture d'écran au moment de l'échec :",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, "Test ignoré le " + now() + " : " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    private String now() {
        return TIMESTAMP_FORMAT.format(LocalDateTime.now());
    }

    private WebDriver extractDriver(ITestResult result) {
        Object instance = result.getInstance();
        if (instance instanceof BaseTest) {
            return ((BaseTest) instance).getDriver();
        }
        return null;
    }
}
