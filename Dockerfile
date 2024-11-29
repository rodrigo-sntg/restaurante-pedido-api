# Etapa de build
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copia os arquivos necessários para o build
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

# Adiciona permissão de execução para o Maven Wrapper
RUN chmod +x mvnw

# Desabilita MAVEN_CONFIG e executa o Maven para limpar, instalar e empacotar o aplicativo sem rodar os testes
RUN unset MAVEN_CONFIG && ./mvnw clean install -DskipTests && ./mvnw package -DskipTests

# Etapa de execução
FROM openjdk:17

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o JAR da etapa de build para a etapa de execução
COPY --from=build /app/target/pedidos-*.jar /app/app.jar

# Exponha a porta da aplicação
EXPOSE 8080

# Comando de inicialização da aplicação
CMD ["java", "-jar", "app.jar"]