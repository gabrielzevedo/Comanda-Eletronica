package com.gabrielzevedo.comandaeletronica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class mostrarProduto extends AppCompatActivity {

    private Button btnVoltar, btnVisualizarComanda;
    private ListView listViewProdutos;
    private TextView lblTitulo;
    private int idCategoria;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    ProdutosAdapter produtosAdapter;
    List<Produto> listaProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_produto);

        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        btnVisualizarComanda = (Button) findViewById(R.id.btnVisualizarComanda);

        listViewProdutos = (ListView) findViewById(R.id.listViewProdutos);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);

        listaProd = new ArrayList<Produto>();
        produtosAdapter = new ProdutosAdapter(mostrarProduto.this, listaProd);

        listViewProdutos.setAdapter(produtosAdapter);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Intent in = getIntent();
            idCategoria = in.getIntExtra("idCategoria", -1);
        }

        listarProdutos();

        Pedido p = new Pedido();

        int numMesa = p.getNumMesa();
        lblTitulo.setText("Mesa "+numMesa);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnVisualizarComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vcomanda = new Intent(mostrarProduto.this, visualizarComanda.class);
                vcomanda.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(vcomanda);
            }
        });

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(20);

                int idProduto = listaProd.get(i).getId();
                String descProduto = listaProd.get(i).getDescricao();

                itemPedido ipe = new itemPedido();
                ipe.setNomeProduto(descProduto);
                ipe.setIdProduto(idProduto);
                ipe.setQtdProduto(1);

                Intent qtdProduto = new Intent(mostrarProduto.this, quantidadeProduto.class);
                qtdProduto.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(qtdProduto);
            }
        });

    }

    private void listarProdutos(){
        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");
        String url = ip + "/listarProdutos.php";

        Ion.with(mostrarProduto.this)
                .load(url)
                .setBodyParameter("idCategoria", Integer.toString(idCategoria))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>(){
                    @Override
                    public void onCompleted(Exception e, JsonArray result){
                        try {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject obj = result.get(i).getAsJsonObject();

                                Produto p = new Produto();
                                p.setId(obj.get("id").getAsInt());
                                p.setDescricao(obj.get("descricao").getAsString());
                                p.setPreco(obj.get("preco").getAsDouble());

                                listaProd.add(p);
                            }

                            produtosAdapter.notifyDataSetChanged();
                        }catch (Exception erro){
                            Toast.makeText(mostrarProduto.this, erro + "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

}
