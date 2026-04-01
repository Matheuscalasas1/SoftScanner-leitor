# SoftScanner-leitor
Aplicação web funcional para leitura de SERIAL de rádio através do código de barras utilizando scanner estilo de supermercado

🚀 Funcionalidades 📊 Controle de Rádios: Registro de saída e retorno de equipamentos

🔍 Scanner de Código de Barras: Leitura automática via campo de texto

🏢 Gestão de Empresas: Registro de empresa, responsável e telefone

📈 Histórico Completo: Rastreabilidade de todos os movimentos

📤 Exportação Excel: Geração de relatórios em formato XLSX

🔄 Gestão de Duplicatas: Sistema inteligente para evitar registros duplicados

🗑️ Exclusão Segura: Remoção de registros por ID ou serial

📱 Interface Responsiva: Design clean e intuitivo

🛠️ Tecnologias Utilizadas Backend: Java 21, Spring Boot 3.5.10

Banco de Dados: MySQL 8.0+

ORM: Hibernate 6.6.41, Spring Data JPA

Frontend: Thymeleaf, HTML5, CSS3, JavaScript

Bibliotecas: Apache POI (exportação Excel), HikariCP (pool de conexões)

Build: Maven

📋 Pré-requisitos Antes de executar o projeto, certifique-se de ter instalado:

Java JDK 21 

MySQL 8.0 

Maven 3.6+

Git

IDE: VS Code

⚙️ Configuração do Banco de Dados

Configuração do MySQL sql -- Conectar ao MySQL mysql -u root -p -- Criar banco de dados CREATE DATABASE IF NOT EXISTS softscanner_db; USE softscanner_db;

-- Verificar usuário SHOW GRANTS FOR 'root'@'localhost';

-- Se necessário, criar usuário específico CREATE USER 'app_user'@'localhost' IDENTIFIED BY 'senha_segura'; GRANT ALL PRIVILEGES ON softscanner_db.* TO 'app_user'@'localhost'; FLUSH PRIVILEGES; 2. Configuração da Aplicação Edite o arquivo src/main/resources/application.properties:

properties

Configurações do MySQL spring.datasource.url=jdbc:mysql://localhost:3306/softscanner_db?useSSL=false&serverTimezone=UTC spring.datasource.username=root spring.datasource.password=sua_senha

Configurações JPA/Hibernate spring.jpa.hibernate.ddl-auto=update spring.jpa.show-sql=true 🚀 Executando a Aplicação Método 1: Via IDE Clone o repositório:

bash git clone https://github.com/seu-usuario/softscanner.git cd softscanner Importe como projeto Maven na sua IDE

Execute a classe SoftApplication.java

Método 2: Via Terminal bash

Navegue até a pasta do projeto cd softscanner

Compilar o projeto mvn clean compile

Executar a aplicação mvn spring-boot:run Método 3: Executar JAR bash

Gerar JAR executável mvn clean package

Executar JAR java -jar target/softscanner-1.0.0.jar 🌐 Acesso à Aplicação Após iniciar a aplicação, acesse:

Interface Web: http://localhost:8080/home

API REST: http://localhost:8080/api

📡 API Endpoints POST /api/ler Registra leitura de serial

bash curl -X POST "http://localhost:8080/api/ler?serial=ABC123&empresa=Empresa+Exemplo&responsavel=João+Silva&telefone=11999999999" GET /api/historico/{serial} Busca histórico por serial

bash curl "http://localhost:8080/api/historico/ABC123" GET /api/exportar-excel Exporta dados para Excel

bash curl -OJ "http://localhost:8080/api/exportar-excel" DELETE /api/excluir/{id} Exclui registro por ID

bash curl -X DELETE "http://localhost:8080/api/excluir/1" DELETE /api/excluir-serial/{serial} Exclui todos os registros de um serial

bash curl -X DELETE "http://localhost:8080/api/excluir-serial/ABC123" POST /api/limpar-duplicatas Remove registros duplicados EM USO
