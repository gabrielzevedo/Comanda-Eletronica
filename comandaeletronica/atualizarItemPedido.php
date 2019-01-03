<?php

    include "conexao.php";

    $qtdProduto  = $_POST['qtdProduto'];
    $idItemPedido  = $_POST['idItemPedido'];

    if($qtdProduto == 0){
        $sql_deletar = "DELETE FROM itempedido WHERE idItemPedido = :IDITEMPEDIDO";
        
        //DELETE FROM `itempedido` WHERE `itempedido`.`idItemPedido` = 67"

        $stmt = $PDO->prepare($sql_deletar);
        $stmt->bindParam(':IDITEMPEDIDO', $idItemPedido);

        if($stmt->execute()){

                $dados = array("status"=>"sucesso");

        }else{
            $dados = array("status"=>"erro");
        }
    }else{
    
    $sql_atualizar = "UPDATE itempedido SET qtdProduto = :QTDPRODUTO WHERE idItemPedido = :IDITEMPEDIDO";

    $stmt = $PDO->prepare($sql_atualizar);
    $stmt->bindParam(':QTDPRODUTO', $qtdProduto);
    $stmt->bindParam(':IDITEMPEDIDO', $idItemPedido);
        
    if($stmt->execute()){
        
            $dados = array("status"=>"sucesso");
        
    }else{
        $dados = array("status"=>"erro");
    }
    }

    echo json_encode($dados);


?>
