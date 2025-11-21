# ========================================
# 📄 restart.sh
# Script para reiniciar apenas o backend
# ========================================

#!/bin/bash

echo "🔄 Reiniciando backend..."
docker-compose restart backend

echo ""
echo "✅ Backend reiniciado!"
echo "📋 Ver logs: ./logs.sh"
