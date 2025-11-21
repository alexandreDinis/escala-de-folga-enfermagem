# ========================================
# 📄 clean.sh
# Script para limpar tudo e recomeçar
# ========================================

#!/bin/bash

echo "🧹 LIMPEZA TOTAL"
echo "⚠️  Isso vai apagar TODOS os dados do banco!"
echo ""
read -p "Tem certeza? (digite 'sim'): " confirm

if [ "$confirm" != "sim" ]; then
    echo "❌ Cancelado!"
    exit 0
fi

echo ""
echo "🛑 Parando containers..."
docker-compose down -v

echo "🗑️  Removendo imagens antigas..."
docker image prune -f

echo "🐳 Subindo tudo de novo..."
docker-compose --env-file .env.dev up --build
