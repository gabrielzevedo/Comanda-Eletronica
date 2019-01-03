<?php

    include "conexao.php";

    $idProduto = $_POST['idProduto'];
    $qtdProduto  = $_POST['qtdProduto'];
    $idPedido  = $_POST['idPedido'];
    
    $sql_novo = "INSERT INTO itempedido (idProduto, qtdProduto, idPedido) VALUES ( :IDPRODUTO, :QTDPRODUTO, :IDPEDIDO )";

    $stmt = $PDO->prepare($sql_novo);
    $stmt->bindParam(':IDPRODUTO', $idProduto);
    $stmt->bindParam(':QTDPRODUTO', $qtdProduto);
    $stmt->bindParam(':IDPEDIDO', $idPedido);

//    $sql_status_mesa  = "UPDATE mesa SET statusMesa = 'atendendo' WHERE idMesa = :IDMESA";
//    $stmt2 = $PDO->prepare($sql_status_mesa);
//    $stmt2->bindParam(':IDMESA', $idMesa);

        
    if($stmt->execute()){
        $id = $PDO->lastInsertId();
        
//        if($stmt2->execute()){
            $dados = array("status"=>"sucesso", "idItemPedido"=>$id);
//        }
        
    }else{
        $dados = array("status"=>"erro");
    }

    echo json_encode($dados);


?>
