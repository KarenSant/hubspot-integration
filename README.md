# HubSpot Integration API

## 🚀 Sobre o Projeto
API REST em Java 11 com Spring Boot para integração com HubSpot, usando OAuth 2.0.

## 📌 Pré-requisitos
- Java 11
- Maven
- Conta HubSpot Developer

## 🔧  Como Rodar o Projeto

1. Clone o repositório:

   ```sh
   git clone https://github.com/seu-usuario/hubspot-integration.git
   cd hubspot-integration

   ```
2. Configure suas credenciais **OAuth** no **application.yml**:

     ```sh
    hubspot:
      client-id: CLIENT_ID
      client-secret: CLIENT_SECRET
      redirect-uri: http://localhost:8080/oauth/callback

      ```
3. Inicie a aplicação:

   ```sh
   mvn spring-boot:run

   ```
4. Fluxo **OAuth**:

   Gere a URL de autorização:

   ```sh
   GET http://localhost:8080/oauth/authorize
   ```

    Após login no HubSpot, copie o código da URL de callback e troque pelo token:
    ```sh
    GET http://localhost:8080/oauth/callback?code=SEU_CÓDIGO

5. Registre o webhook no HubSpot e envie eventos para:
    ```sh
   POST http://localhost:8080/webhooks/contact-created
