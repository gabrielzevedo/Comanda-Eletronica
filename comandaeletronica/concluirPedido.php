<?php

    include "conexao.php";

    $idPedido  = $_POST['idPedido'];

    $sql_enviar = "UPDATE pedido SET statusPedido = 'concluido' WHERE idPedido = :IDPEDIDO";

    $stmt = $PDO->prepare($sql_enviar);
    $stmt->bindParam(':IDPEDIDO', $idPedido);

            
    if($stmt->execute()){
        
        $dados = array("status"=>"sucesso");
        
    }else{
        $dados = array("status"=>"erro");
    }
    

    echo json_encode($dados);


?>
