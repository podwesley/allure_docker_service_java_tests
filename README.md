# PROJETO DEMO JAVA CUCUMBER-JVM USANDO ALLURE

## INSTALAÇÃO
### JDK8
- http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

### MAVEN 3.5.4 ou mais recente
- https://maven.apache.org/download.cgi
- https://maven.apache.org/install.html

### Configurar JAVA_HOME & M2_HOME
- https://access.redhat.com/documentation/en-us/jboss_enterprise_application_platform/5/html/microcontainer_user_guide/ch01
- https://www.mkyong.com/java/how-to-set-java_home-environment-variable-on-mac-os-x/

Verifique a instalação com:
```sh
java -version
```
```sh
mvn --version
```

## USO
Execute o Serviço Allure Docker a partir deste diretório
```sh
docker-compose up -d allure allure-ui
```

- Verifique se a API Allure está funcionando. Acesse -> http://localhost:5050/allure-docker-service/latest-report

- Verifique se a UI Allure está funcionando. Acesse -> http://localhost:5252/allure-docker-service-ui/

Cada vez que você executa os testes, o relatório Allure será atualizado.
Execute os testes:
```sh
 mvn test -Dtest=CucumberRunner
 ```

Nota: Use o plugin `--plugin io.qameta.allure.cucumberjvm.AllureCucumberJvm` se você executar a partir de qualquer IDE como Eclipse ou IntelliJIdea.

Veja a documentação aqui:
- https://github.com/fescobar/allure-docker-service
- https://github.com/fescobar/allure-docker-service-ui
