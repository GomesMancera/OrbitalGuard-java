# 🛰️ OrbitalGuard — Módulo de Monitoramento Ambiental (Java/Spring)

Serviço backend responsável por gerenciar **regiões monitoradas**, registrar **leituras de sensores ambientais** (água e ar), gerar **alertas automáticos** e rastrear **ocorrências** de anomalias.  
É o módulo Java da plataforma **OrbitalGuard**, integrado à Global Solution FIAP 2025 (Java Advanced).

---

## Links do Projeto

| Recurso | URL |
|---------|-----|
| Repositório GitHub | https://github.com/GomesMancera/OrbitalGuard-java |
| Deploy (API) | https://orbitalguard-api.onrender.com |
| Swagger / OpenAPI | https://orbitalguard-api.onrender.com/swagger-ui/index.html |
| Health Check | https://orbitalguard-api.onrender.com/actuator/health |
| Vídeo apresentação (10 min) | https://youtu.be/HNdScQvER8I |
| Vídeo pitch (3 min) |  |

---


## ✨ Visão Geral

- **Contexto da GS**: Monitoramento ambiental, sustentabilidade e alertas em tempo real.
- **Domínio**: Regiões geográficas + Sensores especializados (água/ar) + Leituras + Alertas + Ocorrências.
- **Objetivo**:
    - Centralizar o gerenciamento de regiões monitoradas com coordenadas GPS.
    - Registrar **sensores especializados** (SensorAgua/SensorAr) com dados específicos do tipo.
    - Coletar **leituras em tempo real** com chave composta (sensor + timestamp).
    - Emitir **alertas automáticos** quando métricas excedem limiares.
    - Rastrear **ocorrências** vinculadas aos alertas para análise histórica.
- **Consumidores da API**:
    - Painéis web/mobile de monitoramento ambiental.
    - Sistemas de ONGs e órgãos ambientais.
    - Integrações com pipelines de analytics.

---

## 🧱 Stack Técnica

- **Linguagem**: Java 17
- **Framework**: Spring Boot 3.3.x
- **Módulos Spring**:
    - `spring-boot-starter-web` (API REST)
    - `spring-boot-starter-data-jpa` (persistência com Hibernate)
    - `spring-boot-starter-validation` (Bean Validation)
    - `spring-boot-starter-security` (autenticação JWT)
    - `spring-boot-starter-actuator` (health, info)
    - `spring-boot-devtools` (hot reload em dev)
- **Banco de dados**:
    - **H2 em memória** (dev & testes)
    - **PostgreSQL** (produção)
- **Documentação**: Springdoc OpenAPI (`/swagger-ui/index.html`)
- **Segurança**: JWT (JJWT) + Spring Security
- **Hipermídia**: Spring HATEOAS (links em respostas)
- **Ferramentas**:
    - Lombok (redução de boilerplate)
    - Maven (build system)
    - Docker (containerização)
    - GitHub Actions (CI/CD)
- **Teste**:
    - `@WebMvcTest` para controllers
    - `@DataJpaTest` para repositórios
    - `@SpringBootTest` para contexto completo

---

## 🔧 Perfis & Configuração

### Perfis Ativos

- **`dev` (padrão)**
    - Banco H2 em memória (`jdbc:h2:mem:orbitalguard;MODE=PostgreSQL`)
    - Console H2 em `/h2-console`
    - Segurança relaxada (`permitAll` em `/api/v1/regioes` para GET)
    - Seed de dados via `DataInitializer` (cria usuário admin)

- **`prod`**
    - Banco PostgreSQL via variável de ambiente `DATABASE_URL`
    - Segurança rigorosa: JWT obrigatório em endpoints protegidos
    - Sem acesso a `/h2-console`

### `application.yml` (Resumo)

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:orbitalguard  # dev
    username: sa
    password: ""
  jpa:
    hibernate.ddl-auto: create-drop  # dev
    show-sql: true
  h2.console.enabled: true  # dev

  security:
    jwt:
      secret: ${JWT_SECRET:default-secret-key-for-dev}  # mín 32 chars em prod

app:
  jwt:
    expiration: 86400000  # 24 horas
```

Para **produção** (`application-prod.yml`):
```yaml
spring:
  datasource:
    url: ${DATABASE_URL}  # Render PostgreSQL
  jpa:
    hibernate.ddl-auto: validate
    show-sql: false
