### Environment Setup Guide

#### Requisitos:
- **IntelliJ IDEA instalado**
- **Java 11 instalado**
- O projeto **hubspot-integration** já clonado

### Passo 1: Configurar o Ambiente de Desenvolvimento no IntelliJ IDEA

1. **Abrir o IntelliJ IDEA:**
   Abra o IntelliJ IDEA. Se você já tem o projeto clonado, basta abrir o projeto no IntelliJ IDEA.

2. **Verificar a Versão do Java:**
   Para garantir que o Java 11 está sendo usado, siga estas etapas:
    - Vá até `File > Project Structure > Project`.
    - Na aba `Project SDK`, verifique se o Java 11 está selecionado como SDK.
    - Se o Java 11 não estiver listado, adicione-o:
        - Clique em `New... > JDK`.
        - Selecione o diretório onde o Java 11 está instalado em sua máquina e clique em `OK`.

### Passo 2: Adicionar Variáveis de Ambiente no IntelliJ IDEA

Para configurar variáveis de ambiente como o `HUBSPOT_CLIENT_ID`, `HUBSPOT_CLIENT_SECRET` e outras, siga os passos abaixo:

1. **Abrir a Configuração de Execução:**
    - Clique no ícone de play no canto superior direito do IntelliJ IDEA.
    - Clique em `Edit Configurations`.

2. **Adicionar Variáveis de Ambiente:**
    - Selecione a configuração de execução do seu projeto.
    - Na seção `Environment`, encontre o campo `Environment variables`.
    - Clique no ícone de `...` ao lado do campo `Environment variables`.
    - Adicione as variáveis de ambiente necessárias no formato `NOME_VARIAVEL=valor`, uma por linha. Por exemplo:
      ```
      HUBSPOT_CLIENT_ID=client_id
      HUBSPOT_CLIENT_SECRET=client_secret
      HUBSPOT_REDIRECT_URI=http://localhost:8080/oauth/callback
      http://localhost:8080/oauth/authorize
      https://api.hubapi.com/oauth/v1/token
      HUBSPOT_API_URL=https://api.hubapi.com
      ```

### Passo 3: Executar o Projeto

1. **Executar a Aplicação:**
    - Com as variáveis de ambiente configuradas, clique no ícone de play para iniciar a aplicação.
    - Verifique o console para garantir que a aplicação está iniciando corretamente e que as variáveis de ambiente estão sendo carregadas.

### Testar Funcionalidades com `curl`

#### 1. Geração da Authorization URL
**Request:**
```sh
curl -X GET "http://localhost:8080/oauth/authorize"
```
**Response:**
```json
{
  "url": "https://app.hubspot.com/oauth/authorize?client_id=your_client_id&redirect_uri=http://localhost:8080/oauth/callback&scope=crm.objects.contacts.write+crm.objects.contacts.read+oauth&response_type=code"
}
```

#### 2. Processamento do Callback OAuth
Substitua `authorization_code` pelo código de autorização real que você receberá do HubSpot.
**Request:**
```sh
curl -X GET "http://localhost:8080/oauth/callback?code=authorization_code"
```
**Response:**
```json
{
  "access_token": "your_access_token",
  "refresh_token": "your_refresh_token",
  "expires_in": 21600
}
```

#### 3. Criação de Contatos
Substitua `your_access_token` pelo token de acesso real que você obteve do HubSpot.
**Request:**
```sh
curl -X POST "http://localhost:8080/contacts/create" \
     -H "Authorization: Bearer your_access_token" \
     -H "Content-Type: application/json" \
     -d '{
           "firstName": "Test",
           "lastName": "One",
           "email": "test.one@example.com"
         }'
```
**Response:**
```json
{
  "id": "106346311318",
  "properties": {
    "createdate": "2025-03-16T01:42:35.436Z",
    "email": "test.one@example.com",
    "firstname": "Test",
    "lastname": "One",
    "hs_object_id": "106346311318"
  },
  "createdAt": "2025-03-16T01:42:35.436Z",
  "updatedAt": "2025-03-16T01:42:35.436Z",
  "archived": false
}
```

#### 4. Recebimento de Webhook para Criação de Contatos
**Request:**
```sh
curl -X POST "http://localhost:8080/webhook/contact" \
     -H "Content-Type: application/json" \
     -d '{
           "eventId": "12345",
           "subscriptionId": "67890",
           "portalId": "123456",
           "occurredAt": "2025-03-15T12:34:56.789Z",
           "subscriptionType": "contact.creation",
           "attemptNumber": 1,
           "objectId": "98765",
           "changeSource": "CRM",
           "changeFlag": "NEW",
           "appId": "13579",
           "eventType": "contact.creation"
         }'
```
**Response:**
```json
{
  "message": "Evento de criação de contato processado com sucesso"
}
```

### Conclusão
Certifique-se de substituir os valores de exemplo pelos valores reais que você usará em sua aplicação. Esses comandos `curl` devem ajudá-lo a testar todos os endpoints da sua aplicação end-to-end. Se você encontrar algum problema, verifique os logs da aplicação para identificar qualquer erro durante a execução dos comandos `curl`.### Conclusão
Certifique-se de substituir os valores de exemplo pelos valores reais que você usará em sua aplicação. Esses comandos `curl` devem ajudá-lo a testar todos os endpoints da sua aplicação end-to-end. Se você encontrar algum problema, verifique os logs da aplicação para identificar qualquer erro durante a execução dos comandos `curl`.