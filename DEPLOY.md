# Guia de Deploy - OrbitalGuard no Render

## Fase 0 — Publicar no GitHub

Repositorio: https://github.com/GomesMancera/OrbitalGuard-java

**Opcao A (recomendada):** abra o **cmd.exe**, va ate a pasta do projeto e execute:

```bat
cd /d "d:\Java novo"
push-github.bat
```

**Opcao B (manual):**

```bat
cd /d "d:\Java novo"
git init
git add .
git commit -m "feat: OrbitalGuard API - FIAP Global Solution"
git branch -M main
git remote add origin https://github.com/GomesMancera/OrbitalGuard-java.git
git push -u origin main
```

> Se pedir senha, use um [Personal Access Token](https://github.com/settings/tokens) do GitHub.

## Checklist pre-deploy

- [ ] `mvn test` passando localmente (ou CI no GitHub Actions)
- [ ] Codigo commitado e enviado para repositorio publico no GitHub
- [ ] `JWT_SECRET` gerado (minimo 32 caracteres aleatorios)

## Pre-requisitos

- Conta no [Render](https://render.com)
- Repositorio GitHub: https://github.com/GomesMancera/OrbitalGuard-java

## Passo a passo

### 1. Criar banco PostgreSQL

1. Acesse o [dashboard do Render](https://dashboard.render.com)
2. Clique em **New +** > **PostgreSQL**
3. Configuracao:
   - **Name:** `orbitalguard-db`
   - **Database:** `orbitalguard`
   - **User:** `orbitalguard`
   - **Plan:** Free
4. Clique **Create Database**
5. Na aba **Info**, copie a **External Database URL** (`postgres://...`)

### 2. Criar Web Service (Docker)

1. **New +** > **Web Service**
2. Conecte sua conta GitHub e selecione `GomesMancera/OrbitalGuard-java`
3. Configuracoes:

| Campo | Valor |
|-------|-------|
| Name | `orbitalguard-api` |
| Region | Oregon (ou mais proxima) |
| Branch | `main` |
| Runtime | **Docker** |
| Instance type | Free |
| Health Check Path | `/actuator/health` |

> Com **Docker**, nao preencha Build/Start Command — o [`Dockerfile`](Dockerfile) ja compila e inicia a aplicacao.

### 3. Variaveis de ambiente

Na aba **Environment** do Web Service, adicione:

| Variavel | Valor |
|----------|-------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `JWT_SECRET` | string aleatoria com **32+ caracteres** |
| `DATABASE_URL` | External Database URL do PostgreSQL |
| `DATABASE_USERNAME` | `orbitalguard` |
| `DATABASE_PASSWORD` | senha gerada pelo Render (aba Info do banco) |

`PORT` e injetada automaticamente pelo Render.

**Gerar JWT_SECRET (PowerShell ou cmd):**

```bat
powershell -Command "[guid]::NewGuid().ToString('N') + [guid]::NewGuid().ToString('N')"
```

### 4. Deploy e validacao

1. Clique **Create Web Service** e aguarde o build (~5–10 min na 1a vez)
2. URL esperada: `https://orbitalguard-api.onrender.com`
3. Teste:
   - `https://orbitalguard-api.onrender.com/actuator/health` → `{"status":"UP"}`
   - `https://orbitalguard-api.onrender.com/swagger-ui/index.html`

**Auth em producao:** o admin seed (`admin@orbitalguard.com`) so existe no profile `dev`. Em prod, crie o primeiro usuario com `POST /api/v1/auth/register` e faca login em seguida.

### 5. Atualizar README

Substitua os placeholders no `README.md`:

- Deploy: `https://orbitalguard-api.onrender.com`
- Swagger: `https://orbitalguard-api.onrender.com/swagger-ui/index.html`
- Links dos videos (YouTube/Drive)

### 6. Validacao pos-deploy

```bash
curl https://orbitalguard-api.onrender.com/actuator/health

curl -X POST https://orbitalguard-api.onrender.com/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Operador\",\"email\":\"operador@orbitalguard.com\",\"senha\":\"senha123\"}"

curl -X POST https://orbitalguard-api.onrender.com/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"operador@orbitalguard.com\",\"senha\":\"senha123\"}"
```

Abra o Swagger em producao e teste CRUD com token JWT.

---

### Deploy via Blueprint (alternativa)

O arquivo `render.yaml` na raiz permite deploy automatico (banco + API):

1. No Render: **New +** > **Blueprint**
2. Conecte o repositorio
3. O Render criara o banco e o web service automaticamente
