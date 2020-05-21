package com.example.appcrialista;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Item_lista_activity extends AppCompatActivity {

    private ListView lvItemListas;
    private List<ItemLista> ListItem;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ChildEventListener childEventListener;
    private Query query;
    private ArrayAdapter<ItemLista> adapter;

    private CoordinatorLayout tela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_lista_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String data = getIntent().getExtras().getString("Listaselecionado");

                Intent intent = new Intent(Item_lista_activity.this, FormItemListaActivity.class);
                intent.putExtra("listaToAdd",data);
                startActivity( intent );
            }
        });

        lvItemListas = findViewById( R.id.lvItemListas );

        ListItem = new ArrayList<>();
        adapter = new ArrayAdapter<ItemLista>(
                Item_lista_activity.this, android.R.layout.simple_list_item_1, ListItem);
        lvItemListas.setAdapter( adapter );

        lvItemListas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                excluir( position );
                return true;
            }
        });

        tela = findViewById(R.id.tela);

    }

    private void excluir(final int posicao){


        final ItemLista itemSelecionado = ListItem.get(posicao);
        AlertDialog.Builder alerta = new AlertDialog.Builder(Item_lista_activity.this);
        alerta.setTitle("Excluir Item");
        alerta.setIcon( android.R.drawable.ic_delete );
        alerta.setMessage("Confirma a exclusão do Item " + itemSelecionado.nome + "?");
        alerta.setNeutralButton("Cancelar", null);
        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                reference.child("listas").child("itens").child( itemSelecionado.id ).removeValue();

                ListItem.remove( posicao );
                adapter.notifyDataSetChanged();
            }
        });
        alerta.show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        ListItem.clear();

        String data = getIntent().getExtras().getString("Listaselecionado");

        reference = FirebaseDatabase.getInstance().getReference();
        query = reference.child("listas").child(data).child("itens").orderByChild("nome");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ItemLista i = new ItemLista();
                i.id = dataSnapshot.getKey();
                i.descricao = dataSnapshot.child("descricao").getValue(String.class);
                i.quantidade = dataSnapshot.child("quantidade").getValue(String.class);
                i.marca = dataSnapshot.child("marca").getValue(String.class);


                ListItem.add( i );

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };



        query.addChildEventListener( childEventListener );
    }

    @Override
    protected void onStop() {
        super.onStop();

        query.removeEventListener( childEventListener );
    }
}