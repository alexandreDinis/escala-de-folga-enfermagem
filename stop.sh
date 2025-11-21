# ========================================
# 📄 stop.sh
# Script para parar os containers
# ========================================

#!/bin/bash

echo "🛑 Parando containers..."
docker-compose down

echo ""
echo "✅ Containers parados!"
echo ""
echo "Para parar E remover dados do banco:"
echo "  docker-compose down -v"
