package com.gabrielzevedo.comandaeletronica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class selecionarMesa extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Button btnVoltar;
    private GridView GridViewMesas;
    MesaAdapter mesaAdapter;
    List<Mesa> lista;

    @Override
    public void onResume(){
        super.onResume();

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");

        lista.clear();

        String acao = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Intent in = getIntent();
            acao = in.getStringExtra("acao");
        }

        if(acao.equalsIgnoreCase("consultarMesa")){
            listarMesasComPedido(ip);
        }else {
            listarMesas(ip);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecionar_mesa);

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");

        btnVoltar = (Button) findViewById(R.id.btnVoltar);

        GridViewMesas = (GridView) findViewById(R.id.GridViewMesas);

        lista = new ArrayList<Mesa>();
        mesaAdapter = new MesaAdapter(selecionarMesa.this, lista);

        GridViewMesas.setAdapter(mesaAdapter);

        GridViewMesas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(20);

                int idMesa = lista.get(position).getId();
                int numMesa = lista.get(position).getNumero();
                int idPedido = lista.get(position).getIdPedido();


                String acao = "";
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    Intent in = getIntent();
                    acao = in.getStringExtra("acao");
                }

                if(acao.equalsIgnoreCase("consultarMesa")){

                    Pedido p = new Pedido();
                    p.setIdMesa(idMesa);
                    p.setNumMesa(numMesa);
                    p.setId(idPedido);

                    Intent verpedido = new Intent(selecionarMesa.this, visualizarComanda.class);
                    verpedido.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    verpedido.putExtra("acao", acao);
                    startActivity(verpedido);

                }else {

                    Pedido p = new Pedido();
                    p.setIdMesa(idMesa);
                    p.setNumMesa(numMesa);
                    p.setStatus("novo");

                    int idUsuario = prefs.getInt("idUsuario", -1);
                    p.setIdUsuario(idUsuario);

                    novoPedido(ip, idMesa, idUsuario);
                }
            }
        });


        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void listarMesas(String ip){

        String url = ip + "/listarMesas.php";

        Ion.with(selecionarMesa.this)
                .load(url)
                .setBodyParameter("statusMesa", "livre")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>(){
                    @Override
                    public void onCompleted(Exception e, JsonArray result){
                        try {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject obj = result.get(i).getAsJsonObject();

                                Mesa m = new Mesa();

                                m.setId(obj.get("id").getAsInt());
                                m.setNumero(obj.get("numero").getAsInt());
                                m.setStatus(obj.get("status").getAsString());

                                lista.add(m);
                            }
                            if(mesaAdapter.getCount() == 0){
                                Toast.makeText(selecionarMesa.this, "Nenhuma mesa disponível no momento.", Toast.LENGTH_LONG).show();
                            }

                            mesaAdapter.notifyDataSetChanged();
                        }catch (Exception erro){
                            Toast.makeText(selecionarMesa.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void listarMesasComPedido(String ip){

        String url = ip + "/listarMesas.php";

        Ion.with(selecionarMesa.this)
                .load(url)
                .setBodyParameter("statusMesa", "")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>(){
                    @Override
                    public void onCompleted(Exception e, JsonArray result){
                        try {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject obj = result.get(i).getAsJsonObject();

                                Mesa m = new Mesa();

                                m.setId(obj.get("id").getAsInt());
                                m.setNumero(obj.get("numero").getAsInt());
                                m.setIdPedido(obj.get("idpedido").getAsInt());

                                lista.add(m);
                            }
                            if(mesaAdapter.getCount() == 0){
                                Toast.makeText(selecionarMesa.this, "Nenhuma mesa encontrada.", Toast.LENGTH_LONG).show();
                            }

                            mesaAdapter.notifyDataSetChanged();
                        }catch (Exception erro){
                            Toast.makeText(selecionarMesa.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void novoPedido(String ip, int idMesa, int idUsuario){

        String url = ip + "/novoPedido.php";

        Ion.with(selecionarMesa.this)
                .load(url)
                .setBodyParameter("idMesa", Integer.toString(idMesa))
                .setBodyParameter("idUsuario", Integer.toString(idUsuario))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>(){
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String RETORNO = result.get("status").getAsString();

                            if (RETORNO.equals("erro")) {
                                Toast.makeText(selecionarMesa.this, "Erro ao iniciar novo pedido.", Toast.LENGTH_LONG).show();
                            } else {
                                Pedido p = new Pedido();

                                p.setId(result.get("idPedido").getAsInt());

                               // Toast.makeText(selecionarMesa.this, "Pedido " + p.getId() + " iniciado", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(selecionarMesa.this, mostrarCategoria.class);
                                startActivity(i);

                            }
                        } catch (Exception erro) {
                            Toast.makeText(selecionarMesa.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }
}
