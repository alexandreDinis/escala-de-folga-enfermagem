# ========================================
# 📄 setup.sh
# Script de setup inicial
# ========================================

#!/bin/bash

echo "🎯 SETUP INICIAL - Escala de Folga"
echo "=================================="
echo ""

# Verifica Docker
echo "1️⃣ Verificando Docker..."
if ! command -v docker &> /dev/null; then
    echo "❌ Docker não encontrado!"
    echo "📥 Instale Docker: https://docs.docker.com/get-docker/"
    exit 1
fi
echo "✅ Docker instalado!"

# Verifica Docker Compose
echo ""
echo "2️⃣ Verificando Docker Compose..."
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose não encontrado!"
    exit 1
fi
echo "✅ Docker Compose instalado!"

# Cria .env.dev se não existe
echo ""
echo "3️⃣ Configurando ambiente..."
if [ ! -f .env.dev ]; then
    cat > .env.dev << 'EOF'
POSTGRES_DB=escala_folga_dev
POSTGRES_USER=dev_user
POSTGRES_PASSWORD=dev_pass
POSTGRES_PORT=5432
BACKEND_PORT=8080
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true
EOF
    echo "✅ Arquivo .env.dev criado!"
else
    echo "✅ Arquivo .env.dev já existe!"
fi

# Dá permissão aos scripts
echo ""
echo "4️⃣ Configurando scripts..."
chmod +x run-dev.sh run-prod.sh stop.sh logs.sh restart.sh clean.sh
echo "✅ Permissões configuradas!"

echo ""
echo "🎉 SETUP COMPLETO!"
echo ""
echo "📚 Próximos passos:"
echo "  1. Rode: ./run-dev.sh"
echo "  2. Aguarde alguns minutos"
echo "  3. Acesse: http://localhost:8080/swagger-ui.html"
echo ""
echo "📖 Comandos disponíveis:"
echo "  ./run-dev.sh   - Rodar desenvolvimento"
echo "  ./stop.sh      - Parar containers"
echo "  ./logs.sh      - Ver logs"
echo "  ./restart.sh   - Reiniciar backend"
echo "  ./clean.sh     - Limpar e recomeçar"
