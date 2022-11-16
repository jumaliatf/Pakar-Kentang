package com.example.pakar_kentang;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class hasil_diagnosa_penyakit extends AppCompatActivity {

    private ImageButton ulangi;
    int imagevalue;

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_diagnosa_penyakit);

//        load database
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        if (mDBHelper.openDatabase())
            db = mDBHelper.getReadableDatabase();

        String str_hasil = getIntent().getStringExtra("HASIL"); //ambil hasil diagnosa dari activity diagnosa
        String[] gejala_terpilih = new String[0];
        if (str_hasil != null) {
//            pisahkan berdasarkan tanda # dan masukkan ke array
            gejala_terpilih = str_hasil.split("#");
        }

        Log.d("debug", Arrays.toString(gejala_terpilih));

        HashMap<String, Double> mapHasil = new HashMap<>(); //untuk menyimpan hasil penilaian CF

        String query_penyakit = "SELECT kode_penyakit FROM penyakit order by kode_penyakit";
        Cursor cursor_penyakit = db.rawQuery(query_penyakit, null);

        while (cursor_penyakit.moveToNext()) {
            double gejala_matching = 0.0;

            String query_get_gejala = "SELECT * FROM rule_penyakit WHERE kode_penyakit = '" + cursor_penyakit.getString(0) + "'";
            int count_gejala = db.rawQuery(query_get_gejala, null).getCount();
//            Log.d("debug", String.valueOf(count_gejala));

            String query_rule = "SELECT kode_gejala FROM rule_penyakit where kode_penyakit = '" + cursor_penyakit.getString(0) + "'";
            Cursor cursor_rule = db.rawQuery(query_rule, null);

//            loop rule
            while (cursor_rule.moveToNext()){
                for (String s_gejala_terpilih : gejala_terpilih){
                    String query_gejala = "SELECT kode_gejala FROM gejala_penyakit where nama_gejala = '" + s_gejala_terpilih + "'";
                    Cursor cursor_gejala = db.rawQuery(query_gejala, null);
                    cursor_gejala.moveToFirst();

                    if(cursor_rule.getString(0).equals(cursor_gejala.getString(0))){
                        gejala_matching = gejala_matching + 1.0;
                    }
                    cursor_gejala.close();
                }
            }
            Log.d("debug", String.valueOf(gejala_matching));
            cursor_rule.close();
            mapHasil.put(cursor_penyakit.getString(0), (gejala_matching / count_gejala) * 100); //nilai dikali 100 agar hasilnya berupa persentase
        }

        cursor_penyakit.close();
        Log.d("debug", String.valueOf(mapHasil));

        //        pengurutan hasil penilaian CF berdasarkan nilai terbesar
        Map<String, Double> sortedHasil = sortByValue(mapHasil);

        // ambil kode penyakit dengan nilai terbesar
        Map.Entry<String, Double> entry = sortedHasil.entrySet().iterator().next();
        String kode_penyakit = entry.getKey();
        double hasil_cf = entry.getValue();
        Log.d("debughasil", String.valueOf(hasil_cf));
        Log.d("debugnama", String.valueOf(kode_penyakit));

        int persentase = (int) hasil_cf;

        //        ambil data penyakit dari database
        String query_penyakit_hasil = "SELECT * FROM penyakit where kode_penyakit='" + kode_penyakit + "'";
        Cursor cursor_hasil = db.rawQuery(query_penyakit_hasil, null);
        cursor_hasil.moveToFirst();

        TextView tv_nama_penyakit = findViewById(R.id.tvResult);
        tv_nama_penyakit.setText(cursor_hasil.getString(1));

        TextView tv_persentase = findViewById(R.id.percent);
        tv_persentase.setText(persentase+"%");

        TextView ketpenyakit = findViewById(R.id.ketpenyakit);
        ketpenyakit.setText(cursor_hasil.getString(2));

//        transform solusi
        String[] splitSolusi = new String[0];
        TextView solusi = findViewById(R.id.solusi);
        splitSolusi = cursor_hasil.getString(3).split("#");

        String solusiList = "";
        for (int i = 0; i < splitSolusi.length; i++){
            solusiList += "<li>" + splitSolusi[i] + "</li>";
        }
        solusi.setText(Html.fromHtml("<ol>" + solusiList + "</ol>"));

        ImageView penyakit_image = (ImageView) findViewById(R.id.gmbrpenyakit);

        String pathImg = "@drawable/" + cursor_hasil.getString(4);
        int imageResource = getResources().getIdentifier(pathImg, null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        penyakit_image.setImageDrawable(res);

        cursor_hasil.close();


        ulangi = (ImageButton) findViewById(R.id.button_ulangi);
        ulangi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(hasil_diagnosa_penyakit.this, penyakit.class));
                finish();
            }
        });
    }

    // fungsi untuk mengurutkan nilai dari yang terbesar
    public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double>> list =
                new LinkedList<Map.Entry<String, Double>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    //    biar tombol back di toolbar dan tombol back di device tidak me restart menu sebelumnya/menu activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}