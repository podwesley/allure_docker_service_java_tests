package com.allure.docker.elements;

import com.allure.docker.annotation.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Elements class for Google Search page.
 * Contains all WebElements related to the Google Search page.
 */
@Logger(level = "DEBUG")
public class GoogleSearchElements {

    // Mapeando elementos com @FindBy
    @FindBy(name = "q")
    private WebElement searchBox;

    @FindBy(name = "btnK")
    private WebElement searchButton;

    /**
     * Constructor to initialize the page elements.
     * 
     * @param driver WebDriver instance
     */
    public GoogleSearchElements(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    /**
     * Get the search box element.
     * 
     * @return WebElement for the search box
     */
    public WebElement getSearchBox() {
        return searchBox;
    }

    /**
     * Get the search button element.
     * 
     * @return WebElement for the search button
     */
    public WebElement getSearchButton() {
        return searchButton;
    }
}
