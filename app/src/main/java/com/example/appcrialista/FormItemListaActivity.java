package com.example.appcrialista;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormItemListaActivity extends AppCompatActivity {

    private EditText etDescricao, etQuantidade, etMarca;
    private Button btnSalvar;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_item_lista);


        etDescricao = findViewById(R.id.etDescricaoItem);
        etMarca = findViewById(R.id.etMarcaItem);
        etQuantidade = findViewById(R.id.etQuantidadeItem);

        btnSalvar = findViewById(R.id.btnSalvarItem);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarItem();
            }
        });


    }

    private void salvarItem(){
        String descricao = etDescricao.getText().toString();
        String marca = etMarca.getText().toString();
        String quantidade = etQuantidade.getText().toString();

        if (!descricao.isEmpty() && !marca.isEmpty() && !quantidade.isEmpty() ){
            String data = getIntent().getExtras().getString("listaToAdd");

            ItemLista i = new ItemLista();
            i.descricao = descricao;
            i.marca = marca;
            i.quantidade = quantidade;

            reference = FirebaseDatabase.getInstance().getReference();
            reference.child("listas").child(data).child("itens").push().setValue( i );

            finish();

        }
    }
}
