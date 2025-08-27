# Sistema de Gestão de Clínica Odontológica

Sistema completo para gestão de clínicas odontológicas desenvolvido com Spring Boot (backend) e React (frontend).

## 🚀 Tecnologias Utilizadas

### Backend
- **Spring Boot 3.x** - Framework principal
- **Spring Security** - Autenticação e autorização com JWT
- **Spring Data JPA** - Persistência de dados
- **H2 Database** - Banco de dados em memória (desenvolvimento)
- **PostgreSQL** - Banco de dados para produção
- **Maven** - Gerenciamento de dependências

### Frontend
- **React 18** - Biblioteca para interface de usuário
- **Material-UI (MUI)** - Componentes de interface moderna
- **React Router** - Roteamento
- **Axios** - Cliente HTTP
- **Vite** - Build tool e servidor de desenvolvimento

## 📋 Funcionalidades

- ✅ **Autenticação JWT** - Login seguro com tokens
- ✅ **Dashboard** - Métricas e indicadores da clínica
- ✅ **Gestão de Usuários** - Diferentes tipos (Admin, Dentista, Recepcionista)
- ✅ **Gestão de Pacientes** - Cadastro e controle de pacientes
- ✅ **Gestão de Dentistas** - Controle de profissionais
- ✅ **Gestão de Consultas** - Agendamento e controle de consultas
- ✅ **Controle de Acesso** - Rotas protegidas por perfil de usuário
- ✅ **Interface Responsiva** - Design moderno e adaptável

## 🛠️ Pré-requisitos

- **Java 17+** (testado com Java 23)
- **Node.js 20.19+** (para o frontend)
- **Maven 3.6+** (para o backend)
- **Git**

## 📦 Instalação e Execução

### 1. Clone o repositório
```bash
git clone <url-do-repositorio>
cd "Clinica Odonto"
```

### 2. Backend (Spring Boot)

```bash
cd backend

# Instalar dependências e executar
mvn clean install
mvn spring-boot:run
```

O backend estará disponível em: `http://localhost:8080`

### 3. Frontend (React)

```bash
cd frontend

# Instalar dependências
npm install

# Executar em modo de desenvolvimento
npm run dev
```

O frontend estará disponível em: `http://localhost:5173`

## 🗄️ Banco de Dados

### Desenvolvimento (H2)
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:clinica`
- **Username**: `sa`
- **Password**: *(vazio)*

### Dados Iniciais
O sistema já vem com dados de exemplo:

**Usuários:**
- **Admin**: admin@clinica.com / admin123
- **Dentista**: dentista@clinica.com / dentista123
- **Recepcionista**: recepcao@clinica.com / recepcao123

## 🏗️ Arquitetura

### Backend (Clean Architecture)
```
src/main/java/com/clinica/odonto/
├── application/          # Casos de uso e DTOs
│   ├── dto/             # Data Transfer Objects
│   └── service/         # Serviços de aplicação
├── domain/              # Entidades e regras de negócio
│   ├── entity/          # Entidades JPA
│   └── repository/      # Interfaces de repositório
├── infrastructure/      # Implementações técnicas
│   └── security/        # Configurações de segurança
└── presentation/        # Controllers e APIs
    └── controller/      # Controladores REST
```

### Frontend (Component-based)
```
src/
├── components/          # Componentes reutilizáveis
├── contexts/           # Contextos React (AuthContext)
├── hooks/              # Custom hooks
├── pages/              # Páginas da aplicação
├── services/           # Serviços de API
└── utils/              # Utilitários
```

## 🔐 Autenticação

O sistema utiliza JWT (JSON Web Tokens) para autenticação:

1. **Login**: POST `/api/auth/login`
2. **Token**: Retornado no login e armazenado no localStorage
3. **Autorização**: Token enviado no header `Authorization: Bearer <token>`
4. **Expiração**: Tokens expiram em 24 horas

## 📱 Rotas da API

### Autenticação
- `POST /api/auth/login` - Login do usuário
- `POST /api/auth/register` - Registro de usuário
- `GET /api/auth/me` - Dados do usuário atual

### Dashboard
- `GET /api/dashboard/metrics` - Métricas do dashboard

### Usuários (Admin apenas)
- `GET /api/usuarios` - Listar usuários
- `POST /api/usuarios` - Criar usuário
- `PUT /api/usuarios/{id}` - Atualizar usuário
- `DELETE /api/usuarios/{id}` - Deletar usuário

## 🎨 Interface

A interface foi desenvolvida com Material-UI, oferecendo:

- **Design Responsivo** - Funciona em desktop, tablet e mobile
- **Tema Personalizado** - Cores e tipografia da clínica
- **Navegação Intuitiva** - Sidebar com menu baseado no perfil do usuário
- **Feedback Visual** - Loading states, alertas e confirmações
- **Acessibilidade** - Componentes acessíveis por padrão

## 🔧 Configuração

### Backend (application.yml)
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:clinica
    username: sa
    password: 
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true

app:
  jwt:
    secret: clinica-odonto-secret-key-2024
    expiration: 86400000 # 24 horas
```

### Frontend (Vite)
O frontend está configurado para se comunicar com o backend em `http://localhost:8080`.

## 🚀 Deploy

### Backend
```bash
mvn clean package
java -jar target/clinica-odonto-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
npm run build
# Deploy da pasta 'dist' para seu servidor web
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 📞 Suporte

Para suporte, entre em contato através do email: suporte@clinicaodonto.com

---

**Desenvolvido com ❤️ para facilitar a gestão de clínicas odontológicas**