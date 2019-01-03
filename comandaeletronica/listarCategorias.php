<?php

    include "conexao.php";
    
    $sql_listar = "SELECT * FROM categoria";
    $stmt = $PDO->query($sql_listar);

    $resultado = array();

    while($categoria = $stmt->fetch(PDO::FETCH_OBJ)){
        $resultado[] = array("id"=>$categoria->idCategoria, "nome"=>$categoria->nomeCategoria);
    }

    echo json_encode($resultado);
        
?>