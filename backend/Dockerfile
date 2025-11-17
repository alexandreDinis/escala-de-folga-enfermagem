# Etapa 1: Build da aplicação
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copia apenas pom.xml primeiro (cache de dependências)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila o projeto (pula testes para build mais rápido)
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final (menor)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Cria usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia o JAR da etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8080

# Configurações de JVM otimizadas
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando para iniciar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
