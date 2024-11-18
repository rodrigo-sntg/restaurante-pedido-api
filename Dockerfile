# Use uma imagem base com JDK 17 (ou outra versão necessária)
FROM eclipse-temurin:17-jdk-alpine

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copie o arquivo JAR da sua aplicação para o diretório /app
COPY target/pedidos-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta que a aplicação Spring Boot usa
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]