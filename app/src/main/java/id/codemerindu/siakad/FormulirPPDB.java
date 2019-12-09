package id.codemerindu.siakad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FormulirPPDB extends AppCompatActivity {

    private EditText username,password,nisn,nama,jurusan,thnmasuk;
    private Button simpan;
    private String TAG = "tag";
    private String Daftar = "daftar";
    private   String USERNAME_BEFORE = "usernameBefore";
    private    String USERNAME = "username";
    private    String PASSWORD = "password";
    private String url = "http://smknprigen.sch.id/login/mainppdb.php";
    int SOCKET_TIMEOUT = 3000;
    int RETRIES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulir_ppdb);

        final RequestQueue request = Volley.newRequestQueue(getApplicationContext());
        username = (EditText) findViewById(R.id.ppdb_username);
        password = (EditText) findViewById(R.id.ppdb_password);
        nisn = (EditText) findViewById(R.id.ppdb_nisn);
        nama = (EditText) findViewById(R.id.ppdb_nisn);
        jurusan = (EditText) findViewById(R.id.ppdb_jurusan);
        thnmasuk = (EditText) findViewById(R.id.ppdb_thnMasuk);
        simpan = (Button) findViewById(R.id.ppdbSimpan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                map.put(TAG,Daftar);
                map.put(USERNAME,username.getText().toString());
                map.put(PASSWORD,password.getText().toString());
                request.sendPostRequest
            }
        });

        public void sendPostRequest(String url, Map<String, String> params) {
            StringRequest stringRequests =
            new StringRequest(Request.Method.POST, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    onSucces(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mVolleyInterface.onFailed(error);
                }
            });
            stringRequests.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT,RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            request.add(stringRequests);
        }
    }
}
