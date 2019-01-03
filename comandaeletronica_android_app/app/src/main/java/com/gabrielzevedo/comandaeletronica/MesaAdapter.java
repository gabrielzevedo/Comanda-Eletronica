package com.gabrielzevedo.comandaeletronica;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MesaAdapter extends BaseAdapter {

    private selecionarMesa ctx;
    private List<Mesa> lista;

    public MesaAdapter(selecionarMesa ctx2, List<Mesa> lista2){
        ctx = ctx2;
        lista = lista2;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Mesa getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {
        View v = null;

        if(view == null){
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            v = inflater.inflate(R.layout.item_lista_mesa, null);
        }else{
            v = view;
        }

        Mesa m = getItem(position);
        Button btnMesa = (Button) v.findViewById(R.id.btnMesa);
        String numeroMesa = Integer.toString(m.getNumero());
        btnMesa.setText(numeroMesa);

        btnMesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GridView)viewGroup).performItemClick(v,position,0);
            }
        });

        return v;
    }


}
