package com.example.pakar_kentang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class MainActivity extends AppCompatActivity {

    private Button pindah,keluar;

    SliderView sliderView;
    int[] images = {R.drawable.home4,
            R.drawable.home,
            R.drawable.home1,
            R.drawable.home2,
            R.drawable.home3,
            R.drawable.logo_asli,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //IMAGE SLIDER
        sliderView = findViewById(R.id.image_slider);
        SliderAdapter sliderAdapter = new SliderAdapter(images);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
        //TUTUP

        //BUTTON LANJUT KE SCREEN BERIKUTNYA
        pindah = findViewById(R.id.button_utama);
        pindah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, menu_utama.class));
                finish();
            }
        }); //TUTUP BUTTON LANJUT KE SCREEN BERIKUTNYA

        //BUTTON LANJUT KE SCREEN BERIKUTNYA
        pindah = findViewById(R.id.button_tentang);
        pindah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, tentang.class));
                finish();
            }
        }); //TUTUP BOTTON PINDAH KE SCREEN SELANJUTNYA

        //BUTTON EXIT
        keluar = (Button) findViewById(R.id.button_keluar);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }//TUTUP BUTTON EXIT

    //POPUP BUTTON EXIT
    @Override
    public void onBackPressed(){
        showAlertDialog();
    }
    private void showAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Tutup aplikasi ini?");
        builder.setCancelable(true);
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }

        });
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog AlertDialog = builder.create();
        AlertDialog.show();
    } //TUTUP POPUP BUTTON EXIT
}