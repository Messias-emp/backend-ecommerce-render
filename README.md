# 🛒 E-Commerce API (Spring Boot)

API RESTful desenvolvida em **Java com Spring Boot** para um sistema de e-commerce completo. O projeto implementa arquitetura em camadas, segurança robusta com JWT, controle de perfis de acesso (ADMIN e USER) e persistência relacional com PostgreSQL.

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Security 6** (Autenticação Stateless com JWT)
- **Spring Data JPA / Hibernate**
- **PostgreSQL** (Banco de dados relacional)
- **Docker** (Containerização para deploy)
- **Maven** (Gerenciamento de dependências)

---

## 🏛️ Arquitetura e Padrões

O projeto segue princípios sólidos de desenvolvimento de software:
- **Separação de Camadas**: Controller, Service, Repository e DTOs bem definidos.
- **Segurança Stateless**: Filtros customizados (`JwtAuthFilter`) para validação de tokens por requisição.
- **CORS Centralizado**: Configuração flexível para aceitar requisições do frontend hospedado na Vercel.
- **Idempotência no carregamento de dados**: `CommandLineRunner` inteligente que popula dados iniciais e de teste de forma segura sem duplicar registros.

---

## 📡 Principais Endpoints

### Autenticação (`/auth`)
- `POST /auth/login` — Autentica o usuário e retorna o token JWT e a role.
- `POST /auth/register` — Registra um novo cliente na plataforma.

### Produtos (`/api/products`)
- `GET /api/products` — Lista todos os produtos ativos (Acesso público).
- `GET /api/products/{id}` — Retorna os detalhes de um produto específico.

### Pedidos (`/api/orders` — Requer Autenticação)
- `POST /api/orders/checkout` — Realiza o fechamento de um novo pedido.
- `GET /api/orders/my-orders` — Lista os pedidos do usuário autenticado.
- `GET /api/orders` — Lista todos os pedidos (Apenas ADMIN).

---

## ⚙️ Como Executar Localmente

1. Clone o repositório e configure as variáveis de ambiente do banco (`DATABASE_URL`, `DB_USERNAME`, `DB_PASSWORD`) ou utilize o perfil `dev`.
2. Execute o projeto via Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
