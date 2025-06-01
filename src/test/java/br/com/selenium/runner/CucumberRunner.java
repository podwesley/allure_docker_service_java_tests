package br.com.selenium.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Classe responsável por executar os testes de aceitação utilizando Cucumber.
 *
 * Esta classe configura e inicializa o framework Cucumber para executar os testes BDD.
 *
 * Configurações:
 * - Plugins de relatório: pretty, HTML, JUnit e Allure
 * - Localização dos step definitions: br.com.selenium.steps
 * - Localização dos arquivos .feature: src/test/resources
 *
 * @see cucumber.api.CucumberOptions
 * @see cucumber.api.junit.Cucumber
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber", "junit:target/cucumber.xml", "io.qameta.allure.cucumberjvm.AllureCucumberJvm"},
        glue = {"br.com.selenium.steps", "br.com.selenium.api.hooks"},
        features = "src/test/resources")
public class CucumberRunner {
}