```

### Variáveis de Ambiente (Produção)

```bash
# Banco PostgreSQL (Render)
DATABASE_URL=postgresql://user:pass@host:5432/orbitalguard

# JWT (mínimo 32 caracteres)
JWT_SECRET=sua-chave-secreta-aleatoria-com-32-caracteres

# Perfil ativo
SPRING_PROFILES_ACTIVE=prod
```

---

## 🗃️ Estrutura do Banco de Dados

### Entidades JPA (8 no total)

```
Regiao
  ├─ coordenadas: Coordenadas (embedded)
  ├─ sensores: List<Sensor>
  ├─ alertas: List<Alerta>
  └─ ocorrencias: List<Ocorrencia>

Sensor (abstract, inheritance=JOINED)
  ├─ SensorAgua (phMinimo, phMaximo, turbidezMaxima)
  └─ SensorAr (umidadeMinima, temperaturaMaxima, particuladosMaximos)
  └─ localizacao: Coordenadas (embedded)

LeituraSensor
  ├─ id: LeituraSensorId (composite key)
  ├─ sensor: Sensor
  └─ valor: Double

Alerta
  ├─ regiao: Regiao
  ├─ nivelSeveridade: NivelSeveridade (BAIXO, MÉDIO, ALTO, CRÍTICO)
  └─ ocorrencias: List<Ocorrencia>

Ocorrencia
  ├─ alerta: Alerta
  ├─ usuario: Usuario
  └─ descricao: String

Usuario
  ├─ role: Role (ADMIN, OPERADOR, USUARIO)
  └─ ocorrencias: List<Ocorrencia>
```

### Características Avançadas JPA

1. **Herança JOINED**: `Sensor` → `SensorAgua` / `SensorAr`
   - Cada subtipo em tabela separada com FK para `sensor`
   - `@DiscriminatorColumn(name="tipo_sensor")`

2. **Chave Composta**: `LeituraSensorId`
   ```java
   @Embeddable
   public class LeituraSensorId {
       private Long sensorId;
       private LocalDateTime dataHoraLeitura;
   }
   ```
   - Garante apenas 1 leitura por sensor/timestamp

3. **Embedded Type**: `Coordenadas`
   ```java
   @Embeddable
   public class Coordenadas {
       private Double latitude;   // -90 a 90
       private Double longitude;  // -180 a 180
   }
   ```
   - Reutilizado em `Regiao` e `Sensor`

---

## 🔌 Endpoints (v1)

Todos expostos no path base `/api/v1`.  
Documentação interativa em: **`/swagger-ui/index.html`** (dev e prod).

### 1) Autenticação

#### POST `/api/v1/auth/register`

Registrar novo usuário. Role padrão: `OPERADOR`.

**Request:**
```json
{
  "nome": "João Silva",
  "email": "joao@example.com",
  "senha": "Senha@123"
}
```

**Response (202 Accepted):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer"
}
```

**Exemplo cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"nome":"João","email":"joao@example.com","senha":"Senha@123"}'
```

#### POST `/api/v1/auth/login`

Fazer login e obter JWT. Válido por 24 horas.

---

### 2) Regiões (Públicas para GET, Privadas para Criar/Editar)

#### GET `/api/v1/regioes`

Listar **todas** as regiões (sem autenticação).

**Response:**
```json
[
  {
    "id": 1,
    "nome": "Amazônia",
    "coordenadas": {"latitude": -3.46, "longitude": -62.21},
    "_links": {
      "self": {"href": "/api/v1/regioes/1"},
      "update": {"href": "/api/v1/regioes/1"},
      "delete": {"href": "/api/v1/regioes/1"}
    }
  }
]
```

#### POST `/api/v1/regioes`

Criar região. Requer role `ADMIN` ou `OPERADOR`.

**Request (com JWT):**
```bash
curl -X POST http://localhost:8080/api/v1/regioes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Região Amazônia",
    "descricao": "Monitoramento da Amazônia",
    "latitude": -3.4653,
    "longitude": -62.2159
  }'
