@echo off
REM ========================================
REM SETUP INICIAL - Escala de Folga (Windows)
REM ========================================

echo.
echo ========================================
echo   SETUP INICIAL - Escala de Folga
echo ========================================
echo.

REM ========================================
REM 1. VERIFICAR DOCKER
REM ========================================
echo 1. Verificando Docker...
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERRO] Docker nao encontrado!
    echo Instale Docker Desktop: https://docs.docker.com/desktop/windows/install/
    pause
    exit /b 1
)
echo [OK] Docker instalado!

REM ========================================
REM 2. VERIFICAR DOCKER COMPOSE
REM ========================================
echo.
echo 2. Verificando Docker Compose...
docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERRO] Docker Compose nao encontrado!
    pause
    exit /b 1
)
echo [OK] Docker Compose instalado!

REM ========================================
REM 3. CRIAR .ENV
REM ========================================
echo.
echo 3. Configurando variaveis de ambiente...
if not exist .env (
    (
        echo # PostgreSQL
        echo POSTGRES_DB=escalafolga
        echo POSTGRES_USER=postgres
        echo POSTGRES_PASSWORD=postgres123
        echo POSTGRES_PORT=5432
        echo.
        echo # Backend
        echo BACKEND_PORT=8080
        echo SPRING_PROFILES_ACTIVE=docker
        echo SPRING_JPA_HIBERNATE_DDL_AUTO=update
        echo SPRING_JPA_SHOW_SQL=false
        echo.
        echo # Frontend
        echo FRONTEND_PORT=80
        echo VITE_API_URL=/api
    ) > .env
    echo [OK] Arquivo .env criado!
) else (
    echo [AVISO] Arquivo .env ja existe
)

REM ========================================
REM 4. CRIAR SCRIPTS AUXILIARES
REM ========================================
echo.
echo 4. Criando scripts auxiliares...

REM dev.bat
(
    echo @echo off
    echo echo Iniciando ambiente de desenvolvimento...
    echo docker-compose up -d
    echo echo.
    echo echo [OK] Servicos iniciados!
    echo echo.
    echo echo URLs:
    echo echo   Frontend: http://localhost
    echo echo   Backend:  http://localhost:8080
    echo echo   API Docs: http://localhost:8080/swagger-ui.html
    echo echo.
    echo pause
) > dev.bat

REM stop.bat
(
    echo @echo off
    echo echo Parando containers...
    echo docker-compose down
    echo echo [OK] Containers parados!
    echo pause
) > stop.bat

REM logs.bat
(
    echo @echo off
    echo docker-compose logs -f
) > logs.bat

REM clean.bat
(
    echo @echo off
    echo echo Limpando ambiente...
    echo docker-compose down -v
    echo docker system prune -f
    echo echo [OK] Ambiente limpo!
    echo pause
) > clean.bat

echo [OK] Scripts criados!

REM ========================================
REM CONCLUIDO
REM ========================================
echo.
echo ========================================
echo   SETUP COMPLETO!
echo ========================================
echo.
echo Proximos passos:
echo.
echo   1. Execute: dev.bat
echo   2. Aguarde 2 minutos
echo   3. Acesse: http://localhost
echo.
echo Comandos uteis:
echo   dev.bat    - Iniciar via Docker
echo   stop.bat   - Parar containers
echo   logs.bat   - Ver logs
echo   clean.bat  - Limpar tudo
echo.
pause
