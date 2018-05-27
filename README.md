## Exemplo de API Rest para geração de boletos em Java EE

Este projeto é um exemplo simples de uma API Rest para geração de boletos em Java EE

### Requisitos:
* Java 8
* Wildfly 9
* Maven
* Postman for Windows 6 (opcional)

### Execução do projeto:
* Executar o seguinte comando para compilar o projeto:
```
mvn clean package
```

* Fazer o deploy do **boleto-api.ear** gerado em **\boleto-api\ear\target**

### Exemplo de consumo da API:

O arquivo **boleto-api.postman_collection.json** contém o exemplos de consumo da API através do Postman.

### Execução dos testes de integração:

Os testes de integração são executados através da classe **BoletoIntegrationTesting**.
Para executá-los é necessário executar o projeto através do Wildfly.
