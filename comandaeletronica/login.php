<?php

    include "conexao.php";
    
    $nomeUsuario  = $_POST['usuario_app'];
    $senhaUsuario = $_POST['senha_app'];


    $sql_login = "SELECT * FROM usuario WHERE nomeUsuario = :NOME AND senhaUsuario = :SENHA";
    $stmt = $PDO->prepare($sql_login);
    $stmt->bindParam(':NOME', $nomeUsuario);
    $stmt->bindParam(':SENHA', $senhaUsuario);
    $stmt->execute();
    
    if($stmt->rowCount() > 0){
        $result = $stmt->fetchAll();

        foreach ($result as $row => $usuario) {
            $retornoApp = array("status"=>"sucesso", "id"=>$usuario['idUsuario'], "nome"=>$usuario['nomeUsuario'], "cargo"=>$usuario['cargoUsuario']);
          //$usuario['nomeUsuario']
        }
        
    }else{
        $retornoApp = array("status"=>"erro");
    }
    echo json_encode($retornoApp);
        
?>
