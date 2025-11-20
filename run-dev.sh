#!/bin/bash
# ========================================
# 📄 run-dev.sh
# Script para rodar ambiente de desenvolvimento
# ========================================

echo "🚀 Iniciando ambiente de DESENVOLVIMENTO..."
echo ""

# Verifica se .env.dev existe
if [ ! -f .env.dev ]; then
    echo "⚠️  Arquivo .env.dev não encontrado!"
    echo "📝 Criando .env.dev com valores padrão..."
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
    echo ""
fi

# Roda o Docker Compose
echo "🐳 Subindo containers Docker..."
docker-compose --env-file .env.dev up --build

