<?php

    include "conexao.php";

    $idPedido  = $_POST['idPedido'];
    

    $sql_listar = "SELECT itempedido.idItemPedido, produto.descricaoProduto, itempedido.qtdProduto, cast(itempedido.qtdProduto * produto.precoProduto as decimal(10,2)) as preco FROM itempedido INNER JOIN produto ON itempedido.idProduto = produto.idProduto WHERE itempedido.idPedido = :IDPEDIDO";

    $stmt = $PDO->prepare($sql_listar);
    $stmt->bindParam(':IDPEDIDO', $idPedido);
    $stmt->execute();

    $resultado = array();

    while($produto = $stmt->fetch(PDO::FETCH_OBJ)){
        $resultado[] = array("iditem"=>$produto->idItemPedido, "nome"=>$produto->descricaoProduto, "qtd"=>$produto->qtdProduto, "preco"=>$produto->preco);
    }

    echo json_encode($resultado);
        
?>