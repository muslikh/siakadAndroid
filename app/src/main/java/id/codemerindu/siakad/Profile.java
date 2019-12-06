package id.codemerindu.siakad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
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

import java.lang.reflect.Array;
import java.util.Arrays;

public class Profile extends AppCompatActivity {

    TextView namaUser,ttlUser,kodeKelas,jurusan;
    String id;
    SharedPreferences sharedpreferences;
    public final static String TAG="Profile";
    public final static String TAG_IDU = "idu";

    final String url = "http://152746201341.ip-dynamic.com/login/siswa.php";
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedIntanceState)
    {
        super.onCreate(savedIntanceState);
        setContentView(R.layout.profile);

        requestQueue = Volley.newRequestQueue(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("SIAKAD");
        toolbar.setSubtitle("SMK NEGERI PRIGEN");
        toolbar.setLogo(R.mipmap.ic_logo);

        namaUser = (TextView) findViewById(R.id.namaUser);
        ttlUser = (TextView) findViewById(R.id.ttlUser);
        kodeKelas = (TextView) findViewById(R.id.kodeKelasUser);
        jurusan = (TextView) findViewById(R.id.jurusanUser);


        BottomNavigationView bottomNavigationView = findViewById(R.id.aksesMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case  R.id.beranda:
                        Intent beranda = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(beranda);
                        break;
                    case  R.id.grup:
                        break;
                    case  R.id.profil:
//                        Intent profil = new Intent(getApplicationContext(), Profile.class);
//                        startActivity(profil);
                        break;
                }
                return false;
            }
        });

        getData(url);
    }

    public void getData(String url)
    {

        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray dataArray= new JSONArray(response);

                    for (int i =0; i<dataArray.length(); i++)
                    {

                        JSONObject obj = dataArray.getJSONObject(i);
                        int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDU));
                        String nama = obj.getString("nama");
                        int id = obj.getInt("id_siswa");
                        String tempatLahir = obj.getString("tempat_lahir");
                        String tanggalLahir = obj.getString("tanggal_lahir");
                        String kodekelas = obj.getString("kode_kelas");
                        String jurusanS = obj.getString("kode_jurusan");
                        if (extraId== id )
                        {
                            namaUser.setText(nama);
                           ttlUser.setText(tempatLahir +","+ tanggalLahir);
                            kodeKelas.setText(kodekelas);
                            jurusan.setText(jurusanS);
                        }
                    }

//                    JSONObject obj = new JSONObject(response.toString());
//                    JSONArray dataArray= obj.getJSONArray("data");
                    Log.d(TAG, "onResponse:" + response);
                }  catch(
            JSONException e)

            {
                e.printStackTrace();
            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                namaUser.setText(error.getLocalizedMessage());
            }
    });
     requestQueue.add(stringRequests);
    }

//
//    public void getData()
//    {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        final String url = "http://152746201341.ip-dynamic.com/login/siswa.php";
//       final  JSONObject jsonObject = new JSONObject();
//            final String request = jsonObject.toString();
//
//
//            sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try{
//                JSONObject obj = new JSONObject(response.toString());
//                    JSONArray dataArray= obj.getJSONArray("data");
//
//                int i = 28;
//                int idsiswa = Integer.parseInt(dataArray.getJSONObject(i).getString("id_siswa"));
//                int ambilId = Integer.parseInt(getIntent().getStringExtra(TAG_IDU));
//
//                if (ambilId == idsiswa)
//                {
//                    namaUser.setText(dataArray.getJSONObject(i).getString("nama"));
//                }
////                    if( id == 29 )
////                    {
////                        namaUser.setText(jsonObject1.getString("nama"-> id));
////                    }
//            } catch(
//            JSONException e)
//
//            {
//                e.printStackTrace();
//            }
//        }
//    }, new Response.ErrorListener()
//
//    {
//        @Override
//        public void onErrorResponse(VolleyError error)
//            {
//                    Log.d("error",error.toString());
//            }
//        });
//        queue.add(stringRequest);
//    }

}
