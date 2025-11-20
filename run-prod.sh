#!/bin/bash
echo "🚀 Iniciando ambiente de PRODUÇÃO..."

docker compose down
docker compose up --build -d

echo "🎯 Backend rodando em: http://localhost:8080"
