package com.example.appcrialista;

import androidx.annotation.NonNull;

public class ItemLista extends Lista {

    public String id,descricao, quantidade,marca;

    @NonNull
    @Override
    public String toString() {
        return descricao+" quantidade: "+quantidade+ " marca: "+marca;
    }
}
