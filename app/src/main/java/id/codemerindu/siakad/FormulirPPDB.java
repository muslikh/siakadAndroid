package id.codemerindu.siakad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FormulirPPDB extends AppCompatActivity {

   private EditText username,password,nisn,nama,jurusan,thnmasuk;

    Button simpan;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulir_ppdb);

        /*get data from intent*/
        Intent data = getIntent();
        final int update = data.getIntExtra("update",0);
        String intent_npm = data.getStringExtra("npm");
        String intent_nama = data.getStringExtra("nama");
        String intent_prodi = data.getStringExtra("prodi");
        String intent_fakultas = data.getStringExtra("fakultas");
        /*end get data from intent*/

        username = (EditText) findViewById(R.id.ppdb_username);
        password = (EditText) findViewById(R.id.ppdb_password);
        nisn = (EditText) findViewById(R.id.ppdb_nisn);
        nama = (EditText) findViewById(R.id.ppdb_namaLengkap);
        jurusan = (EditText) findViewById(R.id.ppdb_jurusan);
        //thnmasuk = (EditText) findViewById(R.id.ppdb_thnMasuk);
        simpan = (Button) findViewById(R.id.ppdbSimpan);
        pd = new ProgressDialog(FormulirPPDB.this);

        /*kondisi update / insert*/
        if(update == 1)
        {
            simpan.setText("Update Data");
            npm.setText(intent_npm);
            npm.setVisibility(View.GONE);
            nama.setText(intent_nama);
            prodi.setText(intent_prodi);
            fakultas.setText(intent_fakultas);

        }


        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(update == 1)
                {
                    Update_data();
                }else {
                    simpanData();
                }
            }
        });

        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(InsertData.this,MainActivity.class);
                startActivity(main);
            }
        });
    }

    private void Update_data()
    {
        pd.setMessage("Update Data");
        pd.setCancelable(false);
        pd.show();

        StringRequest updateReq = new StringRequest(Request.Method.POST, ServerAPI.URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(InsertData.this, "pesan : "+   res.getString("message") , Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        startActivity( new Intent(InsertData.this,MainActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Toast.makeText(InsertData.this, "pesan : Gagal Insert Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("npm",npm.getText().toString());
                map.put("nama",nama.getText().toString());
                map.put("prodi",prodi.getText().toString());
                map.put("fakultas",fakultas.getText().toString());

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(updateReq);
    }



    private void simpanData()
    {

        pd.setMessage("Menyimpan Data");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_INSERT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(InsertData.this, "pesan : "+   res.getString("message") , Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        startActivity( new Intent(InsertData.this,MainActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Toast.makeText(InsertData.this, "pesan : Gagal Insert Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("npm",npm.getText().toString());
                map.put("nama",nama.getText().toString());
                map.put("prodi",prodi.getText().toString());
                map.put("fakultas",fakultas.getText().toString());

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(sendData);
    }

//    private EditText username,password,nisn,nama,jurusan,thnmasuk;
//    private Button simpan;
//    private String TAG = "tag";
//    private String TAG_SUCCESSS = "success";
//    private String TAG_MESSAGE = "message";
//    private static String url = "http://152746201341.ip-dynamic.com/login/volley/insert.php";
//    private static String url_update = Server.URL + "update.php";
//   // private String url = "http://smknprigen.sch.id/login/mainppdb.php";
////    int SOCKET_TIMEOUT = 3000;
////    int RETRIES = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.formulir_ppdb);
//
//        final RequestQueue request = Volley.newRequestQueue(getApplicationContext());
//        username = (EditText) findViewById(R.id.ppdb_username);
//        password = (EditText) findViewById(R.id.ppdb_password);
//        nisn = (EditText) findViewById(R.id.ppdb_nisn);
//        nama = (EditText) findViewById(R.id.ppdb_namaLengkap);
//        jurusan = (EditText) findViewById(R.id.ppdb_jurusan);
//        //thnmasuk = (EditText) findViewById(R.id.ppdb_thnMasuk);
//        simpan = (Button) findViewById(R.id.ppdbSimpan);
//        simpan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                simpan_update();
//            }
// //               Map<String, String> map = new HashMap<>();
////                map.put(TAG,Daftar);
////                map.put(USERNAME,username.getText().toString());
////                map.put(PASSWORD,password.getText().toString());
////                request.sendPostRequest();
////            }
//        });
//    }
//
//
//
//    private void simpan_update()
//    {
////        String url;
////        if(id_siswa.isEmpty())
////        {
////            url = url_insert;
////        }else{
////            url = url_update;
////        }
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject dataObj = new JSONObject(response);
//                   int  success = dataObj.getInt(TAG_SUCCESSS);
//
//                    // Cek error node pada json
//                    if (success == 1) {
////                        Log.d("Add/update", dataObj.toString());
//
////                        callVolley();
////                        kosong();
//
//                        Toast.makeText(FormulirPPDB.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                       // adapter.notifyDataSetChanged();
//
//                    } else {
//                        Toast.makeText(FormulirPPDB.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//                Toast.makeText(FormulirPPDB.this, error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }) {
//
//            @Override
//
//            protected Map<String,String> getParams() throws AuthFailureError{
//
//                    Map<String,String> map = new HashMap<String, String>();
////                    params.put("id_siswaBaru", id_siswaBaru);
//                map.put("username", username.getText().toString());
//                map.put("password", password.getText().toString());
//                map.put("nama", nama.getText().toString());
//                map.put("kode_jurusan", jurusan.getText().toString());
//                map.put("nisn", nisn.getText().toString());
//                   // params.put("tahun_nmasuk", thnmasuk.getText().toString());
//                //}
//                return map;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("Content-Type","application/x-www-form-urlencoded");
//                return params;
//            }
//        };
//
//        AppController.getInstance().addToRequestQueue(stringRequest);
//    }
}
