<?php

include 'conexao.php';

try{
    
    $PDO = new PDO($dsn, $usuario, $senha);
    $resultado = array("status"=>"sucesso");
    echo json_encode($resultado);
    
}catch(PDOException $erro){
    $resultado = array("status"=>"erro");
    echo json_encode($resultado);
    exit;
}
        
?>