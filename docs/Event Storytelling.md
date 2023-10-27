# Event Storytelling – Pedido do cliente pelo Totem
&emsp;O processo de realização do pedido se inicia quando o cliente toca no botão “Monte seu combo aqui” na tela do totem de pedidos localizado nas dependências da lanchonete. O sistema então pergunta ao cliente se ele deseja se identificar, o qual por sua vez pode se cadastrar com nome, e-mail e CPF, se identificar apenas utilizando o CPF ou continuar o pedido sem nenhuma identificação. Caso o cliente se identifique, o sistema registra esse cliente na base ou atualiza a data da última visita se o cliente já estiver registrado. 

&emsp;A montagem do combo é feita na seguinte ordem: o cliente seleciona o lanche, depois o acompanhamento, em seguida a bebida e por fim a sobremesa.

&emsp;Após a interface de identificação, é apresentado na tela do totem uma lista de lanches disponíveis com um resumo do pedido no rodapé da interface, o qual é incrementado conforme o cliente seleciona os produtos desejados. Acima do resumo, há um botão em formato de seta para a direita que, ao ser acionado, atualiza a lista de produtos para próximo tipo de produto, seguindo a ordem de apresentação. 

&emsp;O cliente seleciona o produto da vez, o sistema adiciona o produto no resumo do pedido e atualiza a lista de produtos com o próximo tipo. Após o cliente selecionar o último produto, a sobremesa desejada, o sistema exibe uma tela com os itens selecionados para revisão do pedido e o cliente então pode acionar o botão de realizar o pagamento. 

&emsp;O sistema registra o pedido com o estado de pagamento “aguardando pagamento”, associando o pedido ao cliente e apresenta o QR CODE e aguarda o cliente fazer o pagamento.

&emsp;O cliente faz o pagamento por QR CODE, o sistema reconhece o pagamento pela integração com o Mercado Pago, atualiza o estado do pagamento do pedido para “pago”, gera um número de pedido e o informa ao cliente, para que o cliente possa acompanhar o preparo do pedido por um telão que apresenta os pedidos que estão aguardando preparo, em preparo e prontos para retirada. 

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
- Fila de Preparo