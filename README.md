# Aplicação de Serviço de Pedidos

Este é um serviço de pagamento desenvolvido em Spring Boot, O projeto é modular, permitindo escalabilidade e fácil integração com outros componentes.

## Requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- Docker (para testes com containers, opcional)

1. **Clone o repositório**:
    ```bash
    git clone https://github.com/seu-usuario/repositorio.git
    cd repositorio
    ```

2. **Instale as dependências**:
    ```bash
    ./mvnw clean install
    ```

3. **Configuração de Variáveis de Ambiente**:
   Crie um arquivo `.env` com as variáveis necessárias:
   ```properties
   PRODUTO_API_URL=rota do microserviço de produtos
   ```

4. **Rodando a Aplicação**:
    ```bash
    ./mvnw spring-boot:run
    ```

## Configuração

A configuração é feita principalmente através do arquivo `.properties`, onde você define as variáveis de ambiente para o token de autenticação e URL da API do PagSeguro.

## Testes

Para rodar os testes unitários e de integração:

```bash
./mvnw test
```

### Cobertura de Código

A aplicação usa Jacoco para medir a cobertura dos testes. Para gerar o relatório:

```bash
./mvnw test jacoco:report
```

O relatório será gerado em `target/site/jacoco/index.html`.


## Contribuição

Contribuições são bem-vindas! Para contribuir, siga as etapas abaixo:

1. Faça um fork do projeto.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`).
3. Comite suas mudanças (`git commit -m 'Adiciona nova feature'`).
4. Faça o push para a branch (`git push origin feature/nova-feature`).
5. Abra um Pull Request.

## Licença

Este projeto é licenciado sob a licença MIT.