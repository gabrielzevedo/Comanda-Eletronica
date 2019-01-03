<?php

    include "conexao.php";

    $idCategoria  = $_POST['idCategoria'];
    
    $sql_listar = "SELECT * FROM produto WHERE idCategoria = :IDCATEGORIA";

    $stmt = $PDO->prepare($sql_listar);
    $stmt->bindParam(':IDCATEGORIA', $idCategoria);
    $stmt->execute();

    $resultado = array();

    while($produto = $stmt->fetch(PDO::FETCH_OBJ)){
        $resultado[] = array("id"=>$produto->idProduto, "descricao"=>$produto->descricaoProduto, "preco"=>$produto->precoProduto);
    }

    echo json_encode($resultado);
        
?>