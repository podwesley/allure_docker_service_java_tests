package br.com.selenium.pages;

import br.com.selenium.api.annotation.Logger;
import br.com.selenium.elements.GoogleSearchElements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

@Logger(level = "DEBUG")
public class GoogleSearchPage {

    private GoogleSearchElements googleSearchElements;

    public GoogleSearchPage(WebDriver driver) {
        this.googleSearchElements = new GoogleSearchElements(driver);
        PageFactory.initElements(driver, this);
    }

    public WebElement getSearchBox() {
        return googleSearchElements.getSearchBox();
    }

    public WebElement getSearchButton() {
        return googleSearchElements.getSearchButton();
    }
}