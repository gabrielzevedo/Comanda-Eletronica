package com.gabrielzevedo.comandaeletronica;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
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

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class mostrarCategoria extends AppCompatActivity {

    private Button btnVoltar, btnVisualizarComanda;
    private ListView listViewCategorias;
    private TextView lblTitulo;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    CategoriasAdapter categoriasAdapter;
    List<Categoria> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_categoria);

        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        btnVisualizarComanda = (Button) findViewById(R.id.btnVisualizarComanda);

        listViewCategorias = (ListView) findViewById(R.id.listViewCategorias);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");

        lista = new ArrayList<Categoria>();
        categoriasAdapter = new CategoriasAdapter(mostrarCategoria.this, lista);

        listViewCategorias.setAdapter(categoriasAdapter);

        listarCategorias(ip);

        Pedido p = new Pedido();

        int numMesa = p.getNumMesa();
        lblTitulo.setText("Mesa "+numMesa);


        btnVisualizarComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vcomanda = new Intent(mostrarCategoria.this, visualizarComanda.class);
                vcomanda.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(vcomanda);
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        listViewCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(20);

                int idCategoria = lista.get(i).getId();

                Intent mostrarProduto = new Intent(mostrarCategoria.this, mostrarProduto.class);
                mostrarProduto.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                mostrarProduto.putExtra("idCategoria", idCategoria);
                startActivity(mostrarProduto);
            }
        });

    }

    private void listarCategorias(String ip){

        String url = ip + "/listarCategorias.php";

        Ion.with(mostrarCategoria.this)
                .load(url)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>(){
                    @Override
                    public void onCompleted(Exception e, JsonArray result){
                        try {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject obj = result.get(i).getAsJsonObject();

                                Categoria c = new Categoria();
                                c.setId(obj.get("id").getAsInt());
                                c.setNome(obj.get("nome").getAsString());

                                lista.add(c);
                            }

                            categoriasAdapter.notifyDataSetChanged();
                        }catch (Exception erro){
                            Toast.makeText(mostrarCategoria.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void cancelarPedido(){
        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");

        String url = ip + "/cancelarPedido.php";

        Pedido p = new Pedido();
        int idMesa = p.getIdMesa();
        int idPedido = p.getId();

        Ion.with(mostrarCategoria.this)
                .load(url)
                .setBodyParameter("idMesa", Integer.toString(idMesa))
                .setBodyParameter("idPedido", Integer.toString(idPedido))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>(){
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String RETORNO = result.get("status").getAsString();

                            if (RETORNO.equals("erro")) {
                                Toast.makeText(mostrarCategoria.this, "Erro ao cancelar o pedido.", Toast.LENGTH_LONG).show();
                            } else {

                                Toast.makeText(mostrarCategoria.this, "Pedido cancelado", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(mostrarCategoria.this, selecionarMesa.class);
                                i.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }
                        } catch (Exception erro) {
                            Toast.makeText(mostrarCategoria.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mostrarCategoria.this, R.style.AlertDialogCustom);
        builder1.setMessage("Deseja realmente quer cancelar o pedido?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Sim",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        cancelarPedido();

                    }
                });

        builder1.setNegativeButton(
                "Não",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

}
