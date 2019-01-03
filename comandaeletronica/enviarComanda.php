<?php

    include "conexao.php";

    $idMesa  = $_POST['idMesa'];
    $idPedido  = $_POST['idPedido'];

    $sql_enviar = "UPDATE pedido SET statusPedido = 'cozinhando' WHERE idPedido = :IDPEDIDO";

    $stmt = $PDO->prepare($sql_enviar);
    $stmt->bindParam(':IDPEDIDO', $idPedido);

    $sql_enviar2 = "UPDATE mesa SET statusMesa = 'livre' WHERE idMesa = :IDMESA";

    $stmt2 = $PDO->prepare($sql_enviar2);
    $stmt2->bindParam(':IDMESA', $idMesa);
        
    if($stmt->execute() && $stmt2->execute()){
        
        $dados = array("status"=>"sucesso");
        
    }else{
        $dados = array("status"=>"erro");
    }
    

    echo json_encode($dados);


?>
