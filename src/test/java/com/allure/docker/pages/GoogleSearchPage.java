package com.allure.docker.pages;

    import com.allure.docker.annotation.Logger;
    import com.allure.docker.elements.GoogleSearchElements;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.support.PageFactory;

    /**
     * Page Object class for Google Search page.
     * Contains all elements and basic operations related to the Google Search page.
     */
    @Logger(level = "DEBUG")
    public class GoogleSearchPage {

        private GoogleSearchElements googleSearchElements;

        /**
         * Constructor to initialize the page elements.
         *
         * @param driver WebDriver instance
         */
        public GoogleSearchPage(WebDriver driver) {
            this.googleSearchElements = new GoogleSearchElements(driver);
            PageFactory.initElements(driver, this);
        }

        /**
         * Get the search box element.
         *
         * @return WebElement for the search box
         */
        public WebElement getSearchBox() {
            return googleSearchElements.getSearchBox();
        }

        /**
         * Get the search button element.
         *
         * @return WebElement for the search button
         */
        public WebElement getSearchButton() {
            return googleSearchElements.getSearchButton();
        }
    }