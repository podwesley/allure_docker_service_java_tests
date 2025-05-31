Feature: Demo Allure com Google

  Scenario: Pesquisa Básica no Google
    Given Estou na página do Google
    When Eu pesquiso por "Selenium WebDriver"
    Then Eu vejo resultados relacionados a "Selenium"
    And O título da página contém "Selenium"

  Scenario: Exemplo Único Corrigido
    Given Estou em um site
    When Eu insiro "automação de testes" na página
    And Eu clico no botão de pesquisa
    Then Eu verifico que é "OK"

  Scenario: Pesquisa por Imagens no Google
    Given Estou na página do Google
    When Eu pesquiso por "paisagens naturais"
    Then Eu vejo resultados relacionados a "paisagens"
    And O título da página contém "paisagens naturais"

  Scenario: Pesquisa por Notícias no Google
    Given Estou na página do Google
    When Eu pesquiso por "notícias tecnologia"
    Then Eu vejo resultados relacionados a "tecnologia"
    And O título da página contém "notícias tecnologia"

  Scenario: Pesquisa por Termos Técnicos no Google
    Given Estou na página do Google
    When Eu pesquiso por "Cucumber BDD Selenium"
    Then Eu vejo resultados relacionados a "Cucumber"
    And Eu vejo resultados relacionados a "Selenium"
    And O título da página contém "Cucumber BDD Selenium"
