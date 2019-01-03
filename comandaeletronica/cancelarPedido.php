<?php

    include "conexao.php";

    $idPedido = $_POST['idPedido'];
    $idMesa  = $_POST['idMesa'];
    
    $sql_cancelar = "DELETE FROM pedido WHERE idPedido = :IDPEDIDO";

    $stmt = $PDO->prepare($sql_cancelar);
    $stmt->bindParam(':IDPEDIDO', $idPedido);

    $sql_status_mesa  = "UPDATE mesa SET statusMesa = 'livre' WHERE idMesa = :IDMESA";
    $stmt2 = $PDO->prepare($sql_status_mesa);
    $stmt2->bindParam(':IDMESA', $idMesa);

        
    if($stmt->execute()){
        
        if($stmt2->execute()){
            $dados = array("status"=>"sucesso");
        }
        
    }else{
        $dados = array("status"=>"erro");
    }

    echo json_encode($dados);


?>
