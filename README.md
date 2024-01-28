# fastfood-fiap-postech

- [fastfood-fiap-postech](#fastfood-fiap-postech)
  - [Objetivo](#objetivo)
  - [Documentação](#documentação)
    - [Ferramentas utilizadas](#ferramentas-utilizadas)
  - [Executando a aplicação localmente](#executando-a-aplicação-localmente)
    - [Executando no Kubernetes](#executando-no-kubernetes)
      - [Ferramentas necessárias](#ferramentas-necessárias)
      - [Subindo o banco de dados Postgres](#subindo-o-banco-de-dados-postgres)
      - [Subindo o módulo de metrics do node](#subindo-o-módulo-de-metrics-do-node)
      - [Subindo a aplicação](#subindo-a-aplicação)
      - [Swagger](#swagger)
      - [Parando a execução](#parando-a-execução)
      - [pgAdmin](#pgadmin)
      - [Carga inicial de dados](#carga-inicial-de-dados)
    - [Executando via docker-compose](#executando-via-docker-compose)
      - [Ferramentas necessárias](#ferramentas-necessárias-1)
      - [Swagger](#swagger-1)
      - [Parando a execução](#parando-a-execução-1)
      - [pgAdmin](#pgadmin-1)
      - [Carga inicial de dados](#carga-inicial-de-dados-1)


## Objetivo

Repositório para o desenvolvimento dos Tech Challenges da Postech FIAP em Arquitetura de Software.
Grupo 75
Integrante: Guilherme Henrique Rafael Junqueira de Barros (guilhermehr00@gmail.com)

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
Atualmente é possível executar a aplicação localmente através do docker-compose ou aplicar os arquivos de configuração do Kubernetes em um singlenode rodando localmente.

### Executando no Kubernetes
<details open>
<summary>Instruções</summary>

####  Ferramentas necessárias
- Docker Desktop com Kubernetes ativado (ou qualquer outro Kubernetes singlenode rodando localmente)
- docker
- kubectl
- Postman

Para execução da aplicação, na raiz do projeto, abra um terminal e execute os comandos:

#### Subindo o banco de dados Postgres
```
kubectl apply -f .\deploy\postgres\postgres-pv.yml
kubectl apply -f .\deploy\postgres\postgres-pvc.yml
kubectl apply -f .\deploy\postgres\postgres-deployment.yml
kubectl apply -f .\deploy\postgres\postgres-service.yml
```
Os comandos acima criam, respectivamente, o Persistent Volume, Persistent Volume Claim, Deployment e Service do banco. <br>
O service expõe a porta 5432 apenas para dentro do cluster, garantindo que não é possível acessar diretamente o banco de dados de fora do cluster.

#### Subindo o módulo de metrics do node
```
kubectl apply -f .\deploy\metrics\metrics.yml
```
Esse módulo de métricas é necessário para que o HPA da aplicação consiga coletar as métricas de uso de recursos (CPU e Memória) do node e do pod e assim conseguir escalar a aplicação horizontalmente de acordo com a configuração de escalabilidade definido no .\deploy\app\app-hpa.yml.

#### Subindo a aplicação
```
kubectl apply -f .\deploy\app\app-deployment.yml
kubectl apply -f .\deploy\app\app-service.yml  
kubectl apply -f .\deploy\app\app-hpa.yml  
```
Os comandos acima criam, respectivamente, o Deployment, o Service e o Horizontal Pod Autoscaling da aplicação. <br>
O **Deployment** da aplicação contém as configurações de:
- Um POD inicialmente executando a aplicação:
  - replicas: 1
- Imagem presente no DockerHub a ser executada dentro do POD:
  - guibaarros/fastfood-fiap-postech:1.0.2
- Variáveis de ambiente de configuração de acesso ao banco de dados
    - DB_HOSTNAME;
    - DB_USER;
    - DB_PASSWORD;
    - DB_PORT;
- Verificação de saúde da aplicação através do liveness e readiness probes, configurados para:
  - Utilizar o endpoint actuator/health na porta 8085;
  - Verificar a saúde a cada 60 segundos;
  - Aguardar 60 segundos após o início do pod para iniciar a verificação;
  - Considerar a execução falha após 3 verificações consecutivas mal sucedidas; 
- Recursos de CPU e memória limitados a:
  - Memória: 512Mi
  - CPU: 300m
- Portas mapeadas:
  - containerPort: 8085

O **HPA** está configurado para manter:
- Máximo de 4 réplicas;
- Mínimo de 1 réplica;

E o gatilho para a escalabilidade é:
- CPU: utilização média de 40%;

O **Service** está habilitando o acesso à aplicação através de um NodePort, mapeando a porta de acesso de fora do cluster **32001** para a porta **8085** do container.

Após a execução dos comandos acima, a aplicação estará acessível através da porta 32001 depois da verificação da saúde da app pelos probes.

#### Swagger

A aplicação está exposta na porta 32001 e seu Swagger pode ser acessado pela url:

    http://localhost:32001/swagger-ui/index.html

#### Parando a execução

Para parar a execução da aplicação, basta executar no terminal na raiz do projeto:
```
kubectl delete -f .\deploy\app\app-deployment.yml
kubectl delete -f .\deploy\app\app-service.yml  
kubectl delete -f .\deploy\app\app-hpa.yml 
```
Os comandos acima irão excluir o Deployment, Service e HPA da aplicação e consequentemente o Pod em que a app estiver sendo executada.

Para parar a execução do banco de dados, basta executar no terminal na raiz do projeto:
```
kubectl delete -f .\deploy\postgres\postgres-deployment.yml
kubectl delete -f .\deploy\postgres\postgres-pvc.yml
kubectl delete -f .\deploy\postgres\postgres-pv.yml
kubectl delete -f .\deploy\postgres\postgres-service.yml
```
Os comandos acima irão excluir o Deployment, PVC, PV e Service do banco de dados e consequentemente o Pod em que o banco estiver sendo executado.

#### pgAdmin

Caso seja necessário, é possível criar um Pod contendo o pgAdmin para acessar o banco de dados da aplicação. para isso, basta executar os comandos:
```
kubectl apply -f .\deploy\pgadmin\pgadmin-deployment.yml
kubectl apply -f .\deploy\pgadmin\pgadmin-service.yml  
```
Os comando acima irão criar o Deployment e o Service do pgAdmin, o qual estará acessível através da porta 32000 através da configuração de um NodePort na URL: http://localhost:32000/

Para acessar a interface, utilize os dados de acesso:
- Login: admin@admin.com
- Password: pgadmin1234

Para acesso à base, é necessário configurar um servidor com os seguintes dados:
- hostname/address: svc-postgres
- Port: 5432
- username: postgres
- password: abcd1234

Para parar a execução do pgAdmin, basta executar no terminal na raiz do projeto:
```
kubectl delete -f .\deploy\pgadmin\pgadmin-deployment.yml
kubectl delete -f .\deploy\pgadmin\pgadmin-service.yml  
```

####  Carga inicial de dados
Considerando que a estrutura de deploy do Kubernetes é pensada para um uso real, não condiz com essa intenção o uso de um arquivo com uma massa inicial de dados, como é feito para o desenvolvimento local rodando a aplicação pelo docker-compose.
Dessa forma, foi feita uma collection do Postman que exemplifica um fluxo de funcionamento da aplicação, e a própria collection possui chamadas HTTP configuradas para criar a massa necessária para a sua própria execução.
</details>

### Executando via docker-compose
<details>
<summary>Instruções</summary>

####  Ferramentas necessárias
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

####  Carga inicial de dados

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

A collection do Postman referente à aplicação rodando no docker levou em consideração esses dados cadastrados nessa carga inicial;
</details>