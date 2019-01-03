<?php

    include "conexao.php";

    $idMesa = $_POST['idMesa'];
    $idUsuario  = $_POST['idUsuario'];
    $statusPedido  = "novo";
    
    $sql_listar = "INSERT INTO pedido (idMesa, idUsuario, statusPedido) VALUES ( :IDMESA, :IDUSUARIO, :STATUSPEDIDO )";

    $stmt = $PDO->prepare($sql_listar);
    $stmt->bindParam(':IDMESA', $idMesa);
    $stmt->bindParam(':IDUSUARIO', $idUsuario);
    $stmt->bindParam(':STATUSPEDIDO', $statusPedido);

    $sql_status_mesa  = "UPDATE mesa SET statusMesa = 'atendendo' WHERE idMesa = :IDMESA";
    $stmt2 = $PDO->prepare($sql_status_mesa);
    $stmt2->bindParam(':IDMESA', $idMesa);

        
    if($stmt->execute()){
        $id = $PDO->lastInsertId();
        
        if($stmt2->execute()){
            $dados = array("status"=>"sucesso", "idPedido"=>$id);
        }
        
    }else{
        $dados = array("status"=>"erro");
    }

    echo json_encode($dados);


?>
