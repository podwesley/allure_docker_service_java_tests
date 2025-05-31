package com.allure.docker.drivers;

import com.allure.docker.utils.LoggerManager;
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
        // Define um tempo de espera implícito de 30 segundos para que o WebDriver aguarde elementos ficarem disponíveis
        // antes de lançar uma exceção. Isso evita falhas quando elementos demoram para carregar na página.
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        // Define um tempo limite de 60 segundos para o carregamento completo da página.
        // Isso evita timeouts em conexões lentas ou quando páginas são muito pesadas.
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);

        // Define um tempo limite de 30 segundos para a execução de scripts JavaScript assíncronos.
        // Importante para páginas que dependem de JavaScript para renderizar conteúdo.
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

        // Maximiza a janela do navegador para garantir que todos os elementos estejam visíveis.
        // Isso ajuda a evitar problemas de elementos não clicáveis por estarem fora da área visível.
        driver.manage().window().maximize();
    }
    
    /**
     * Creates Chrome options based on the environment
     * @return ChromeOptions configured for the current environment
     */
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
            // Configurações específicas para Docker/Linux
            LoggerManager.debug("Configurando para ambiente Docker/Linux");
            options.addArguments("--headless");

            // Usar o caminho do Chrome no Docker se disponível
            String chromeBinary = System.getenv("CHROME_BIN");
            if (chromeBinary != null && !chromeBinary.isEmpty()) {
                LoggerManager.debug("Usando Chrome binário em: " + chromeBinary);
                options.setBinary(chromeBinary);
            }
        } else {
            // Configurações específicas para Windows
            LoggerManager.debug("Configurando para ambiente Windows");
            configureChromePathForWindows(options);
        }

        return options;
    }
    
    /**
     * Configures Chrome path for Windows environment
     * @param options ChromeOptions to configure
     */
    private void configureChromePathForWindows(ChromeOptions options) {
        // Verificar se o caminho padrão do Chrome existe
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
    
    /**
     * Handles errors during WebDriver setup
     * @param e The exception that occurred
     */
    private void handleWebDriverSetupError(Exception e) {
        LoggerManager.error("Erro ao configurar WebDriver: " + e.getMessage(), e);

        // Tentar abordagem alternativa com configuração mais simples
        LoggerManager.info("Tentando configuração alternativa...");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }
    
    /**
     * Quits the driver and resets the instance
     */
    public synchronized void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}