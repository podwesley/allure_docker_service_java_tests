package com.allure.docker.actions;

import com.allure.docker.annotation.Logger;
import com.allure.docker.utils.LoggerManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;

import java.io.ByteArrayInputStream;

/**
 * This class encapsulates common Selenium API actions.
 * It provides methods for operations like navigating to a URL,
 * clicking, typing, and interacting with page elements via Page Object classes.
 */
@Logger(level = "DEBUG")
public class SeleniumActions {
    private WebDriver driver;

    public SeleniumActions(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Navega para uma URL.
     * @param url A URL para a qual navegar.
     */
    public void navigateTo(String url) {
        LoggerManager.debug("Navigating to URL: " + url);
        driver.get(url);
    }


    /**
     * Clear a text field and type into it.
     * @param element The WebElement to type into.
     * @param text The text to type.
     */
    public void clearAndType(WebElement element, String text) {
        LoggerManager.debug("Clearing and typing text: '" + text + "' into element: " + element.toString());
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Click on an element.
     * @param element The WebElement to click on.
     */
    public void click(WebElement element) {
        LoggerManager.debug("Clicking on element: " + element.toString());
        element.click();
    }

    /**
     * Press Enter key on an element.
     * @param element The WebElement to press Enter on.
     */
    public void pressEnter(WebElement element) {
        LoggerManager.debug("Pressing Enter key on element: " + element.toString());
        element.sendKeys(Keys.ENTER);
    }

    /**
     * Get the current URL.
     * @return The current URL as a String.
     */
    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        LoggerManager.debug("Current URL: " + url);
        return url;
    }

    /**
     * Get the page title.
     * @return The page title as a String.
     */
    public String getTitle() {
        String title = driver.getTitle();
        LoggerManager.debug("Page title: " + title);
        return title;
    }

    /**
     * Get the page source.
     * @return The page source as a String.
     */
    public String getPageSource() {
        LoggerManager.debug("Getting page source");
        return driver.getPageSource();
    }

    /**
     * Take a screenshot and attach it to the Allure report.
     * @param name The name of the screenshot.
     */
    public void takeScreenshot(String name) {
        LoggerManager.debug("Taking screenshot: " + name);
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), "png");
        } catch (WebDriverException e) {
            LoggerManager.error("Failed to take screenshot: " + name + ". Error: " + e.getMessage());
        }
    }

    /**
     * Check if a text is present in the page source.
     * Performs a case-insensitive check.
     * @param text The text to check for.
     * @return True if the text is present, false otherwise.
     */
    public boolean isTextPresentInPage(String text) {
        String pageSource = driver.getPageSource().toLowerCase();
        boolean isPresent = pageSource.contains(text.toLowerCase());
        LoggerManager.debug("Checking if text '" + text + "' is present in page: " + isPresent);
        return isPresent;
    }

    /**
     * Check if a text is present in the page title.
     * Performs a case-insensitive check.
     * @param text The text to check for.
     * @return True if the text is present, false otherwise.
     */
    public boolean isTextPresentInTitle(String text) {
        String title = driver.getTitle().toLowerCase();
        boolean isPresent = title.contains(text.toLowerCase());
        LoggerManager.debug("Checking if text '" + text + "' is present in title: " + isPresent);
        return isPresent;
    }
}
