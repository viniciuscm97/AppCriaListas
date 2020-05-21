package com.example.appcrialista;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.DatabaseError;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListasActivity extends AppCompatActivity {

    private ListView lvListas;
    private List<Lista> lista;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ChildEventListener childEventListener;
    private Query query;
    private ArrayAdapter<Lista> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListasActivity.this, FormListaActivity.class);
                startActivity( intent );
            }
        });

        lvListas = findViewById( R.id.lvListas );

        lista = new ArrayList<>();
        adapter = new ArrayAdapter<Lista>(
                ListasActivity.this, android.R.layout.simple_list_item_1, lista);
        lvListas.setAdapter( adapter );

        lvListas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // MANDAR O ID DA LISTA QUE TA ACESSANDO, COMO? NAO SEI, TE VIRA FUDIDO dar uma bizu no setonlongcliclistener

                final Lista ListSelecionado = lista.get( position );
                String idList = ListSelecionado.id;

                Intent intent = new Intent(ListasActivity.this, Item_lista_activity.class);
                intent.putExtra("Listaselecionado",idList);
                startActivity( intent );
            }
        });

        lvListas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                excluir( position );
                return true;

            }
        });


    }
    private void excluir(final int posicao){


        final Lista listaSelecionado = lista.get(posicao);

        AlertDialog.Builder alerta = new AlertDialog.Builder(ListasActivity.this);
        alerta.setTitle("Excluir Lista");
        alerta.setIcon( android.R.drawable.ic_delete );
        alerta.setMessage("Confirma a exclusão da lista " + listaSelecionado.nome + "?");
        alerta.setNeutralButton("Cancelar", null);
        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                reference.child("listas").child( listaSelecionado.id ).removeValue();

                lista.remove( posicao );
                adapter.notifyDataSetChanged();
            }
        });
        alerta.show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        lista.clear();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        query = reference.child("listas").orderByChild("nome");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Lista l = new Lista();
                l.id = dataSnapshot.getKey();
                l.nome = dataSnapshot.child("nome").getValue(String.class);


                lista.add( l );

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
