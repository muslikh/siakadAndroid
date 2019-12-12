package id.codemerindu.siakad;

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
    private Button simpan;
    private String TAG = "tag";
    private String TAG_SUCCESSS = "success";
    private String TAG_MESSAGE = "message";
    private static String url = Server.URL + "insert.php";
    private static String url_update = Server.URL + "update.php";
   // private String url = "http://smknprigen.sch.id/login/mainppdb.php";
//    int SOCKET_TIMEOUT = 3000;
//    int RETRIES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulir_ppdb);

        final RequestQueue request = Volley.newRequestQueue(getApplicationContext());
        username = (EditText) findViewById(R.id.ppdb_username);
        password = (EditText) findViewById(R.id.ppdb_password);
        nisn = (EditText) findViewById(R.id.ppdb_nisn);
        nama = (EditText) findViewById(R.id.ppdb_namaLengkap);
        jurusan = (EditText) findViewById(R.id.ppdb_jurusan);
        thnmasuk = (EditText) findViewById(R.id.ppdb_thnMasuk);
        simpan = (Button) findViewById(R.id.ppdbSimpan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan_update();
            }
 //               Map<String, String> map = new HashMap<>();
//                map.put(TAG,Daftar);
//                map.put(USERNAME,username.getText().toString());
//                map.put(PASSWORD,password.getText().toString());
//                request.sendPostRequest();
//            }
        });
    }



    private void simpan_update()
    {
//        String url;
//        if(id_siswa.isEmpty())
//        {
//            url = url_insert;
//        }else{
//            url = url_update;
//        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);
                   int  success = dataObj.getInt(TAG_SUCCESSS);

                    // Cek error node pada json
                    if (success == 1) {
//                        Log.d("Add/update", dataObj.toString());

//                        callVolley();
//                        kosong();

                        Toast.makeText(FormulirPPDB.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                       // adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(FormulirPPDB.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(FormulirPPDB.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String,String> getParams(){

//                String usernameS = username.getText().toString();
//                String passwordS = password.getText().toString();
//                String namaS = nama.getText().toString();
//                String jurusanS = jurusan.getText().toString();
//                String nisnS = nisn.getText().toString();
//                String thnmasukS = thnmasuk.getText().toString();
              //  String  id_siswaBaru = "1000";
//                for(i=0;<array.si)
                // Posting parameters ke post url
                    Map<String,String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
//                if (id_siswa.isEmpty()){
//                    params.put("username", usernameS);
//                    params.put("password", passwordS);
//                    params.put("nama", nama);
//                    params.put("jurusan", jurusanS);
//                    params.put("nisn", nisnS);
//                    params.put("thnmasuk", thnmasukS);
//                } else {
                   // params.put("id", getString(id_siswa));
//                    params.put("id_siswaBaru", id_siswaBaru);
                    params.put("username", username.getText().toString());
                    params.put("password", password.getText().toString());
                    params.put("nama", nama.getText().toString());
                    params.put("kode_jurusan", jurusan.getText().toString());
                    params.put("nisn", nisn.getText().toString());
                    params.put("tahun_nmasuk", thnmasuk.getText().toString());
                //}
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
