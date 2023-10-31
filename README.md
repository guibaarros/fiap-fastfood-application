# fastfood-fiap-postech

- [fastfood-fiap-postech](#fastfood-fiap-postech)
  - [Objetivo](#objetivo)
  - [Documentação](#documentação)
    - [Ferramentas utilizadas](#ferramentas-utilizadas)
  - [Executando a aplicação localmente](#executando-a-aplicação-localmente)
    - [Ferramentas necessárias:](#ferramentas-necessárias)
      - [Swagger](#swagger)
      - [Parando a execução](#parando-a-execução)
      - [pgAdmin](#pgadmin)
    - [Carga inicial de dados](#carga-inicial-de-dados)

## Objetivo

Repositório para o desenvolvimento dos Tech Challenges da Postech FIAP em Arquitetura de Software.

## Documentação
### Ferramentas utilizadas
- [Miro](https://miro.com)
- [Egon.io](https://egon.io)
- [Swagger Editor](https://editor.swagger.io)

O repositório possui o diretório [docs](https://github.com/guibaarros/fastfood-fiap-postech/tree/main/docs) onde é versionada toda documentação do projeto. Atualmente, existem as seguintes documentações:

- [Event Storytelling.md](https://github.com/guibaarros/fastfood-fiap-postech/blob/main/docs/Event%20Storytelling.md) - Descrição dos fluxos de negócio de identificação do cliente, pedido, pagamento, preparação e entrega do pedido ao cliente.

Para o Egon.io: 
- [Domain Storytelling - Sistema de Pedidos - Lanchonete - Pedido e Pagamento.dst](https://github.com/guibaarros/fastfood-fiap-postech/blob/main/docs/Domain%20Storytelling%20-%20Sistema%20de%20Pedidos%20-%20Lanchonete%20-%20Pedido%20e%20Pagamento.dst) - Diagrama de Domain Storytelling do fluxo de pedido e pagamento.
- [Domain Storytelling - Sistema de Pedidos - Lanchonete - Preparação e Entrega.dst](https://github.com/guibaarros/fastfood-fiap-postech/blob/main/docs/Domain%20Storytelling%20-%20Sistema%20de%20Pedidos%20-%20Lanchonete%20-%20Prepara%C3%A7%C3%A3o%20e%20Entrega.dst) - Diagrama de Domain Storytelling do fluxo de preparação e entrega do pedido.

Collection do Postman:
- [FIAP - Postech - Lanchonete.postman_collection.json](https://github.com/guibaarros/fastfood-fiap-postech/blob/main/docs/FIAP%20-%20Postech%20-%20Lanchonete.postman_collection.json)

Documentação OpenAPI para o Swagger:
- [OpenAPI-Swagger.yml](https://github.com/guibaarros/fastfood-fiap-postech/blob/main/docs/OpenAPI-Swagger.yml)


Alem disso, o projeto foi documentado também no Miro, com um quadro contendo:
- O Event Storytelling;
- Os diagramas de Domain Storytelling;
- O dicionário da Linguagem Ubíqua para os subdomínios de Gestão de Pedidos, Gestão de Clientes e Gestão de Produtos;
- Definição dos subdomínios;
- Context Mapping com os Bounded Contexts, assim como as ACLs, OHS e PLs;
- Event Storming dos fluxos de Identificação do Cliente, Realização do Pedido e Pagamento, Envio para preparo, Preparação e Entrega


O quadro do Miro está público e pode ser acessado [aqui](https://miro.com/app/board/uXjVNVAZsgw=/)

## Executando a aplicação localmente
### Ferramentas necessárias:
- docker
- docker-compose
- Postman
- OpenJDK 17

Para execução da aplicação, na raiz do projeto, abra um terminal e execute os comandos:

1. Compile o projeto:

        ./gradlew clean build

2. Crie a imagem docker da aplicação:
    
        docker build -t fastfood-fiap-postech .  

3. Execute o docker-compose para subir a aplicação e o banco de dados:
        
        docker-compose up -d

#### Swagger

A aplicação está exposta na porta 8085 e seu Swagger pode ser acessado pela url:

    http://localhost:8085/swagger-ui/index.html

#### Parando a execução

Para parar a execução da aplicação, basta executar no terminal na raiz do projeto:

    docker-compose down

Esse comando irá parar a execução dos containers e excluí-los.

#### pgAdmin

O docker-compose também executa uma instância do pgadmin, interface gráfica para acesso à bases Postgres. Para utilizar, basta acessar a URL:

    http://localhost:15432/

    Login: admin@admin.com
    Password: pgadmin1234

Para acesso à base, é necessário configurar um servidor com os seguintes dados:
- hostname/address: postgres_fiap_fastfood
- Port: 5432
- username: postgres
- password: abcd1234

### Carga inicial de dados

Ao executar o ```docker-compose up -d```, o container do postgres executa os comandos presentes no arquivo [initial_data.sql](https://github.com/guibaarros/fastfood-fiap-postech/blob/development/.database/initial_data.sql), criando assim as tabelas necessárias para a execução da aplicação e uma pequena massa de dados para inicio dos testes. Nesse script são executados:
- Criação da tabela de clientes;
- Criação da tabela de pedidos;
- Criação da tabela de produtos;
- Criação da tabela de imagens dos produtos;
- Criação da tabela de produtos dos pedidos;
- Inclusão de 5 clientes;
- Inclusão de 11 produtos;
- Inclusão de 3 pedidos sendo eles:
  - 1 pago e finalizado;
  - 1 pago e aguardando preparo;
  - 1 aguardando pagamento;
- Inclusão dos produtos dos pedidos acima;

A collection do Postman levou em consideração esses dados cadastrados nessa carga inicial;