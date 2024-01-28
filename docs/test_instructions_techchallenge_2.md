# Instruções de Testes - Tech Challenge Fase 2 - FIAP Postech

- [Instruções de Testes - Tech Challenge Fase 2 - FIAP Postech](#instruções-de-testes---tech-challenge-fase-2---fiap-postech)
  - [Pré Requisitos](#pré-requisitos)
    - [Carga de dados inicial](#carga-de-dados-inicial)
    - [Execução das funcionalidades da aplicação](#execução-das-funcionalidades-da-aplicação)


## Pré Requisitos
- Contexto da aplicação rodando no Kubernetes seguindo as instruções do README.md
- Postman
- Collection do projeto importada no Postman

### Carga de dados inicial
Para que os testes partam de uma base já populada com uma massa mínima para os testes, é possível executar as requisições configuradas no folder "Fluxo de Testes - Kubernetes - Base inicial". Para isso, 
1. Clique o botão direito no folder e em seguida clique em "Run Folder":<br>
![image](https://github.com/guibaarros/fastfood-fiap-postech/assets/34094900/cd131f2d-f55d-47d2-b8c8-11209772c1cb)<br>
2. Certifique-se que todas as requisições estão selecionadas (se necessário, clique em "Select All") e em seguida, clique em "Run FIAP - Postech - Lanchonete":<br>
![image](https://github.com/guibaarros/fastfood-fiap-postech/assets/34094900/cf176e3e-d630-4cdb-815a-e7013f26e9a5)<br>
3. Após a execução, será possível verificar, na base pelo pgAdmin ou pelos demais endpoints de consulta, que a massa inicial de dados foi criada com sucesso: <br>
- Tabela de clientes: <br>
![image](https://github.com/guibaarros/fastfood-fiap-postech/assets/34094900/e6a8f54c-06f4-49c2-a4d6-31afecea2668) <br>
- Tabela de produtos: <br>
![image](https://github.com/guibaarros/fastfood-fiap-postech/assets/34094900/b782a8f3-d531-4b5d-8776-499e4518698a)<br>
- Tabela de pedidos:<br>
![image](https://github.com/guibaarros/fastfood-fiap-postech/assets/34094900/927ce134-ef8c-4733-b314-77fac14e71bc) <br>
- Tabela de produtos dos pedidos: <br>
![image](https://github.com/guibaarros/fastfood-fiap-postech/assets/34094900/60cbf5e6-d095-4a19-850f-f57d20d2693b) <br>

### Execução das funcionalidades da aplicação
Considerando os requisitos de entrega das APIs da segunda fase do Tech Challenge, temos os seguintes endpoints:<br>
| API | URL | Request no Postman |
|---|---|---|
| Checkout do Pedido | POST http://localhost:32001/order | Números 13~18 |
| Consultar status do pagamento | GET http://localhost:32001/order/{id}/payment | Números 21, 23, 25, 27, 29, 31, 33 |
| Webhook de confirmação do pagamento | POST http://localhost:32001/mercadopago/webhook | Números 20, 22, 24, 26, 28, 30, 32 |
| Consulta dos pedidos não finalizados | GET http://localhost:32001/order | Números 19, 50 |
| Atualizar status do pedido | PATCH http://localhost:32001/order/{id} | Números 34~49 |

Para realizar a execução de um fluxo utilizando os endpoints desenvolvidos, foi criado um folder na collection do postman "Fluxo de Testes - Kubernetes" com uma ordem de execução das APIs: <br>
![image](https://github.com/guibaarros/fastfood-fiap-postech/assets/34094900/a33fcc46-9c4a-455c-8ac8-f06259b6493c) <br>
São 50 requisições que passam por uma ordem lógica de execução, sendo elas:
- Cadastro de novos produtos e exclusão: requisições 1 à 10
- Cadastro de um novo cliente: requisições 11 e 12
- Criação de novos pedidos com e sem clientes relacionados: requisições 13 à 18
- Listagem de pedidos não finalizados que retorna vazia, seguindo a regra definida: requisição 19
- Confirmação do pagamento pelo webhook Fake e consulta dos status de pagamento: requisições 20 à 33
- Atualização dos status dos pedidos criados anteriormente para que apareçam na listagem de pedidos, mantendo um cadastro de 1 pedido finalizado, 2 recebidos, 2 prontos e 2 em preparação: requisições 34 à 49
- Listagem de pedidos não finalizados retornando a lista de pedidos ordenados conforme regra definida: requisição 50

Para completar o fluxo, é possível executar cada requisição manualmente, uma a uma, ou então, como foi feito na carga inicial, é possível executar todas de uma vez através do Postman. Para isso,
1. Clique com o botão direito no folder "Fluxo de Testes - Kubernetes" e em seguida em "Run folder": <br>
![image](https://github.com/guibaarros/fastfood-fiap-postech/assets/34094900/df923e44-8e85-4b21-962b-3fa5a1ff4a5a) <br>
2. Certifique-se que todas as requisições estão selecionadas (se necessário, clique em "Select All") e em seguida, clique selecione a opção "Persist responses for a session" para que seja possível visualizar as respostas das APIs, e então clique em "Run FIAP - Postech - Lanchonete": <br>
![image](https://github.com/guibaarros/fastfood-fiap-postech/assets/34094900/49d86c89-04dd-4822-99cb-08a89cd084aa)<br>
3. Após a execução, é possível visualizar as alterações feitas através da base, pelo pgAdmin, ou pelos demais endpoints de consulta, por exemplo, o endpoint de listagem de pedidos não finalizados: <br>
![image](https://github.com/guibaarros/fastfood-fiap-postech/assets/34094900/02a69ae0-112d-4aec-9200-63762a472d2c)<br>


Além disso, caso seja necessário fazer uma requisição individualmente a alguma das APIs, na collection foi criado um folder "APIs - Kubernetes" com as estruturas de todas as requisições e exemplos de execução dos endpoints disponíveis na aplicação, separados por controllers:
![image](https://github.com/guibaarros/fastfood-fiap-postech/assets/34094900/399b9d2a-6209-479e-96e4-ba12123ac131)










