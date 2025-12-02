#!/bin/bash

# ========================================
# 🚀 SETUP INICIAL - Escala de Folga
# Sistema de Gestão de Escalas de Enfermagem
# ========================================

echo "🎯 SETUP INICIAL - Escala de Folga"
echo "===================================="
echo ""

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# ========================================
# 1️⃣ VERIFICAR DOCKER
# ========================================
echo "1️⃣ Verificando Docker..."
if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker não encontrado!${NC}"
    echo "📥 Instale Docker: https://docs.docker.com/get-docker/"
    exit 1
fi
DOCKER_VERSION=$(docker --version)
echo -e "${GREEN}✅ Docker instalado: ${DOCKER_VERSION}${NC}"

# ========================================
# 2️⃣ VERIFICAR DOCKER COMPOSE
# ========================================
echo ""
echo "2️⃣ Verificando Docker Compose..."
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}❌ Docker Compose não encontrado!${NC}"
    echo "📥 Instale Docker Compose: https://docs.docker.com/compose/install/"
    exit 1
fi
COMPOSE_VERSION=$(docker-compose --version)
echo -e "${GREEN}✅ Docker Compose instalado: ${COMPOSE_VERSION}${NC}"

# ========================================
# 3️⃣ CONFIGURAR AMBIENTE
# ========================================
echo ""
echo "3️⃣ Configurando variáveis de ambiente..."

if [ ! -f .env ]; then
    cat > .env << 'ENVEOF'
# ==========================================
# VARIÁVEIS DE AMBIENTE - DOCKER
# ==========================================

# PostgreSQL
POSTGRES_DB=escalafolga
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres123
POSTGRES_PORT=5432

# Backend
BACKEND_PORT=8080
SPRING_PROFILES_ACTIVE=docker
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false

# Frontend
FRONTEND_PORT=80
VITE_API_URL=/api
ENVEOF
    echo -e "${GREEN}✅ Arquivo .env criado!${NC}"
else
    echo -e "${YELLOW}⚠️  Arquivo .env já existe (mantendo configurações atuais)${NC}"
fi

# ========================================
# 4️⃣ CONFIGURAR FRONTEND
# ========================================
echo ""
echo "4️⃣ Configurando frontend..."

# .env.development (IDE local)
if [ ! -f frontend/.env.development ]; then
    cat > frontend/.env.development << 'ENVEOF'
# Desenvolvimento Local (npm run dev)
# Backend rodando localmente na porta 8080
VITE_API_BASE_URL=http://localhost:8080/api
ENVEOF
    echo -e "${GREEN}✅ frontend/.env.development criado!${NC}"
else
    echo -e "${YELLOW}⚠️  frontend/.env.development já existe${NC}"
fi

# .env.production (Docker)
if [ ! -f frontend/.env.production ]; then
    cat > frontend/.env.production << 'ENVEOF'
# Produção/Docker (npm run build)
# Usa proxy do Nginx (/api → backend:8080/api)
VITE_API_BASE_URL=/api
ENVEOF
    echo -e "${GREEN}✅ frontend/.env.production criado!${NC}"
else
    echo -e "${YELLOW}⚠️  frontend/.env.production já existe${NC}"
fi

# ========================================
# 5️⃣ VERIFICAR NODE/NPM (OPCIONAL)
# ========================================
echo ""
echo "5️⃣ Verificando Node.js (opcional para dev local)..."
if command -v node &> /dev/null; then
    NODE_VERSION=$(node --version)
    NPM_VERSION=$(npm --version)
    echo -e "${GREEN}✅ Node.js: ${NODE_VERSION}${NC}"
    echo -e "${GREEN}✅ npm: ${NPM_VERSION}${NC}"
else
    echo -e "${YELLOW}⚠️  Node.js não encontrado (OK se usar apenas Docker)${NC}"
fi

