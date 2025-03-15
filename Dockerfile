# Etapa 1: Build da aplicação Java com Maven (usando JDK 17)
FROM maven:3.8-openjdk-17 AS builder

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo pom.xml e baixa as dependências
COPY pom.xml .

# Baixa as dependências do Maven
RUN mvn dependency:go-offline

# Copia o código fonte da aplicação
COPY src ./src

# Compila o JAR
RUN mvn clean package -DskipTests

# Etapa 2: Imagem para rodar a aplicação (usando JDK 17)
FROM openjdk:17-slim

# Cria o diretório para armazenar a aplicação
WORKDIR /app

# Copia o JAR compilado da etapa de build
COPY --from=builder /app/target/teal-0.0.2.jar /app/teal-foods.jar

# Expondo a porta que o Spring Boot irá rodar
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "teal-foods.jar"]
