🏥 Escala de Folga – API de Gestão Inteligente para Enfermagem
Mostrar Imagem
Mostrar Imagem
Mostrar Imagem
Mostrar Imagem
Mostrar Imagem

🎯 Sistema inteligente para automatizar a geração de escalas de folgas de profissionais de enfermagem, garantindo cumprimento das regras trabalhistas e evitando erros humanos.


📘 Sobre o Projeto
Esta API foi criada para resolver um problema real do dia a dia de gestores de enfermagem: a criação manual de escalas de folgas.
💡 A História por Trás
Minha esposa é enfermeira, e eu via o tempo enorme que ela gastava elaborando escalas — conferindo datas, domingos, intervalos mínimos, distribuição por turnos e inúmeras regras específicas de cada hospital.
O resultado? Horas perdidas, planilhas confusas e risco constante de erro humano.
Para facilitar esse processo e devolver tempo de qualidade para os profissionais de saúde, desenvolvi esta API.

🎯 Objetivos
Garantir que o administrador da escala consiga, com poucos cliques:

✅ Criar e gerenciar folgas com segurança
✅ Respeitar todas as regras trabalhistas automaticamente
✅ Evitar sobrecarga de profissionais
✅ Distribuir colaboradores corretamente entre os turnos
✅ Ser avisado automaticamente quando alguma regra for violada
✅ Ter rastreabilidade completa de todas as mudanças


⚙️ Principais Funcionalidades
🗓️ 1. Controle Inteligente de Folgas
A API identifica automaticamente:

✅ Última folga registrada do colaborador
✅ Intervalo mínimo até a próxima folga permitida
✅ Se o colaborador já tem seu domingo de descanso no mês
✅ Se a nova folga viola alguma regra trabalhista

🚨 2. Sistema de Alertas Preventivos
Ao tentar registrar uma folga, a API valida e informa:
ValidaçãoDescrição⚠️ Domingo obrigatórioColaborador ainda não tem folga em domingo no mês⚠️ Intervalo mínimoNão respeitou o período mínimo entre folgas⚠️ Limite mensalTentativa de ultrapassar o número máximo de folgas⚠️ Desequilíbrio de turnoEscala desproporcional em determinado dia⚠️ Risco de insuficiênciaColaborador pode não completar suas folgas no mês
Esses alertas garantem segurança, justiça e precisão no planejamento!
👥 3. Distribuição Proporcional por Turno
A API calcula automaticamente:

Quantos colaboradores existem por turno (Manhã, Tarde, Noite)
Quantos podem folgar simultaneamente em cada dia
Se o turno ficará desequilibrado ou descoberto
Evita deixar plantões sem cobertura adequada

🧮 4. Regras Configuráveis por Instituição
Cada hospital tem suas próprias diretrizes. A API permite definir:
yaml✓ Número de folgas mensais (ex: 4 a 6 folgas)
✓ Quantidade mínima por semana
✓ Exigência de pelo menos 1 domingo
✓ Intervalos obrigatórios entre folgas
✓ Limite de folgas simultâneas por turno
Tudo é validado automaticamente!
📊 5. Auditoria Completa de Alterações
Cada mudança gera um registro detalhado:

📝 Criação de folga
✏️ Atualização/reprogramação
❌ Cancelamento/inativação
✅ Reativação

O sistema mantém rastreabilidade total para fins de conformidade e auditorias.

🧩 Tecnologias Utilizadas
CategoriaTecnologiaLinguagemJava 17FrameworkSpring Boot 3Banco de DadosPostgreSQL 15ORMSpring Data JPA + HibernateValidaçãoBean Validation (JSR 380)DocumentaçãoSpringDoc OpenAPI 3 (Swagger)BuildMaven 3.9+ContainerizaçãoDocker + Docker ComposeUtilitáriosLombok

🚀 Como Rodar o Projeto
📦 Opção 1: Com Docker ✅ (Recomendado)
1️⃣ Pré-requisitos

Docker instalado
Docker Compose instalado

2️⃣ Clone o repositório
bashgit clone https://github.com/seu-usuario/escala-folga.git
cd escala-folga
3️⃣ Configure as variáveis de ambiente
bash# Copie o exemplo
cp .env.example .env

# Edite com suas senhas
nano .env
Conteúdo do .env:
env# Banco de Dados
POSTGRES_DB=escala_folga
POSTGRES_USER=admin
POSTGRES_PASSWORD=sua_senha_forte_aqui  # ⚠️ MUDE!
POSTGRES_PORT=5432

# Backend
BACKEND_PORT=8080
SPRING_PROFILES_ACTIVE=docker
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true
4️⃣ Subir os containers
bash# Primeira vez (com build)
docker compose up --build -d

# Ver logs
docker compose logs -f backend

# Verificar status
docker compose ps
5️⃣ Acessar a aplicação

🌐 API: http://localhost:8080
📚 Swagger UI: http://localhost:8080/swagger-ui.html
📖 API Docs: http://localhost:8080/api-docs

6️⃣ Comandos úteis
bash# Parar containers (dados permanecem)
docker compose down

# Rebuild após mudanças no código
docker compose up --build -d

# Ver logs em tempo real
docker compose logs -f backend

# Reiniciar apenas o backend
docker compose restart backend

# Apagar tudo (⚠️ CUIDADO: remove dados!)
docker compose down -v

