<?php


//$dsn = "mysql:host=ENDERECO DO BANCO DE DADOS;dbname=NOME DO BANCO (comanda_eletronica);charset=utf8";
//$usuario = "USUARIO PARA ACESSO AO BANCO DE DADOS";
//$senha = "SENHA PARA ACESSO AO BANCO DE DADOS";


$dsn = "mysql:host=localhost;dbname=comanda_eletronica;charset=utf8";
$usuario = "root";
$senha = "";

try{
    
    $PDO = new PDO($dsn, $usuario, $senha);
    
}catch(PDOException $erro){
    
    exit;
}
        
?>