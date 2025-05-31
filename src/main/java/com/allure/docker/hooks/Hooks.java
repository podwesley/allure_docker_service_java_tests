package com.allure.docker.hooks;

import com.allure.docker.annotation.Logger;
import com.allure.docker.drivers.WebDriverFactory;
import com.allure.docker.utils.LoggerManager;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.google.common.io.Files;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Hooks class for Cucumber test lifecycle management.
 * Uses WebDriverFactory to manage WebDriver instances.
 */
@Logger(level = "DEBUG")
public class Hooks {
    private URL pathFile = Hooks.class.getResource("/files/");
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    // Reference to the WebDriver - now obtained from WebDriverFactory
    private WebDriver driver;

    @Before
    public void antesDoCenario(Scenario scenario) throws IOException {
        // Get the WebDriver instance from the factory
        driver = WebDriverFactory.getInstance().getDriver();

        LoggerManager.info("ChromeDriver iniciado com sucesso");

        // Adicionar anexos ao relatório Allure usando separador de arquivo do sistema
        addAllureAttachments();
    }

    private void addAllureAttachments() throws IOException {
        File image = new File(pathFile.getPath() + FILE_SEPARATOR + "wesley.png");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(image));
        Allure.addAttachment("Alguma Captura de Tela", byteArrayInputStream);

        File video = new File(pathFile.getPath() + FILE_SEPARATOR + "google.mp4");
        Allure.addAttachment("Algum vídeo", "video/mp4", Files.asByteSource(video).openStream(), "mp4");
    }

    @After
    public void depoisDoCenario(Scenario scenario) {
        if (scenario.isFailed() && driver != null) {
            captureFailureScreenshot(scenario);
        }

        // Let the factory handle quitting the driver
        WebDriverFactory.getInstance().quitDriver();
    }

    private void captureFailureScreenshot(Scenario scenario) {
        // Capturar screenshot em caso de falha
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment("Screenshot de Falha", "image/png", new ByteArrayInputStream(screenshot), "png");
    }
}
