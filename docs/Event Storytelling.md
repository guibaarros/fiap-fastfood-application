# Event Storytelling – Pedido do cliente pelo Totem
&emsp;O processo de realização do pedido se inicia quando o cliente toca no botão “Monte seu combo aqui” na tela do totem de pedidos localizado nas dependências da lanchonete. O sistema então pergunta ao cliente se ele deseja se identificar, o qual por sua vez pode se cadastrar com nome, e-mail e CPF, se identificar apenas utilizando o CPF ou continuar o pedido sem nenhuma identificação. Caso o cliente se identifique, o sistema registra esse cliente na base ou atualiza a data da última visita se o cliente já estiver registrado. 

&emsp;A montagem do combo é feita na seguinte ordem: o cliente seleciona o lanche, depois o acompanhamento, em seguida a bebida e por fim a sobremesa.

&emsp;Após a interface de identificação, é apresentado na tela do totem uma lista de lanches disponíveis com um resumo do pedido no rodapé da interface, o qual é incrementado conforme o cliente seleciona os produtos desejados. Acima do resumo, há um botão em formato de seta para a direita que, ao ser acionado, atualiza a lista de produtos para próximo tipo de produto, seguindo a ordem de apresentação. 

&emsp;O cliente seleciona o produto da vez, o sistema adiciona o produto no resumo do pedido e atualiza a lista de produtos com o próximo tipo. Após o cliente selecionar o último produto, a sobremesa desejada, o sistema exibe uma tela com os itens selecionados para revisão do pedido e o cliente então pode acionar o botão de realizar o pagamento. 

&emsp;O sistema registra o pedido com o estado de pagamento “aguardando pagamento”, associando o pedido ao cliente e apresenta o QR CODE e aguarda o cliente fazer o pagamento.

&emsp;O cliente faz o pagamento por QR CODE, o sistema reconhece o pagamento pela integração com o Mercado Pago. Caso o pagamento não seja bem sucedido ou o sistema não seja notificado da confirmação do pagamento em cinco minutos, o sistema atualiza o estado do pedido para "Cancelado" e informa o cliente do ocorrido. Caso o sistema seja notificado da confirmação do pagamento, atualiza o estado do pagamento do pedido para “pago”, gera um número de pedido e o informa ao cliente, para que o cliente possa acompanhar o preparo do pedido por um telão que apresenta os pedidos que estão aguardando preparo, em preparo e prontos para retirada. 

&emsp;O sistema envia o pedido para a fila de preparo, a qual o atendente responsável consegue acompanhar através de uma interface que lista os pedidos a serem preparados. O atendente então informa o início do preparo do pedido através dessa interface, o que altera o estado do pedido para “em preparo”. A alteração dos estados do pedido é refletida no telão disponível para os clientes.

&emsp;Após finalizar o preparo do pedido, o atendente coloca todos os produtos solicitados pelo cliente em uma bandeja, atualiza o estado do pedido para “pronto para retirada” e encaminha a bandeja para o local de retirada de pedidos. O sistema alerta a finalização do preparo no telão, o cliente retira o pedido e o atendente atualiza o estado do pedido para "finalizado".

&emsp;Assim se encerra o processo de pedido, preparo e entrega do pedido ao cliente. Esse processo automatiza o envio dos pedidos para a cozinha e dá autonomia ao cliente solicitar o que deseja sem interferir no funcionamento da lanchonete.

# Linguagem Ubíqua

- Cliente/Client 
- Atendente/Attendant
- Pedido/Order
- Produto/Product
- Tipo de Produto/Product Type
- Combo/Combo
- Lanche/Snack
- Acompanhamento/Side
- Bebida/Drink
- Sobremesa/Dessert
- Totem/Totem
- Estado "AGUARDANDO PAGAMENTO"/Status "AWAITING PAYMENT"
- Estado "PAGO"/Status "PAID"
- Número do Pedido/Order Number
- Estado "AGUARDANDO PREPARO"/Status "AWAITING PREPARATION"
- Estado "EM PREPARO"/Status "PREPARING"
- Estado "PRONTOS PARA RETIRADA"/Status "READY"
- Estado "FINALIZADO"/Status "FINISHED"
- Fila de Preparo/Preparation Queue

### Dicionário da Linguagem Ubíqua

