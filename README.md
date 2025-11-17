# 🏥 Escala de Folga – API de Gestão Inteligente para Enfermagem
📘 Sobre o Projeto

API desenvolvida para automatizar a geração de escalas de folga para profissionais de enfermagem.
Ela nasceu da observação da rotina da minha esposa, enfermeira, que gastava horas criando escalas manualmente — o que me motivou a criar uma solução prática, segura e automática do Projeto


O resultado?
⏳ Horas perdidas
📄 Planilhas confusas
⚠️ Alto risco de erro humano

Para facilitar esse processo e devolver tempo de qualidade aos profissionais, desenvolvi esta API.



🎯 Objetivos

Com poucos cliques, o administrador consegue:

✅ Criar e gerenciar folgas com segurança

✅ Validar automaticamente regras trabalhistas

✅ Evitar sobrecarga de plantões

✅ Distribuir turnos proporcionalmente

✅ Receber alertas quando qualquer regra for violada

✅ Rastrear todas as alterações com auditoria

⚙️ Principais Funcionalidades
🗓️ Controle Inteligente de Folgas

A API identifica automaticamente:

Última folga do colaborador

Intervalo mínimo para a próxima folga

Se o colaborador já folgou no domingo

Se uma nova folga viola regras trabalhistas

🚨 Sistema de Alertas Preventivos
Validação	Descrição
⚠️ Domingo obrigatório	Colaborador sem domingo no mês
⚠️ Intervalo mínimo	Tenta folgar antes do permitido
⚠️ Limite mensal	Ultrapassa quantidade de folgas mensais
⚠️ Desequilíbrio	Turno ficaria desproporcional
⚠️ risco de insuficiência	Pode não completar folgas no mês
👥 Distribuição Proporcional por Turno

A API calcula:

Quantidade de colaboradores por turno

Quantos podem folgar no mesmo dia

Previne plantões descobertos

🧮 Regras Configuráveis por Hospital

Você pode configurar:

Número de folgas mensais

Intervalo mínimo entre folgas

Exigência de domingo

Limites de folgas simultâneas por turno

📊 Auditoria Completa

Cada ação gera um registro:

📝 Criação

✏️ Atualização

❌ Cancelamento

🔁 Reativação


#
#
#
# ⚠️ Status Atual do Projeto ⚠️

Este é um projeto em **Desenvolvimento Ativo (Work In Progress)**. 
Tanto o **backend (API)** quanto o **frontend (Dashboard)** estão sendo construídos. 
A API já possui funcionalidades robustas, mas o sistema ainda não é recomendado para uso em produção. Por favor, confira o [Roadmap](#📝-roadmap) para detalhes sobre as funcionalidades em andamento.
#
#
#
## Stack utilizada


**Back-end:** 

Java 17

Spring Boot 3

Maven 3.9

PostgreSQL 15

Docker e Docker Compose

## 🚀 Como Rodar o Projeto

📦 Opção 1 — Rodando com Docker (Recomendado)

1️⃣ Clonar o repositório

```bash
  git clone https://github.com/alexandreDinis/escala-de-folga-enfermagem

  ```

Entre no diretório do projeto

```bash
  cd EscalaDeFolga/backend
```

🐋 Subir com Docker

```bash
  cp .env.example .env
  nano .env
```

Exemplo:

Inicie o servidor

```bash
  POSTGRES_DB=escala_folga
  POSTGRES_USER=admin
  POSTGRES_PASSWORD=admin123
  POSTGRES_PORT=5432

  BACKEND_PORT=8080
  SPRING_PROFILES_ACTIVE=docker
```

2️⃣ Subir tudo

```bash
  docker compose up --build -d
```

3️⃣ Ver logs

```bash
  docker compose logs -f backend
```

🗄️ Acessar Banco de Dados Via Docker

```bash
  docker exec -it escala-folga-db psql -U admin -d escala_folga
```

🗄️ Comandos úteis no psql:

```bash
  \dt
  \d colaborador
  \d+ folga
  SELECT * FROM colaborador;
  \q
```

💻 Rodar Localmente (sem Docker)

1️⃣ Configurar application-dev.properties

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/escala_folga
spring.datasource.username=admin
spring.datasource.password=admin123

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

2️⃣ Rodar o backend


```bash
cd backend
mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

📁 Estrutura do Projeto

```bash
escala-folga/
├── docker-compose.yml
├── .env
├── README.md
│
└── backend/
    ├── Dockerfile
    ├── .dockerignore
    ├── pom.xml
    │
    └── src/
        ├── main/java/com/oroboros/EscalaDeFolga/
        │   ├── app/
        │   ├── domain/
        │   ├── infrastructure/
        │   └── util/
        │
        └── resources/

```

🌐 Documentação da API
📚 Swagger

👉 http://localhost:8080/swagger-ui.html

📘 OpenAPI JSON

👉 http://localhost:8080/api-docs



🤝 Como Contribuir

```bash
# Criar branch
git checkout -b feature/minha-feature

# Commitar mudanças
git commit -m "feat: minha nova funcionalidade"

# Enviar ao GitHub
git push origin feature/minha-feature
```

## Autores

- [@AlexandreDinis](https://www.github.com/AlexandreDinis)

👨‍💻 Autor

Alexandre Dinis

💼 LinkedIn: https://www.linkedin.com/in/alexandredinis/


Agradecimentos
Este projeto foi inspirado pela rotina desafiadora da minha esposa, enfermeira dedicada, e pela necessidade de tornar a gestão de escalas mais justa, segura e prática para todos os profissionais de saúde.
Dedicado a todos os profissionais de enfermagem que trabalham incansavelmente para cuidar de nós. 🏥💙




              ⭐ Se este projeto te ajudou, deixe uma estrela! ⭐




