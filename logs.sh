# ========================================
# 📄 logs.sh
# Script para ver logs
# ========================================

#!/bin/bash

echo "📋 Mostrando logs do backend..."
echo "   (Ctrl+C para sair)"
echo ""

docker-compose logs -f backend

