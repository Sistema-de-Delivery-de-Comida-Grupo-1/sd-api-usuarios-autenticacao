# Microsserviço de Usuários 👤

Este é o microsserviço responsável pelo gerenciamento de usuários (CRUD) e por orquestrar a ponte de comunicação para a autenticação no sistema distribuído.

## 🛠 Tecnologias Utilizadas
* **Java 21+**
* **Spring Boot 3.2+**
* **Spring Data JPA**
* **H2 Database** (Banco de dados em memória)
* **gRPC / Protobuf** (Comunicação síncrona como Cliente)
* **Lombok**

## ⚙️ Arquitetura e Fluxo
Este serviço atua como a porta de entrada (API REST) para requisições de clientes.
Para a rota de login, ele consulta a senha real do usuário no banco H2 e atua como um **gRPC Client**, enviando as credenciais através da rede para o `microsservico-autenticacao` validar e retornar o Token JWT.

## 🚀 Como Executar o Projeto

1. Certifique-se de ter o Java e o Maven instalados.
2. Clone este repositório.
3. Abra o terminal na raiz do projeto e execute:
   `.\mvnw spring-boot:run`
4. A aplicação iniciará na porta **8081**.

> **Aviso:** Para que o login funcione, o `microsservico-autenticacao` deve estar rodando simultaneamente na porta `8082`.

## 🌐 Endpoints REST (Porta 8081)

| Método | Rota | Descrição |
|---|---|---|
| **POST** | `/api/usuarios` | Cadastra um novo usuário. |
| **GET** | `/api/usuarios` | Retorna a lista de todos os usuários. |
| **GET** | `/api/usuarios/{id}` | Busca um usuário específico pelo ID. |
| **PUT** | `/api/usuarios/{id}` | Atualiza os dados de um usuário. |
| **DELETE**| `/api/usuarios/{id}` | Remove um usuário do banco de dados. |
| **POST** | `/api/usuarios/login`| Recebe credenciais e retorna um Token JWT. |

--- 
*Bacharelado em Sistemas de Informação - IF Goiano (Campus Urutaí)*