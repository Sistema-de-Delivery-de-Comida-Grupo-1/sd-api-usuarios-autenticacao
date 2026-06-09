# Microsserviço de Usuários 

Este é o microsserviço responsável pelo gerenciamento de usuários (CRUD) e por orquestrar a ponte de comunicação para a autenticação no sistema distribuído.

## Tecnologias Utilizadas
* **Java 21+**
* **Spring Boot 3.2+**
* **Spring Data JPA**
* **H2 Database** (Banco de dados em memória)
* **gRPC / Protobuf** (Comunicação síncrona como Cliente)
* **Spring Cloud Netflix Eureka Client** (Service Discovery)
* **Spring for RabbitMQ** (Mensageria e Eventos)
* **Lombok**

##  Atendimento aos Requisitos Técnicos (Arquitetura)

Este serviço implementa conceitos essenciais de sistemas distribuídos exigidos no escopo do projeto:

### 1. Invocação Remota (RPC) - Comunicação Síncrona
* **Implementação gRPC:** Atua como um *gRPC Client* (`net.devh:grpc-client-spring-boot-starter`). Durante o fluxo de login, o serviço captura as credenciais enviadas pelo usuário e faz uma chamada síncrona (RPC) de alta performance para o microsserviço de autenticação.
* O contrato de comunicação é estritamente definido via Protobuf (`auth.proto`).

### 2. Serviço de Nomes (Service Discovery)
* **Implementação Eureka:** Atua como um *Eureka Client*. Em vez de apontar para um IP estático para encontrar o servidor de autenticação, ele utiliza a URI `discovery:///microsservico-autenticacao`. O serviço consulta o catálogo do Eureka dinamicamente para descobrir o IP e a porta gRPC corretos em tempo de execução.

### 3. Mensageria e Eventos (Processamento Assíncrono)
* **Implementação RabbitMQ:** Utilizado para evitar acoplamento temporal e gargalos de requisição.
* Ele está configurado para publicar eventos (Publish/Subscribe) em *Exchanges* quando ações importantes ocorrem (ex: criação de um novo usuário), permitindo que outros microsserviços (como o de Notificação) consumam esses dados de forma assíncrona.

## ️ Arquitetura e Fluxo
Este serviço atua como a porta de entrada (API Gateway/REST) para requisições de clientes.
Para a rota de login, ele consulta a senha real do usuário no banco H2 e atua como um **gRPC Client**, enviando as credenciais através da rede de forma segura para o `microsservico-autenticacao` validar e retornar o Token JWT.

##  Como Executar o Projeto

1. Certifique-se de ter o Java e o Maven instalados.
2. **Pré-requisito:** O **Eureka Server** (`localhost:8761`) deve estar rodando.
3. Clone este repositório.
4. Abra o terminal na raiz do projeto e execute:
   `.\mvnw spring-boot:run`
5. A aplicação iniciará na porta **8081**.

> **Aviso:** Para que o login funcione, o `microsservico-autenticacao` deve estar registrado no Eureka e rodando simultaneamente (escutando web na 8082 e gRPC na 8083).

##  Endpoints REST (Porta 8081)

| Método | Rota | Descrição |
|---|---|---|
| **POST** | `/api/usuarios` | Cadastra um novo usuário. |
| **GET** | `/api/usuarios` | Retorna a lista de todos os usuários com dados completos. |
| **GET** | `/api/usuarios/{id}` | Busca um usuário específico pelo ID. |
| **GET** | `/api/usuarios/{id}/resumo` | Retorna apenas informações públicas (ID e Nome) via DTO. |
| **PUT** | `/api/usuarios/{id}` | Atualiza os dados de um usuário. |
| **DELETE**| `/api/usuarios/{id}` | Remove um usuário do banco de dados. |
| **POST** | `/api/usuarios/login`| Recebe credenciais e retorna um Token JWT via gRPC. |

--- 
**Desenvolvido por:** Cristian Martins Fernandes  
*Bacharelado em Sistemas de Informação - IF Goiano (Campus Urutaí)*