```

#### PUT `/api/v1/regioes/{id}`

Atualizar região. Requer `ADMIN` ou `OPERADOR`.

#### DELETE `/api/v1/regioes/{id}`

Deletar região. Requer `ADMIN`.

---

### 3) Sensores (com Herança JPA)

#### POST `/api/v1/sensores`

Criar sensor (água ou ar). Requer JWT.

**Request:**
```json
{
  "nome": "Sensor Rio Negro",
  "localizacao": {"latitude": -3.10, "longitude": -60.02},
  "tipo": "AGUA",
  "regiaoId": 1,
  "phMinimo": 6.5,
  "phMaximo": 8.5,
  "turbidezMaxima": 50
}
```

**Resposta inclui tipo específico:**
```json
{
  "id": 1,
  "nome": "Sensor Rio Negro",
  "tipo_sensor": "AGUA",
  "phMinimo": 6.5,
  "phMaximo": 8.5,
  "_links": { ... }
}
```

#### GET `/api/v1/sensores`

Listar todos os sensores (herança transparente).

---

### 4) Leituras (com Chave Composta)

#### POST `/api/v1/leituras`

Registrar leitura para um sensor. Chave composta garante 1 por timestamp.

**Request:**
```json
{
  "sensorId": 1,
  "dataHoraLeitura": "2025-06-09T14:30:00",
  "valor": 7.5
}
```

#### GET `/api/v1/leituras`

Listar leituras com paginação.

**Parâmetros:**
- `sensorId` (opcional)
- `page=0`, `size=10` (paginação)

---

### 5) Alertas

#### POST `/api/v1/alertas`

Criar alerta para uma região.

**Request:**
```json
{
  "regiaoId": 1,
  "nivelSeveridade": "ALTO",
  "descricao": "Nível de barulho acima do normal"
}
```

#### GET `/api/v1/alertas`

Listar alertas de uma região (filtro por `regiaoId`).

---

### 6) Ocorrências

#### POST `/api/v1/ocorrencias`

Registrar ocorrência vinculada a um alerta.

**Request:**
```json
{
  "alertaId": 1,
  "descricao": "Evento confirmado por inspeção local"
}
```

#### GET `/api/v1/ocorrencias`

Listar ocorrências.

---

### 7) Usuários (Listagem)

#### GET `/api/v1/usuarios`

Listar usuários (requer autenticação). Apenas `ADMIN` pode listar todos.

---

## 🌐 Validação & Tratamento de Erros

### Validações Bean Validation

- **Regiões**: `nome` (@NotBlank, 1-100 chars), `latitude` (-90 a 90), `longitude` (-180 a 180)
- **Sensores**: `nome` (@NotBlank), `tipo` (@NotNull, enum)
- **Leituras**: `dataHoraLeitura` (@NotNull), `valor` (@NotNull, range)
- **Alertas**: `nivelSeveridade` (enum BAIXO/MÉDIO/ALTO/CRÍTICO)
- **Usuários**: `email` (@Email, @NotNull), `senha` (min 8 chars)

### Resposta de Erro (GlobalExceptionHandler)

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação",
  "path": "/api/v1/regioes",
  "fieldErrors": {
    "nome": "Tamanho deve estar entre 1 e 100",
    "latitude": "Deve estar entre -90.0 e 90.0"
  }
}
```

---

## 🛡️ Segurança (Spring Security + JWT)

### Autenticação JWT

```
1. Usuário registra/faz login → senha validada com BCrypt
2. JWT gerado com claims: { sub, roles, iat, exp }
3. Cliente envia: Authorization: Bearer <token>
4. JwtAuthenticationFilter extrai e valida token
5. Spring Security monta authentication context
6. @PreAuthorize("hasRole('ADMIN')") valida autorização
```

### Perfis de Segurança

**Dev** (`SecurityConfig.devChain()`):
```yaml
permitAll:
  - GET /api/v1/regioes
  - POST /api/v1/auth/**
  - /h2-console/**
  - /swagger-ui/**
```

**Prod** (`SecurityConfig.prodChain()`):
```yaml
requiresJWT:
  - POST /api/v1/regioes
  - PUT /api/v1/regioes/{id}
  - DELETE /api/v1/regioes/{id}
  - POST /api/v1/sensores
  - ...
```

### Roles

| Role | Permissões |
|------|-----------|
| `ADMIN` | CRUD completo |
| `OPERADOR` | CREATE, READ, UPDATE (sem DELETE) |
| `USUARIO` | READ only |

---

## 🧪 Testes Automatizados

Localizados em `src/test/java/br/com/fiap/orbitalguard/integration`:

### RegiaoIntegrationTest
- `@SpringBootTest @AutoConfigureMockMvc`
- Testa: POST (202), GET, PUT, DELETE
- Valida: HATEOAS links, autenticação, autorização