# ========================================
# 6️⃣ VERIFICAR JAVA/MAVEN (OPCIONAL)
# ========================================
echo ""
echo "6️⃣ Verificando Java/Maven (opcional para dev local)..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1)
    echo -e "${GREEN}✅ Java: ${JAVA_VERSION}${NC}"
else
    echo -e "${YELLOW}⚠️  Java não encontrado (OK se usar apenas Docker)${NC}"
fi

if command -v mvn &> /dev/null; then
    MVN_VERSION=$(mvn --version | head -n 1)
    echo -e "${GREEN}✅ Maven: ${MVN_VERSION}${NC}"
else
    echo -e "${YELLOW}⚠️  Maven não encontrado (OK se usar apenas Docker)${NC}"
fi

# ========================================
# 7️⃣ CRIAR SCRIPTS AUXILIARES
# ========================================
echo ""
echo "7️⃣ Criando scripts auxiliares..."

# Script de desenvolvimento
cat > dev.sh << 'DEVEOF'
#!/bin/bash
echo "🚀 Iniciando ambiente de desenvolvimento..."
docker-compose up -d
echo ""
echo "✅ Serviços iniciados!"
echo ""
echo "🌐 URLs:"
echo "  Frontend: http://localhost"
echo "  Backend:  http://localhost:8080"
echo "  API Docs: http://localhost:8080/swagger-ui.html"
echo ""
echo "📊 Ver logs: docker-compose logs -f"
DEVEOF

# Script de logs
cat > logs.sh << 'LOGSEOF'
#!/bin/bash
docker-compose logs -f
LOGSEOF

# Script de parar
cat > stop.sh << 'STOPEOF'
#!/bin/bash
echo "⏹️  Parando containers..."
docker-compose down
echo "✅ Containers parados!"
STOPEOF

# Script de limpar
cat > clean.sh << 'CLEANEOF'
#!/bin/bash
echo "🧹 Limpando ambiente..."
docker-compose down -v
docker system prune -f
echo "✅ Ambiente limpo!"
CLEANEOF

# Dar permissões
chmod +x setup.sh dev.sh logs.sh stop.sh clean.sh

echo -e "${GREEN}✅ Scripts auxiliares criados!${NC}"

# ========================================
# 8️⃣ TESTAR CONEXÃO DOCKER
# ========================================
echo ""
echo "8️⃣ Testando conexão com Docker..."
if docker ps &> /dev/null; then
    echo -e "${GREEN}✅ Docker está rodando!${NC}"
else
    echo -e "${RED}❌ Docker não está rodando!${NC}"
    echo "   Inicie o Docker e rode ./setup.sh novamente"
    exit 1
fi

# ========================================
# ✅ SETUP COMPLETO
# ========================================
echo ""
echo -e "${GREEN}🎉 SETUP COMPLETO!${NC}"
echo ""
echo "📚 Próximos passos:"
echo ""
echo "  ${BLUE}Para rodar VIA DOCKER:${NC}"
echo "    1. ./dev.sh                    # Iniciar todos os serviços"
echo "    2. Aguardar ~2 minutos        # Backend inicializando"
echo "    3. Abrir http://localhost      # Frontend"
echo ""
echo "  ${BLUE}Para rodar VIA IDE (desenvolvimento):${NC}"
echo "    Terminal 1:"
echo "      cd backend"
echo "      mvn spring-boot:run"
echo ""
echo "    Terminal 2:"
echo "      cd frontend"
echo "      npm install"
echo "      npm run dev"
echo ""
echo "    Acessar: http://localhost:5173"
echo ""
echo "📖 Comandos úteis:"
echo "  ./dev.sh     - Iniciar via Docker"
echo "  ./stop.sh    - Parar containers"
echo "  ./logs.sh    - Ver logs"
echo "  ./clean.sh   - Limpar tudo e recomeçar"
echo ""
echo "📊 Monitorar:"
echo "  docker-compose ps                # Status dos containers"
echo "  docker-compose logs -f backend   # Logs do backend"
echo "  docker-compose logs -f frontend  # Logs do frontend"
echo ""
