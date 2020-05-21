package com.example.appcrialista;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormListaActivity extends AppCompatActivity {

    private EditText etNomeLista;
    private Button btnSalvar;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_lista);

        etNomeLista = findViewById(R.id.etNomeLista);
        btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarLista();
            }
        });

    }

    private void salvarLista(){
        String nome = etNomeLista.getText().toString();

        if (!nome.isEmpty()){
            Lista l = new Lista();
            l.nome = nome;

            database = FirebaseDatabase.getInstance();
            reference = database.getReference();
            reference.child("listas").push().setValue( l );

            finish();

        }

    }


}
