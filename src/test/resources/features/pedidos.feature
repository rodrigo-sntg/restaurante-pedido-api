# language: pt

Funcionalidade: Usuario cria um pedido
    Cenário: Criar um pedido com sucesso
        Dado um cliente
        Quando eu adiciono itens ao pedido
        E eu confirmo o pedido
        Então o pedido deve ser criado com sucesso