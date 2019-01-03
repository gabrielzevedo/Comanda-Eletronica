package com.gabrielzevedo.comandaeletronica;

public class itemPedido{
    public static int idItemPedido;
    public static int idProduto;
    public static int qtdProduto;
    public static double precoItem;
    public static String nomeProduto;

//    public itemPedido(int id, int idMesa, int idUsuario, String status, int idPedido, int idProduto, int qtdProduto) {
//        super(id, idMesa, idUsuario, status);
//        this.idPedido = idPedido;
//        this.idProduto = idProduto;
//        this.qtdProduto = qtdProduto;
//    }


    public double getPrecoItem() {
        return precoItem;
    }

    public void setPrecoItem(double precoItem) {
        this.precoItem = precoItem;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getIdItemPedido() {
        return idItemPedido;
    }

    public void setIdItemPedido(int idItemPedido) {
        this.idItemPedido = idItemPedido;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getQtdProduto() {
        return qtdProduto;
    }

    public void setQtdProduto(int qtdProduto) {
        this.qtdProduto = qtdProduto;
    }
}
