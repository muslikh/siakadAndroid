package id.codemerindu.siakad;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;


public class kehadiran extends AppCompatActivity {


    TextView btn_absenMasuk,btn_absenPulang,tes;
    String TAG_IDU = "idu";
    String TAG_Jenis = "jenis";
    String idu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kehadiran);

        idu = getIntent().getStringExtra(TAG_IDU);
        btn_absenMasuk = (TextView) findViewById(R.id.absenMasuk);
        btn_absenMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AbsenMasuk = new Intent(kehadiran.this,AbsenQR.class);
                AbsenMasuk.putExtra(TAG_IDU,idu);
                AbsenMasuk.putExtra("jenis","masuk");
                startActivity(AbsenMasuk);
            }
        });

        btn_absenPulang = (TextView) findViewById(R.id.absenPulang);
        btn_absenPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AbsenPulang = new Intent(kehadiran.this,AbsenQR.class);
                AbsenPulang.putExtra(TAG_IDU,idu);
                AbsenPulang.putExtra("jenis","pulang");
                startActivity(AbsenPulang);
            }
        });

        tes = findViewById(R.id.tesIDU);
        tes.setText(idu);

    }

}