| Termo em Português | Termo em Inglês | Significado |
|:---:|:---:|---|
| Cliente | Client | Pessoa física presente nas dependências da lanchonete que interage com o Totem para:<br>- montar seu combo de lanche, acompanhamento, bebida e sobremesa; <br>- realizar o pagamento;<br>Após a finalização do preparo, o cliente também retira pessoalmente o pedido para consumo na lanchonete. |
| Atendente | Attendant | Pessoa física funcionária da lanchonete responsável por entregar o pedido ao cliente e interagir com o Sistema Gestor de Pedidos para:<br>- identificar os pedidos a serem preparados;<br>- selecionar o pedido para preparo;<br>- indicar a finalização do preparo do pedido;<br>- indicar a retirada do pedido pelo cliente; |
| Pedido | Order | Conjunto de produtos solicitados pelo cliente que, após ser pago, é enviado para preparo para o atendente.<br>Após enviado para preparo, tem seu estado modificado pelo atendente, o qual indica o início, finalização do preparo e entrega ao cliente. |
| Tipo de Produto | Product Type | Identificação da categoria do produto disponível para o cliente, podendo ser:<br>- Lanche;<br>- Acompanhamento;<br>- Bebida;<br>- Sobremesa; |
| Combo | Combo | Conjunto de seleção de zero ou uma unidade de cada tipo de produto disponível para o cliente apresentado pelo Totem. |
| Lanche | Snack | Tipo de produto principal da lanchonete, indicando a refeição salgada mais importante do pedido. Os produtos dessa categoria são cadastrados pela lanchonete. Por exemplo, podem ser X-Burguer, X-Salada, Hot-Dog, etc. |
| Acompanhamento | Side | Tipo de produto da lanchonete, indicando uma refeição salgada menor que acompanha a refeição principal. Os produtos dessa categoria são cadastrados pela lanchonete. Por exemplo, podem ser Batata Frita, Onion Rings, Nuggets, etc. |
| Bebida | Drink | Tipo de produto da lanchonete, indicando uma bebida para acompanhar a refeição principal. Os produtos dessa categoria são cadastrados pela lanchonete. Por exemplo, podem ser Água, Coca-Cola, Suco de Laranja, etc. |
| Sobremesa | Dessert | Tipo de produto da lanchonete, indicando uma refeição geralmente doce e menor que finaliza a refeição principal. Os produtos dessa categoria são cadastrados pela lanchonete. Por exemplo, podem ser Milkshake, Pudim de leite, Petit Gateau, etc. |
| Totem | Totem | Aparelho localizado nas dependências da lanchonete responsável por apresentar ao cliente uma interface visual que tem como objetivo orientá-lo durante o fluxo de identificação do cliente e solicitação do pedido. |
| Estado "AGUARDANDO PAGAMENTO" | Status "AWAITING PAYMENT" | Estado atribuído ao Pedido após a finalização da solicitação do cliente e antes da notificação do pagamento pelo WebHook da integração com o Mercado Pago. |
| Estado "PAGO" | Status "PAID" | Estado atribuído ao Pedido após a confirmação do pagamento pelo WebHook da integração com o Mercado Pago. |
| Estado "CANCELADO" | Status "CANCELLED" | Estado atribuído ao pedido após a notificação de recusa do pagamento pelo WebHook da integração com o Mercado Pago ou após esperar cinco minutos da criação do QR CODE de pagamento e não receber a confirmação do Pagamento. |
| Número do Pedido | Order Number | Número do pedido gerado após confirmação do pagamento composto por uma sequência de até três dígitos iniciada em 001 que é reiniciada todo primeiro dia do mês ou quando a sequência chegar em 999. |
| Estado "AGUARDANDO PREPARO" | Status "AWAITING PREPARATION" | Estado atribuído ao Pedido na fila de preparo imediatamente após a confirmação do pagamento. |
| Estado "EM PREPARO" | Status "PREPARING" | Estado atribuído ao Pedido na fila de preparo após o atendente selecionar o pedido para preparo na interface do sistema. |
| Estado "PRONTOS PARA RETIRADA" | Status "READY" | Estado atribuído ao Pedido na fila de preparo após o atendente indicar a finalização do preparo na interface do sistema. |
| Estado "FINALIZADO" | Status "FINISHED" | Estado atribuído ao Pedido na fila de preparo após o atendente indicar na interface do sistema a retirada do pedido pelo cliente. |
| Fila de Preparo | Preparation Queue | Lista de pedidos visualizada pelo atendente para orientar o fluxo de preparo e entrega de pedidos ao cliente. A lista é composta de pedidos com os estados:<br>- "AGUARDANDO PREPARO";<br>- "EM PREPARO";<br>- "PRONTOS PARA RETIRADA";<br>A lista é ordenada pela ordem do fluxo do processo de atualização do estado do pedido e número do pedido. |