<?php

    include "conexao.php";

    $idMesa  = $_POST['idMesa'];
    $idPedido  = $_POST['idPedido'];
    

    $sql_listar = "SELECT itempedido.idItemPedido, produto.descricaoProduto, itempedido.qtdProduto FROM itempedido INNER JOIN produto ON itempedido.idProduto = produto.idProduto INNER JOIN pedido ON itempedido.idPedido = pedido.idPedido WHERE pedido.statusPedido = 'cozinhando' AND pedido.idMesa = :IDMESA AND pedido.idPedido = :IDPEDIDO";

    $stmt = $PDO->prepare($sql_listar);
    $stmt->bindParam(':IDMESA', $idMesa);
    $stmt->bindParam(':IDPEDIDO', $idPedido);
    $stmt->execute();

    $resultado = array();

    while($produto = $stmt->fetch(PDO::FETCH_OBJ)){
        $resultado[] = array("iditem"=>$produto->idItemPedido, "nome"=>$produto->descricaoProduto, "qtd"=>$produto->qtdProduto);
    }

    echo json_encode($resultado);
        
?>