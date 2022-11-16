package com.example.pakar_kentang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class penyakit extends AppCompatActivity {

    private ImageButton pindah;
    private Button Diagnosa;

    private SQLiteDatabase db;
    private penyakit.MyCustomAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyakit);

        pindah = (ImageButton) findViewById(R.id.button_back);
        pindah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(penyakit.this, menu_utama.class));
                finish();
            }
        });

        //        open database
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        if (mDBHelper.openDatabase())
            db = mDBHelper.getReadableDatabase();

//        array buat nyimpen daftar gejala
        ArrayList<GejalaPenyakit> gejalaList = new ArrayList<GejalaPenyakit>();
        GejalaPenyakit gejala;
//        ambil data gejala dari database
        String query = "SELECT nama_gejala FROM gejala_penyakit ORDER BY kode_gejala";
        Cursor cursor = db.rawQuery(query, null);
//        looping untuk memasukkan data gejala kedalam array
        while (cursor.moveToNext()) {
//            ambil gejala dari database dan masukkan ke objek gejala dengan nama gejala dan ceklis default ke false
            gejala = new GejalaPenyakit(cursor.getString(0), false);
            gejalaList.add(gejala); //masukkan ke array
        }
        cursor.close(); //tutup cursor database

        //        adapter untuk daftar gejala biar tampil ke listview
        dataAdapter = new penyakit.MyCustomAdapter(this, R.layout.list_gejala_penyakit, gejalaList);
        ListView listView = findViewById(R.id.list_gejala);
        listView.setAdapter(dataAdapter); // set listview dengan dataadapter

        // button diagnosa
        Button myButton = findViewById(R.id.btn_hasil_diagnosa);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuffer gejalaTerpilih = new StringBuffer();

//                array gejala yang diceklis
                ArrayList<GejalaPenyakit> gejalaList = dataAdapter.gejalaList;
                for (int i = 0; i < gejalaList.size(); i++) {
                    GejalaPenyakit gejala = gejalaList.get(i);
                    if (gejala.isSelected()) { // cek apakah gejala diceklis
//                        masukkan ke variabel responseText dengan pemisah tanda tagar #
                        gejalaTerpilih.append(gejala.getNamaGejala()).append("#");
                    }
                }


//                 jika tidak ada yang diceklis
                if (gejalaTerpilih.toString().equals("")) {
                    Toast.makeText(penyakit.this, "Silakan pilih gejala!", Toast.LENGTH_SHORT).show();

                }
                else {
                    // tampilkan activity hasil diagnosa
                    Intent myIntent = new Intent(v.getContext(), hasil_diagnosa_penyakit.class);
                    myIntent.putExtra("HASIL", gejalaTerpilih.toString());
                    startActivity(myIntent);
                }
            }
        });

    }

    //    custom adapter dari listview yang isinya checkbox daftar gejala
    private class MyCustomAdapter extends ArrayAdapter<GejalaPenyakit> {

        private ArrayList<GejalaPenyakit> gejalaList;

        MyCustomAdapter(Context context, int textViewResourceId,
                        ArrayList<GejalaPenyakit> gejalaList) {
            super(context, textViewResourceId, gejalaList);
            this.gejalaList = new ArrayList<GejalaPenyakit>();
            this.gejalaList.addAll(gejalaList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            penyakit.MyCustomAdapter.ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_gejala_penyakit, null);

                holder = new penyakit.MyCustomAdapter.ViewHolder();
                holder.name = convertView.findViewById(R.id.check_gejala);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        GejalaPenyakit gejala = (GejalaPenyakit) cb.getTag();
                        gejala.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (penyakit.MyCustomAdapter.ViewHolder) convertView.getTag();
            }

            GejalaPenyakit gejala = gejalaList.get(position);
            holder.name.setText(gejala.getNamaGejala());
            holder.name.setChecked(gejala.isSelected());
            holder.name.setTag(gejala);

            return convertView;
        }
    }

}