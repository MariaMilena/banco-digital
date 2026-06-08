# Banco Digital API

API REST para transferências bancárias entre contas, desenvolvida com Spring Boot e PostgreSQL.

---

## Tecnologias

- Java 17
- Spring Boot 4.0.6
- Spring Data JPA
- PostgreSQL (Neon.tech)
- Springdoc OpenAPI (Swagger)
- Lombok
- JUnit 5 + Mockito

---

## Como rodar

### Pré-requisitos

- Java 17+
- Maven (ou usar o `./mvnw` incluso no projeto)

### Configurar o banco

Copie o arquivo de exemplo e preencha com suas credenciais:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Edite o `application.properties` com os dados do seu banco:

```properties
spring.datasource.url=jdbc:postgresql://<host>/neondb?sslmode=require&channel_binding=require
spring.datasource.username=<usuario>
spring.datasource.password=<senha>
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

springdoc.swagger-ui.path=/swagger-ui.html
```

### Banco de dados

O banco PostgreSQL está hospedado no [Neon.tech](https://neon.tech) e já possui as tabelas e dados iniciais configurados.

As credenciais de acesso devem ser preenchidas no `application.properties` conforme o passo anterior. Não é necessário executar nenhum script SQL manualmente.

### Executar

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

### Documentação interativa (Swagger)

http://localhost:8080/swagger-ui.html

---

## Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/accounts` | Criar nova conta |
| `GET` | `/accounts` | Listar todas as contas |
| `GET` | `/accounts/{id}` | Buscar conta por ID |
| `POST` | `/transfers` | Realizar transferência |
| `GET` | `/transfers/statement/{accountId}` | Consultar extrato |

### Exemplo — criar conta

```json
POST /accounts
{
  "name": "Carlos Lima",
  "initialBalance": 1500.00
}
```

### Exemplo — transferência

```json
POST /transfers
{
  "sourceId": "a1000000-0000-0000-0000-000000000001",
  "targetId": "a1000000-0000-0000-0000-000000000002",
  "amount": 200.00
}
```

---

## Testes

```bash
./mvnw test
```

Cenários cobertos no `TransferServiceTest`:

| Teste | Descrição |
|-------|-----------|
| `deveRealizarTransferenciaComSucesso` | Transferência válida debita origem e credita destino |
| `deveLancarExcecaoQuandoSaldoInsuficiente` | Impede transferência quando saldo é insuficiente |
| `deveLancarExcecaoQuandoContaOrigemNaoExiste` | Retorna erro quando conta não é encontrada |
| `naoDeveTransferirParaMesmaConta` | Impede transferência entre a mesma conta |

---