💻 Opção 2: Rodar Localmente (sem Docker)
1️⃣ Pré-requisitos

☕ JDK 17+
📦 Maven 3.9+
🐘 PostgreSQL 15+ rodando localmente

2️⃣ Configurar o banco de dados
sql-- Criar banco
CREATE DATABASE escala_folga;

-- Criar usuário
CREATE USER admin WITH PASSWORD 'admin123';
GRANT ALL PRIVILEGES ON DATABASE escala_folga TO admin;
3️⃣ Configurar application-dev.properties
properties# src/main/resources/application-dev.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/escala_folga
spring.datasource.username=admin
spring.datasource.password=admin123

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
4️⃣ Rodar o projeto
bashcd backend

# Limpar e compilar
mvn clean install

# Rodar com perfil dev
mvn spring-boot:run -Dspring-boot.run.profiles=dev

🗄️ Acessar o Banco de Dados
Via Docker:
bash# Entrar no container PostgreSQL
docker exec -it escala-folga-db psql -U admin -d escala_folga
Via psql local:
bashpsql -h localhost -p 5432 -U admin -d escala_folga
Comandos úteis dentro do psql:
sql\dt                           -- Listar todas as tabelas
\d colaborador               -- Ver estrutura da tabela colaborador
\d+ folga                    -- Ver estrutura detalhada da tabela folga
SELECT * FROM colaborador;   -- Listar colaboradores
SELECT * FROM folga WHERE status = 'APROVADA';
\q                           -- Sair

📁 Estrutura do Projeto
escala-folga/
├── .env                          # Variáveis de ambiente (não commitar!)
├── .env.example                  # Template de variáveis
├── docker-compose.yml            # Orquestração Docker
├── README.md
│
└── backend/
    ├── Dockerfile
    ├── .dockerignore
    ├── pom.xml
    │
    └── src/
        ├── main/
        │   ├── java/com/oroboros/EscalaDeFolga/
        │   │   ├── app/                    # Controllers, DTOs
        │   │   ├── domain/                 # Entidades, Services, Validações
        │   │   ├── infrastructure/         # Repositories, Configs
        │   │   └── util/                   # Helpers, Utilities
        │   │
        │   └── resources/
        │       ├── application.properties
        │       ├── application-dev.properties
        │       ├── application-docker.properties
        │       └── application-prod.properties
        │
        └── test/
            └── java/                       # Testes unitários e integração

🌐 Documentação da API
📚 Swagger UI (Interativo)
Acesse: http://localhost:8080/swagger-ui.html
📖 OpenAPI JSON
Acesse: http://localhost:8080/api-docs

🔹 Resumo dos Endpoints Principais
👤 Colaboradores
MétodoEndpointDescriçãoPOST/api/colaboradoresCriar novo colaboradorGET/api/colaboradoresListar todos os colaboradoresGET/api/colaboradores/{id}Buscar colaborador por IDPUT/api/colaboradores/{id}Atualizar dados do colaboradorDELETE/api/colaboradores/{id}Inativar colaborador
🗓️ Folgas
MétodoEndpointDescriçãoPOST/api/folgasSolicitar nova folgaGET/api/folgasListar folgas (com filtros)GET/api/folgas/{id}Ver detalhes de uma folgaPUT/api/folgas/{id}/aprovarAprovar folga pendentePUT/api/folgas/{id}/rejeitarRejeitar folgaDELETE/api/folgas/{id}Cancelar folga
📊 Escalas
MétodoEndpointDescriçãoPOST/api/escalas/gerarGerar escala automáticaGET/api/escalas/{mes}/{ano}Buscar escala por períodoGET/api/escalas/{id}Ver detalhes da escalaPUT/api/escalas/{id}/publicarPublicar escala

🧪 Testes
bash# Rodar todos os testes
mvn test

# Rodar com coverage
mvn test jacoco:report

# Ver relatório de cobertura
open target/site/jacoco/index.html

🤝 Como Contribuir

Fork o projeto
Crie uma branch para sua feature (git checkout -b feature/nova-funcionalidade)
Commit suas mudanças (git commit -m 'Add: nova funcionalidade')
Push para a branch (git push origin feature/nova-funcionalidade)
Abra um Pull Request


📝 Roadmap
✅ Implementado

 CRUD completo de colaboradores
 Sistema de validações de folgas
 Distribuição por turnos
 Alertas inteligentes
 Auditoria de mudanças
 Dockerização completa

🚧 Em Desenvolvimento

 Dashboard administrativo (frontend)
 Notificações via email
 Exportação para PDF/Excel
 Integração com sistemas de RH
 App mobile

💡 Planejado

 IA para sugestão automática de escalas
 Multi-tenancy (suporte a múltiplos hospitais)
 Relatórios analíticos avançados


📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

👨‍💻 Autor
Alexandre Dinis

💼 LinkedIn: seu-linkedin
🐙 GitHub: @seu-usuario
📧 Email: seu-email@exemplo.com


❤️Agradecimentos
Este projeto foi inspirado pela rotina desafiadora da minha esposa, enfermeira dedicada, e pela necessidade de tornar a gestão de escalas mais justa, segura e prática para todos os profissionais de saúde.
Dedicado a todos os profissionais de enfermagem que trabalham incansavelmente para cuidar de nós. 🏥💙

<div align="center">
⭐ Se este projeto te ajudou, deixe uma estrela! ⭐
</div>
