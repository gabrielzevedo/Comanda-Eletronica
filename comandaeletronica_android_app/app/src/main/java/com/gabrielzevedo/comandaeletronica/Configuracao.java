package com.gabrielzevedo.comandaeletronica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class Configuracao extends AppCompatActivity {

    private Button btnVoltar, btnTestar, btnSalvar;
    private TextView lblTitulo;
    private EditText edtEndereco;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean endvalido = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracao);

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        String ip = prefs.getString("ip", "");

        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        btnTestar = (Button) findViewById(R.id.btnTestar);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);

        if(!ip.isEmpty()){
            edtEndereco.setText(ip);
        }

        btnTestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checarConexao(edtEndereco.getText().toString());
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(endvalido) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("ip", edtEndereco.getText().toString());
                    editor.commit();

                    Intent config = new Intent(Configuracao.this, Login.class);
                    startActivity(config);

                    //finish();

                }else {
                    Toast.makeText(Configuracao.this, "Preencha um endereço válido antes de salvar", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void checarConexao(final String endereco){
        String url = endereco + "/testa_conexao.php";

            if(endereco.isEmpty()){
                Toast.makeText(Configuracao.this, "Preencha o endereço", Toast.LENGTH_LONG).show();
            }else if(endereco.toLowerCase().contains("http://") || endereco.toLowerCase().contains("https://")){
                Ion.with(Configuracao.this)
                        .load(url)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                try{
                                    String RETORNO = result.get("status").getAsString();

                                    //Toast.makeText(Login.this, RETORNO + ".", Toast.LENGTH_LONG).show();

                                    if(RETORNO.equals("erro")){
                                        Toast.makeText(Configuracao.this, "Endereço existente, porém erro ao conectar no banco de dados", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(Configuracao.this, "Conexão efetuada com sucesso!", Toast.LENGTH_LONG).show();
                                        endvalido = true;
                                    }
                                }catch (Exception erro){
                                    Toast.makeText(Configuracao.this, "Ocorreu um erro! Tente outro endereço.", Toast.LENGTH_LONG).show();
                                }

                            }
                        });


            }else{
            Toast.makeText(Configuracao.this, "Não esqueça de colocar http:// ou https://", Toast.LENGTH_LONG).show();
        }
    }


}
