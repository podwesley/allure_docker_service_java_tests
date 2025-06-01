package br.com.selenium.steps;

import br.com.selenium.api.actions.SeleniumActions;
import br.com.selenium.api.annotation.Logger;
import br.com.selenium.api.drivers.WebDriverFactory;
import br.com.selenium.pages.GoogleSearchPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

@Logger(level = "DEBUG")
public class StepsDefinitions {
    private SeleniumActions seleniumActions;
    private GoogleSearchPage googleSearchPage;

    public StepsDefinitions() {
        seleniumActions = new SeleniumActions(WebDriverFactory.getInstance().getDriver());
        googleSearchPage = new GoogleSearchPage(WebDriverFactory.getInstance().getDriver());
    }

    @Given("^Estou na página do Google$")
    public void estouNaPaginaDoGoogle() {
        seleniumActions.navigateTo("https://www.google.com");
        seleniumActions.takeScreenshot("Página do Google");
    }

    @Given("^Estou em um site$")
    public void estouEmUmSite() {

        seleniumActions.navigateTo("https://www.google.com");
        seleniumActions.takeScreenshot("Página do Google");
    }

    @When("^Eu insiro \"([^\"]*)\" na página$")
    public void euInsiroNaPagina(String texto) {

        WebElement searchBox = googleSearchPage.getSearchBox();
        seleniumActions.clearAndType(searchBox, texto);
        seleniumActions.takeScreenshot("Texto inserido: " + texto);
    }

    @When("^Eu pesquiso por \"([^\"]*)\"$")
    public void euPesquisoPor(String termo) {

        WebElement searchBox = googleSearchPage.getSearchBox();
        seleniumActions.clearAndType(searchBox, termo);
        seleniumActions.pressEnter(searchBox);
        seleniumActions.takeScreenshot("Pesquisa por: " + termo);
    }

    @When("^Eu clico no botão de pesquisa$")
    public void euClicoNoBotaoDePesquisa() {

        WebElement searchButton = googleSearchPage.getSearchButton();
        seleniumActions.click(searchButton);
        seleniumActions.takeScreenshot("Clique no botão de pesquisa");
    }

    @Then("^Eu verifico que é \"([^\"]*)\"$")
    public void euVerificoQueE(String status) throws Throwable {
        switch(status){
            case "FAILED":
                Assert.fail("FALHA PROPOSITAL");
                break;
            case "OK":

                String currentUrl = seleniumActions.getCurrentUrl();
                Assert.assertTrue("Não estamos em uma página do Google", 
                    currentUrl.contains("google"));
                break;
            case "SUCESS":

                String url = seleniumActions.getCurrentUrl();
                Assert.assertTrue("Não estamos em uma página do Google", 
                    url.contains("google"));
                break;
        }

        seleniumActions.takeScreenshot("Verificação: " + status);
    }

    @Then("^Eu vejo resultados relacionados a \"([^\"]*)\"$")
    public void euVejoResultadosRelacionadosA(String termo) {

        boolean containsTerm = seleniumActions.isTextPresentInPage(termo);
        Assert.assertTrue("Resultados não contêm o termo pesquisado: " + termo, containsTerm);

        seleniumActions.takeScreenshot("Resultados para: " + termo);
    }

    @Then("^O título da página contém \"([^\"]*)\"$")
    public void oTituloDaPaginaContem(String texto) {

        boolean titleContains = seleniumActions.isTextPresentInTitle(texto);
        boolean pageContains = seleniumActions.isTextPresentInPage(texto);


        Assert.assertTrue("Nem o título nem o conteúdo da página contém: " + texto, 
            titleContains || pageContains);

        seleniumActions.takeScreenshot("Verificação de título/conteúdo para: " + texto);
    }
}
