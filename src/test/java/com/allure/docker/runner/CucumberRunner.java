package com.allure.docker.runner;

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
 * - Localização dos step definitions: com.allure.docker.steps
 * - Localização dos arquivos .feature: src/test/resources
 *
 * @see cucumber.api.CucumberOptions
 * @see cucumber.api.junit.Cucumber
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber", "junit:target/cucumber.xml", "io.qameta.allure.cucumberjvm.AllureCucumberJvm"},
        glue = {"com.allure.docker.steps", "com.allure.docker.hooks"},
        features = "src/test/resources")
public class CucumberRunner {
}
