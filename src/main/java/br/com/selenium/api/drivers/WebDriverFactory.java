package br.com.selenium.api.drivers;

import br.com.selenium.api.utils.LoggerManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Singleton factory class responsible for creating and managing WebDriver instances.
 * This class separates the driver management responsibility from the Hooks class.
 */
public class WebDriverFactory {
    private static WebDriverFactory instance;
    private WebDriver driver;
    
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final boolean IS_WINDOWS = OS_NAME.contains("win");
    private static final boolean IS_DOCKER = new File("/.dockerenv").exists() || System.getenv("DOCKER_CONTAINER") != null;
    
    // Private constructor to enforce singleton pattern
    private WebDriverFactory() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Gets the singleton instance of WebDriverFactory
     * @return The WebDriverFactory instance
     */
    public static synchronized WebDriverFactory getInstance() {
        if (instance == null) {
            instance = new WebDriverFactory();
        }
        return instance;
    }
    
    /**
     * Creates and returns a WebDriver instance if one doesn't exist,
     * or returns the existing instance.
     * @return The WebDriver instance
     */
    public synchronized WebDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }
    
    /**
     * Initializes the WebDriver with appropriate settings
     */
    private void initializeDriver() {
        LoggerManager.info("Ambiente detectado: " + (IS_WINDOWS ? "Windows" : "Linux/Docker"));

        try {
            // Configurar WebDriverManager para detectar automaticamente a versão do Chrome
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = createChromeOptions();

            LoggerManager.info("Iniciando ChromeDriver com as opções configuradas");
            driver = new ChromeDriver(options);
            
            // Configure driver timeouts
            configureDriverTimeouts();
        } catch (Exception e) {
            handleWebDriverSetupError(e);
        }
    }
    
    /**
     * Configures timeouts for the WebDriver
     */
    private void configureDriverTimeouts() {

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }
    
    private ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // Configurações comuns para todos os ambientes
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");

        if (IS_DOCKER || !IS_WINDOWS) {
    
            LoggerManager.debug("Configurando para ambiente Docker/Linux");
            options.addArguments("--headless");


            String chromeBinary = System.getenv("CHROME_BIN");
            if (chromeBinary != null && !chromeBinary.isEmpty()) {
                LoggerManager.debug("Usando Chrome binário em: " + chromeBinary);
                options.setBinary(chromeBinary);
            }
        } else {
       
            LoggerManager.debug("Configurando para ambiente Windows");
            configureChromePathForWindows(options);
        }

        return options;
    }
    

    private void configureChromePathForWindows(ChromeOptions options) {
  
        File defaultChromePath = new File("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
        File defaultChromePath2 = new File("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");

        if (defaultChromePath.exists()) {
            options.setBinary(defaultChromePath.getAbsolutePath());
            LoggerManager.debug("Usando Chrome em: " + defaultChromePath.getAbsolutePath());
        } else if (defaultChromePath2.exists()) {
            options.setBinary(defaultChromePath2.getAbsolutePath());
            LoggerManager.debug("Usando Chrome em: " + defaultChromePath2.getAbsolutePath());
        } else {
            LoggerManager.warn("Caminho padrão do Chrome não encontrado. Usando configuração padrão.");
        }
    }
    
    private void handleWebDriverSetupError(Exception e) {
        LoggerManager.error("Erro ao configurar WebDriver: " + e.getMessage(), e);
        LoggerManager.info("Tentando configuração alternativa...");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }
    
    public synchronized void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}