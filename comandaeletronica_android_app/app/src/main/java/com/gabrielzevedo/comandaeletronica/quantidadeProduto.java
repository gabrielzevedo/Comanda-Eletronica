package com.gabrielzevedo.comandaeletronica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static java.lang.Integer.toString;

public class quantidadeProduto extends AppCompatActivity {

    private Button btnVoltar, btnMenos, btnMais, btnConfirmarProduto;
    private TextView lblProduto, lblQtd;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    ProdutosAdapter produtosAdapter;
    List<Produto> listaProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quantidade_produto);

        String acao = "";

        btnConfirmarProduto = (Button) findViewById(R.id.btnConfirmarProduto);
        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        btnMais = (Button) findViewById(R.id.btnMais);
        btnMenos = (Button) findViewById(R.id.btnMenos);
        lblProduto = (TextView) findViewById(R.id.lblProduto);
        lblQtd = (TextView) findViewById(R.id.lblQtd);

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Intent in = getIntent();
            acao = in.getStringExtra("acao");
        }

        itemPedido ipe = new itemPedido();
        lblProduto.setText(ipe.getNomeProduto());
        lblQtd.setText(Integer.toString(ipe.getQtdProduto()));

        if (acao.equalsIgnoreCase("alterar")) {
            btnConfirmarProduto.setText("Atualizar produto");
            int load = obtemQuantidade();
        }

        final String finalAcao = acao;
        btnConfirmarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalAcao.equalsIgnoreCase("alterar")) {
                    atualizarItem(ip);
                }else{
                    novoItemPedido(ip);
                }
            }
        });



        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux = String.valueOf(obtemQuantidade() - 1);
                lblQtd.setText(aux);

                int verifica = obtemQuantidade();
            }
        });

        btnMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux = String.valueOf(obtemQuantidade() + 1);
                lblQtd.setText(aux);

                int verifica = obtemQuantidade();
            }
        });



        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public int obtemQuantidade(){
        String acao = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Intent in = getIntent();
            acao = in.getStringExtra("acao");
        }

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(50);

        lblQtd = (TextView) findViewById(R.id.lblQtd);
        int qtd = Integer.parseInt(lblQtd.getText().toString());
        btnMenos = (Button) findViewById(R.id.btnMenos);

        if(qtd > 1){
            btnMenos.setEnabled(true);
        }else{
            btnMenos.setEnabled(false);
        }

        if (acao.equalsIgnoreCase("alterar")) {
            if(qtd > 0){
                btnMenos.setEnabled(true);
            }else{
                btnMenos.setEnabled(false);
            }
        }



        return qtd;
    }


    private void atualizarItem(String ip){
        final itemPedido ipe = new itemPedido();

        int qtdProduto = obtemQuantidade();
        int idItemPedido = ipe.getIdItemPedido();

        String url = ip + "/atualizarItemPedido.php";

        Ion.with(quantidadeProduto.this)
                .load(url)
                .setBodyParameter("qtdProduto", Integer.toString(qtdProduto))
                .setBodyParameter("idItemPedido", Integer.toString(idItemPedido))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>(){
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String RETORNO = result.get("status").getAsString();

                            if (RETORNO.equals("erro")) {
                                Toast.makeText(quantidadeProduto.this, "Erro ao atualizar item.", Toast.LENGTH_LONG).show();
                            } else {

                                Toast.makeText(quantidadeProduto.this, "Produto atualizado", Toast.LENGTH_SHORT).show();

                                finish();

                            }
                        } catch (Exception erro) {
                            Toast.makeText(quantidadeProduto.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void novoItemPedido(String ip){
        final Pedido p = new Pedido();
        final itemPedido ipe = new itemPedido();

        int idProduto = ipe.getIdProduto();
        int qtdProduto = obtemQuantidade();
        int idPedido = p.getId();

        String url = ip + "/novoItemPedido.php";

        Ion.with(quantidadeProduto.this)
                .load(url)
                .setBodyParameter("idProduto", Integer.toString(idProduto))
                .setBodyParameter("qtdProduto", Integer.toString(qtdProduto))
                .setBodyParameter("idPedido", Integer.toString(idPedido))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>(){
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String RETORNO = result.get("status").getAsString();

                            if (RETORNO.equals("erro")) {
                                Toast.makeText(quantidadeProduto.this, "Erro ao adicionar item.", Toast.LENGTH_LONG).show();
                            } else {

//                                p.setId(result.get("idPedido").getAsInt());
                                ipe.setIdProduto(result.get("idItemPedido").getAsInt());

                                Toast.makeText(quantidadeProduto.this, "Produto adicionado", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(quantidadeProduto.this, mostrarCategoria.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }
                        } catch (Exception erro) {
                            Toast.makeText(quantidadeProduto.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

}
