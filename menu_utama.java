package com.example.pakar_kentang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class menu_utama extends AppCompatActivity {

    private ImageButton pindah;
    private Button kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);

        //BUTTON LANJUT KE SCREEN BERIKUTNYA
        pindah = (ImageButton) findViewById(R.id.pilihhama);
        pindah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu_utama.this,hama.class));
                finish();
            }
        });

        //BUTTON LANJUT KE SCREEN BERIKUTNYA
        pindah = (ImageButton) findViewById(R.id.pilihpenyakit);
        pindah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu_utama.this,penyakit.class));
                finish();
            }
        });

        //BUTTON LANJUT KE SCREEN BERIKUTNYA
        kembali = (Button) findViewById(R.id.button_kembali);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu_utama.this,MainActivity .class));
                finish();
            }
        });
    }
}