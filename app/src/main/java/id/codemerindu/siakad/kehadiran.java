package id.codemerindu.siakad;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.GridLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;


public class kehadiran extends AppCompatActivity {

    public  static String url = Server.URL+"jmlabsensi?kode_kelas=";
    public  static String url_log = Server.URL+"logabsensi?kode_kelas=";
    public  static String url_hitung = Server.URL+"hitungabsensi?kode_kelas=";
    public  static String cek_absen = Server.URL+"cekabsen?kode_kelas=";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    TextView btn_absenMasuk,btn_absenPulang,tes,formijinsakit;
    CardView cardAbsen,cardIjin,card_pesanCekAbsen;
    String TAG_ID = "id";
    String TAG_IDU = "id";
    public final static String TAG_KELAS = "kode_kelas";
    public final static String TAG_SEMESTER = "semester";

    String id,kelas,semester,jam,tgl;
    int extraId;
    TextView jmlHadir, jmlSakit, jmlIjin, jmlAlpha;


    RequestQueue requestQueue;
    ArrayList<HashMap<String ,String>> list_data;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    AdapterLogAbsensi adapterLogAbsensi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kehadiran);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarKehadiran);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kehadiran");

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        kelas = sharedpreferences.getString(TAG_KELAS, null);
        semester = sharedpreferences.getString(TAG_SEMESTER, null);

//        kelas = getIntent().getStringExtra(TAG_KELAS);
//        semester = getIntent().getStringExtra(TAG_SEMESTER);

         jam = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
         tgl = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        jmlHadir = findViewById(R.id.jmlHadir);
        jmlSakit = findViewById(R.id.jmlSakit);
        jmlIjin = findViewById(R.id.jmlIjin);
        jmlAlpha = findViewById(R.id.jmlAlpha);

        cardAbsen = findViewById(R.id.cardAbsen);
        cardIjin = findViewById(R.id.cardIjin);
        card_pesanCekAbsen = findViewById(R.id.card_pesanCekAbsen);
//        tes = findViewById(R.id.isiNo);
//        tes.setText(semester);
        btn_absenMasuk = (TextView) findViewById(R.id.absenMasuk);
        btn_absenMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AbsenMasuk = new Intent(kehadiran.this,AbsenQR.class);
                AbsenMasuk.putExtra(TAG_ID,id);
                AbsenMasuk.putExtra(TAG_KELAS,kelas);
                AbsenMasuk.putExtra(TAG_SEMESTER,semester);
                AbsenMasuk.putExtra("jenis","masuk");
                startActivity(AbsenMasuk);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.logabsensi);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        list_data = new ArrayList<HashMap<String, String>>();


//        btn_absenPulang = (TextView) findViewById(R.id.absenPulang);
//        btn_absenPulang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent AbsenPulang = new Intent(kehadiran.this,AbsenQR.class);
//                AbsenPulang.putExtra(TAG_ID,id);
//                AbsenPulang.putExtra(TAG_KELAS,kelas);
//                AbsenPulang.putExtra(TAG_SEMESTER,semester);
//                AbsenPulang.putExtra("jenis","pulang");
//                startActivity(AbsenPulang);
//            }
//        });
        formijinsakit = findViewById(R.id.formtidakmasuk);
        formijinsakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent formisian = new Intent(kehadiran.this,FormIjinSakit.class);
                formisian.putExtra(TAG_ID,id);
                formisian.putExtra(TAG_KELAS,kelas);
                formisian.putExtra(TAG_SEMESTER,semester);
                startActivity(formisian);
            }
        });

        AmbilJmlAbsen();
        AmbilLogABsen();
        hitungAbsen();
        cekAbsen();

    }

    public void AmbilJmlAbsen()
    {



        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url+kelas+"&semester="+semester+"&siswaID="+id, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray = new JSONArray(response);

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject obj = dataArray.getJSONObject(i);

//                                int id = obj.getInt("siswaID");
//                                if (extraId == id) {
                                    jmlHadir.setText(obj.getString("hadir"));
                                    jmlSakit.setText(obj.getString("sakit"));
                                    jmlIjin.setText(obj.getString("ijin"));
                                    jmlAlpha.setText(obj.getString("alpha"));
//                                }
                            }
                        }  catch(
                                JSONException e)

                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        namaUser.setText(error.getLocalizedMessage());
                    }
                });
        requestQueue.add(stringRequests);

    }
    public void hitungAbsen()
    {



        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url_hitung+kelas+"&semester="+semester+"&siswaID="+id, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray = new JSONArray(response);

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject obj = dataArray.getJSONObject(i);

//                                int id = obj.getInt("siswaID");
//                                if (extraId == id) {
                                jmlHadir.setText(obj.getString("hadir"));
                                jmlSakit.setText(obj.getString("sakit"));
                                jmlIjin.setText(obj.getString("ijin"));
                                jmlAlpha.setText(obj.getString("alpha"));
//                                }
                            }
                        }  catch(
                                JSONException e)

                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        namaUser.setText(error.getLocalizedMessage());
                    }
                });
        requestQueue.add(stringRequests);

    }


    public void AmbilLogABsen()
    {


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url_log+kelas+"&semester="+semester+"&siswaID="+id, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

                            for (int i =0; i<dataArray.length(); i++)
                            {

                                JSONObject json = dataArray.getJSONObject(i);
                                HashMap<String, String > map = new HashMap<String , String >();

                                int key = i+1;
                                map.put("no", Integer.toString(key));
//                                map.put("siswaID",json.getString("siswaID"));
                                map.put("tgl",json.getString("tgl"));
                                map.put("jam",json.getString("jam"));
                                map.put("status",json.getString("status"));
//                                map.put("keterangan",json.getString("keterangan"));
                                list_data.add(map);
                                adapterLogAbsensi = new AdapterLogAbsensi(getApplicationContext(), list_data);
                                mRecyclerView.setAdapter(adapterLogAbsensi);

//                                    no.setText("1" + (Integer.parseInt(no.getText().toString()) + 1));
//                                hari.setText(obj.getString("hari"));
//                                    jamke.setText(obj.getString("jam_ke"));
//                                    namaGuru.setText(obj.getString("nama_guru"));
//                                    namaMapel.setText(obj.getString("id_mapel"));

                            }
                        }  catch(
                                JSONException e)

                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequests);
    }


    public void cekAbsen()
    {

        String tgl = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, cek_absen+kelas+"&semester="+semester+"&siswaID="+id+"&tgl="+tgl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

                            for (int i =0; i<dataArray.length(); i++)
                            {

                                JSONObject json = dataArray.getJSONObject(i);

                                String pesan = json.getString("message");
                                if(pesan.equals("sudah_absen"))
                                {
                                    cardIjin.setVisibility(View.GONE);
                                    cardAbsen.setVisibility(View.GONE);
                                    card_pesanCekAbsen.setVisibility(View.VISIBLE);

                                }else {

                                    cardIjin.setVisibility(View.VISIBLE);
                                    cardAbsen.setVisibility(View.VISIBLE);
                                    card_pesanCekAbsen.setVisibility(View.GONE);
                                }


                            }
                        }  catch(
                                JSONException e)

                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequests);
    }

    @Override
    public void onBackPressed()
    {

        Intent pindah = new Intent(kehadiran.this, MainActivity.class);

        startActivity(pindah);
//        }
    }
}
