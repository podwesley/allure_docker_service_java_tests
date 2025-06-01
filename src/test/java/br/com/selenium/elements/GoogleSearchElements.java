package br.com.selenium.elements;

import br.com.selenium.api.annotation.Logger;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Logger(level = "DEBUG")
@Getter
public class GoogleSearchElements {

    public GoogleSearchElements(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(name = "q")
    private WebElement searchBox;

    @FindBy(name = "btnK")
    private WebElement searchButton;

}