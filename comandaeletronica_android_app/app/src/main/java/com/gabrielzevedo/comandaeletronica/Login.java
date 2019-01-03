package com.gabrielzevedo.comandaeletronica;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.Intent;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;


public class Login extends AppCompatActivity {

    private EditText edtUsuario, edtSenha;
    private Button btnEntrar;
    private ImageButton btnConfiguracoes;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtSenha =  (EditText) findViewById(R.id.edtSenha);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnConfiguracoes = (ImageButton) findViewById(R.id.btnConfiguracoes);

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");
        boolean logado = prefs.getBoolean("logado", false);
        String cargo = prefs.getString("cargoUsuario", "");
        int id = prefs.getInt("idUsuario", -1);

        redirecionaUsuario(id, cargo, logado);

        btnConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent config = new Intent(Login.this, Configuracao.class);
                startActivity(config);
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ip.isEmpty()) {
                    Toast.makeText(Login.this, "Vá em Configurações (ícone no canto superior direito da tela) e insira um endereço para conexão.", Toast.LENGTH_LONG).show();
                } else {

                    String URL = ip + "/login.php";

                    String usuario = edtUsuario.getText().toString();
                    String senha = edtSenha.getText().toString();

                    if (usuario.isEmpty() || senha.isEmpty()) {
                        Toast.makeText(Login.this, "Preencha todos os campos.", Toast.LENGTH_LONG).show();
                    } else {
                        Ion.with(Login.this)
                                .load(URL)
                                .setBodyParameter("usuario_app", usuario)
                                .setBodyParameter("senha_app", senha)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                            String RETORNO = result.get("status").getAsString();

                                            //Toast.makeText(Login.this, RETORNO + ".", Toast.LENGTH_LONG).show();

                                            if (RETORNO.equals("erro")) {
                                                Toast.makeText(Login.this, "Usuário ou senha incorreto.", Toast.LENGTH_LONG).show();
                                            } else {
                                                int idUsuario = result.get("id").getAsInt();
                                                String nomeUsuario = result.get("nome").getAsString();
                                                String cargoUsuario = result.get("cargo").getAsString();

                                                Toast.makeText(Login.this, "Bem-vindo, " + nomeUsuario + "!", Toast.LENGTH_LONG).show();

                                                SharedPreferences.Editor editor = prefs.edit();
                                                editor.putInt("idUsuario", idUsuario);
                                                editor.putString("cargoUsuario", cargoUsuario);
                                                editor.putString("nomeUsuario", nomeUsuario);
                                                editor.putBoolean("logado", true);
                                                editor.commit();

                                                redirecionaUsuario(idUsuario, cargoUsuario, true);

                                            }
                                        } catch (Exception erro) {
                                            Toast.makeText(Login.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });


                    }

                }
            }
        });
    }

    public void abreMenu(){
        Intent menu = new Intent(Login.this, Menu.class);
        menu.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(menu);
    }

    public void redirecionaUsuario(int idUsuario, String cargoUsuario, boolean logado){

        //faz redirecionamento para usuario previamente logado,
        // evitando realizar login e consultar banco de dados a cada vez que abrir aplicativo

        if(idUsuario != -1 && logado) {
                abreMenu();
        }

    }

}
