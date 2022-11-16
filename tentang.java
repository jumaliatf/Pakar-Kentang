package com.example.pakar_kentang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class tentang extends AppCompatActivity {

    private ImageButton pindah;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);

        //BUTTON LANJUT KE SCREEN BERIKUTNYA
        pindah = findViewById(R.id.button_back);
        pindah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tentang.this, MainActivity.class));
                finish();
            }
        });
    }
}