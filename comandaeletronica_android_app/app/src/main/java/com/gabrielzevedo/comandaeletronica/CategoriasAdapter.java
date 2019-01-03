package com.gabrielzevedo.comandaeletronica;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoriasAdapter extends BaseAdapter {

    private Context ctx;
    private List<Categoria> lista;

    public CategoriasAdapter(mostrarCategoria ctx2, List<Categoria> lista2){
        ctx = ctx2;
        lista = lista2;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Categoria getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = null;

        if(view == null){
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            v = inflater.inflate(R.layout.item_lista_categoria, null);
        }else{
            v = view;
        }

        Categoria c = getItem(position);
        TextView itemNome = (TextView) v.findViewById(R.id.itemNome);
        itemNome.setText(c.getNome());


        return v;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
