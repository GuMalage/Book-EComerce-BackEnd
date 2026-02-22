
# 📘 Back-end


# 📚 BookStore API – Back-end

API REST desenvolvida para o sistema **BookStore - Projeto Final De Programação WEB**, um E-Commerce de Livros.

Construída com **Java + Spring Boot**, a API é responsável pelo gerenciamento de usuários, livros, carrinho, pedidos e endereços.

---

## 🏗️ Arquitetura

O projeto segue o padrão de **arquitetura em camadas**:

```
Controller → Service → Repository → Model
```

* **Controller** → Endpoints REST
* **Service** → Regras de negócio
* **Repository** → Acesso a dados
* **Model** → Entidades do sistema

---

## 🚀 Funcionalidades da API

✔️ Cadastro de usuários
✔️ Autenticação (Login / Logout)
✔️ CRUD de livros
✔️ Paginação por categoria
✔️ Gerenciamento de carrinho
✔️ Processamento de pedidos
✔️ Histórico de compras
✔️ Cadastro e listagem de endereços

---

## 🛠️ Tecnologias Utilizadas

* Java 24
* Spring Boot
* Spring Web
* Spring Data JPA
* Maven

---

## ▶️ Como Executar

### 📌 Pré-requisitos

* Java 24 ou superior instalado

### 📥 Clonar o repositório

```bash
git clone https://github.com/Rafaela0203/Projeto_final_PW44S-E-Commerce.git
cd Projeto_final_PW44S-E-Commerce
```

### ▶️ Executar a aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em:

```
http://localhost:8080
```

---

## 🎯 Objetivo

Fornecer uma API REST robusta para um sistema de E-Commerce, aplicando boas práticas de desenvolvimento back-end e arquitetura em camadas.


