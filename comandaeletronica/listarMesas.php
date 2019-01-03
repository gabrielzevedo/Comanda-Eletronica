<?php

    include "conexao.php";

    $statusMesa  = $_POST['statusMesa'];
    
if($statusMesa == "livre"){
    $sql_listar = "SELECT * FROM mesa WHERE statusMesa = :STATUS";

    $stmt = $PDO->prepare($sql_listar);
    $stmt->bindParam(':STATUS', $statusMesa);
    $stmt->execute();

    $resultado = array();

    while($mesa = $stmt->fetch(PDO::FETCH_OBJ)){
        $resultado[] = array("id"=>$mesa->idMesa, "numero"=>$mesa->numeroMesa, "status"=>$mesa->statusMesa);
    }
}else{
   // $sql_listar = "SELECT * FROM mesa INNER JOIN pedido ON mesa.idMesa = pedido.idMesa WHERE statusPedido = 'cozinhando' GROUP BY mesa.idMesa";

    $sql_listar = "SELECT * FROM mesa INNER JOIN pedido ON mesa.idMesa = pedido.idMesa WHERE statusPedido = 'cozinhando'";

    $stmt = $PDO->prepare($sql_listar);
    $stmt->execute();

    $resultado = array();

    while($mesa = $stmt->fetch(PDO::FETCH_OBJ)){
        $resultado[] = array("id"=>$mesa->idMesa, "numero"=>$mesa->numeroMesa, "idpedido"=>$mesa->idPedido);
    }
}
    echo json_encode($resultado);
        
?>