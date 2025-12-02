# ==========================================
# ESTÁGIO 1: BUILD
# ==========================================
FROM node:20-alpine AS build

WORKDIR /app

# Copiar package files
COPY package*.json ./

# Instalar dependências
RUN npm ci --silent

# Copiar código fonte
COPY . .

# ✅ CRÍTICO: Passar variável de ambiente para o Vite durante o build
ARG VITE_API_BASE_URL=/api
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}

# Debug: Mostrar qual URL está sendo usada
RUN echo "🔧 Building with VITE_API_BASE_URL=${VITE_API_BASE_URL}"

# Build da aplicação
RUN npm run build

# ==========================================
# ESTÁGIO 2: PRODUÇÃO
# ==========================================
FROM nginx:1.25-alpine

# Copiar build do estágio anterior
COPY --from=build /app/dist /usr/share/nginx/html

# Copiar configuração do Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expor porta 80
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost/ || exit 1

# Iniciar Nginx
CMD ["nginx", "-g", "daemon off;"]