### SensorIntegrationTest
- Testa: Herança JOINED (SensorAgua, SensorAr)
- Valida: Tipo específico retornado na resposta

### AuthIntegrationTest
- Testa: Register → JWT válido
- Valida: Token expiração, refresh não implementado

### LeituraSensorIntegrationTest
- Testa: Chave composta LeituraSensorId
- Valida: Apenas 1 leitura por (sensor, timestamp)

### AlertaIntegrationTest & OcorrenciaIntegrationTest
- Testes de CRUD com validações

**Executar testes:**
```bash
mvn test
# Ou especifico
mvn test -Dtest=RegiaoIntegrationTest
```

---

## 🐳 Docker & Deploy

### Dockerfile (Multi-stage)

```dockerfile
# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests clean package

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/orbitalguard-1.0.0.jar app.jar
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Build local:**
```bash
mvn clean package -DskipTests
docker build -t orbitalguard:latest .
docker run -p 8080:8080 orbitalguard:latest
```

### Deploy em Render (Nuvem)

**render.yaml** (Blueprint automático):
```yaml
services:
  - type: web
    name: orbitalguard-api
    env: docker
    dockerfilePath: ./Dockerfile
    envVars:
      - key: SPRING_PROFILES_ACTIVE
        value: prod
      - key: JWT_SECRET
        generateValue: true
      - key: DATABASE_URL
        fromDatabase:
          name: orbitalguard-db
          property: connectionString
databases:
  - name: orbitalguard-db
    engine: postgresql
```

**URL em Produção:**
```
https://orbitalguard-api.onrender.com
```

---

## 💻 Como Rodar Localmente

### Pré-requisitos

- Java 17+
- Maven 3.9+
- Git
- (Opcional) PostgreSQL local

### Passos

```bash
# 1. Clonar
git clone https://github.com/GomesMancera/OrbitalGuard-java.git
cd OrbitalGuard-java

# 2. Compilar
mvn clean compile

# 3. Rodar em dev (H2 em memória, sem auth)
mvn spring-boot:run

# 4. Testar
curl http://localhost:8080/api/v1/regioes
curl http://localhost:8080/swagger-ui/index.html
curl http://localhost:8080/h2-console
```

**Credenciais seed (dev):**
- Email: `admin@orbitalguard.com`
- Senha: `admin123`
- Role: `ADMIN`

---

## ✅ Checklist de Requisitos (Java Advanced FIAP)

- [x] **Anotações Spring** (`@Entity`, `@Service`, `@Component`, `@Autowired`)
- [x] **Injeção de Dependências** (`@RequiredArgsConstructor`, `@Autowired`)
- [x] **Spring Data JPA** (8 repositórios, `findBy*` queries)
- [x] **Validação Bean Validation** (DTOs com Records)
- [x] **Herança JPA** (JOINED strategy em `Sensor`)
- [x] **Chaves Compostas** (LeituraSensorId)
- [x] **Tipos Embutidos** (Coordenadas)
- [x] **Spring Security + JWT** (autenticação, autorização)
- [x] **REST correto** (verbos HTTP, status codes, HATEOAS)
- [x] **Tratamento de Erros Global** (`GlobalExceptionHandler`)
- [x] **Paginação** (Pageable em endpoints de listagem)
- [x] **Documentação Swagger** (`/swagger-ui/index.html`)
- [x] **Testes Automatizados** (MockMvc, DataJpaTest, SpringBootTest)
- [x] **Deploy em Nuvem** (Render com PostgreSQL)
- [x] **CI/CD** (GitHub Actions)

---

## 📚 Referências

- [Spring Boot 3.3 Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security + JWT](https://www.baeldung.com/spring-security-authentication-and-authorization)
- [Springdoc OpenAPI](https://springdoc.org/)
- [JPA Inheritance Strategies](https://www.baeldung.com/hibernate-inheritance)

---

## 👥 Integrantes

| Nome | RM | Função |
|------|-----|--------|
| Gabriel Gomes Mancera | 562279 | 
| Raphael Gomes Mancera | 562279 | 
| Bruno Vinicius Barbosa | 566366 | 
| João Victor Rebello de Santis | 555287 | 
| Guilherme de andrade Martini | 566087 | 

---

## 📄 Licença

Projeto acadêmico — FIAP Global Solution (Java Advanced).

---

**Status:** Em produção no Render  
**Última atualização:** Junho 2026  
**Repositório:** https://github.com/GomesMancera/OrbitalGuard-java
