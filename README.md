# Sistema de Gerenciamento de Estoque

## Visão Geral do Projeto

Este projeto é uma aplicação web desenvolvida em Java com o framework Spring Boot, projetada para gerenciar o estoque de uma empresa. O sistema permite o controle de materiais, fornecedores, categorias e movimentações de estoque (entradas e saídas), além de gerenciar o acesso de usuários com base em permissões.

## Funcionalidades Principais

-   **Gerenciamento de Materiais:** Cadastro, edição, exclusão e listagem de materiais.
-   **Controle de Fornecedores:** Gerenciamento completo dos dados dos fornecedores.
-   **Categorização:** Organização de materiais em categorias.
-   **Movimentação de Estoque:** Registro de todas as entradas e saídas de materiais, com atualização automática de saldo.
-   **Segurança:** Sistema de autenticação e autorização baseado em papéis (roles) e permissões.
-   **Interface Dinâmica:** Utilização de DataTables com processamento no lado do servidor para listagens eficientes.

## Tecnologias Utilizadas

-   **Backend:**
    -   Java 21
    -   Spring Boot 3
    -   Spring Data JPA
    -   Spring Security
    -   Maven
-   **Frontend:**
    -   Thymeleaf
    -   HTML, CSS, JavaScript
    -   Bootstrap
    -   jQuery DataTables
-   **Banco de Dados:**
    -   PostgreSQL

## Pré-requisitos

Antes de iniciar, certifique-se de que você tem os seguintes softwares instalados em sua máquina:
-   JDK 21 ou superior
-   Maven 3.8 ou superior
-   PostgreSQL 13 ou superior

## Configuração do Banco de Dados

1.  **Crie um banco de dados** no PostgreSQL com o nome de sua preferência (ex: `estoque_db`).
2.  Abra o arquivo `src/main/resources/application.properties`.
3.  **Atualize as propriedades** de conexão com o banco de dados com suas credenciais:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/estoque_db
    spring.datasource.username=seu_usuario_postgres
    spring.datasource.password=sua_senha_postgres
    ```
4.  A propriedade `spring.jpa.hibernate.ddl-auto=update` fará com que o Hibernate crie e atualize as tabelas automaticamente na primeira inicialização.

## Configuração e Instalação

Siga os passos abaixo para configurar e executar o projeto localmente:

1.  **Clone o repositório:**
    ```bash
    git clone <url_do_repositorio>
    cd gerenciamento_estoque
    ```

2.  **Compile o projeto com o Maven:**
    O Maven irá baixar todas as dependências listadas no arquivo `pom.xml`.
    ```bash
    mvn clean install
    ```

## Executando a Aplicação

Após a compilação bem-sucedida, você pode iniciar a aplicação usando o plugin do Spring Boot:

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

## Uso da Aplicação

-   **Acesso ao Sistema:**
    -   URL: `http://localhost:8080/login`
    -   **Usuário:** `admin`
    -   **Senha:** `123456`

-   **Dados Iniciais:**
    -   O arquivo `src/main/resources/data.sql` contém os dados iniciais para o usuário administrador e outras configurações básicas que são carregadas na inicialização da aplicação.