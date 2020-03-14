package id.codemerindu.siakad;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class Diskusi extends AppCompatActivity {

    EditText tulispesan;
    TextView pesan,pengirim,time_text;
    Button kirim;
    String TAG_IDU = "idu";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    private static final String TAG_LEVEL = "level";
    private RecyclerView listpesan;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    ProgressDialog progressDialog;

    ArrayList<HashMap<String ,String>> txt_message;
    private SearchView cari;
    String levelU;

    AdapterDiskusi adapterDiskusi;

    final String ambil = Server.URL+"diskusi.php";
    final  String  kirimPesan = Server.URL+"kirimDiskusi.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diskusi);
        pesan = (TextView) findViewById(R.id.message_text);
        pengirim =(TextView) findViewById(R.id.name_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.diskusiToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Diskusi");

        ambilData();

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        levelU = sharedpreferences.getString(TAG_LEVEL, null);

        tulispesan = (EditText) findViewById(R.id.messageEdit);
        kirim = (Button) findViewById(R.id.sendButton);
        if (levelU.equals("admin"))
        {
            tulispesan.setEnabled(false);
            tulispesan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Diskusi.this,"Admin Hanya Lihat",Toast.LENGTH_LONG).show();
                }
            });
            kirim.setEnabled(false);
            kirim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(Diskusi.this,"Admin Hanya Lihat",Toast.LENGTH_LONG).show();
                }
            });
        }else{

            tulispesan = (EditText) findViewById(R.id.messageEdit);
            kirim = (Button) findViewById(R.id.sendButton);
            kirim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kirimpesan();
                    recreate();
                }
            });
        }
    }

    private  void ambilData()

    { listpesan = (RecyclerView) findViewById(R.id.messagesList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listpesan.setLayoutManager(llm);
        txt_message = new ArrayList<HashMap<String, String>>();

//
//        progressDialog = new ProgressDialog(Diskusi.this);
//        progressDialog.setMessage("Proses Pengambilan Data, Mohon Tunggu...");
//        progressDialog.show();

        requestQueue = Volley.newRequestQueue(Diskusi.this);
        stringRequest = new StringRequest(Request.Method.GET, ambil, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);
//                    progressDialog.cancel();
                    for (int i =0; i<dataArray.length(); i++)
                    {

                        JSONObject json = dataArray.getJSONObject(i);
                        HashMap<String, String > map = new HashMap<String , String >();
                        map.put("id_siswa", json.getString("id_siswa"));
                        map.put("nama",json.getString("nama"));
                        map.put("pesan",json.getString("pesan"));
                        map.put("waktuKirim",json.getString("waktuKirim"));
                        txt_message.add(map);
                        adapterDiskusi = new AdapterDiskusi(Diskusi.this, txt_message);
                        listpesan.setAdapter(adapterDiskusi);

                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(Diskusi.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void kirimpesan()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, kirimPesan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//                Toast.makeText(FormulirPPDB.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<String, String>();
                Date waktu = new Date();
                String dateFormat = DateFormat.getInstance().format(waktu);
//                    params.put("id_siswaBaru", id_siswaBaru);
                map.put("siswa_id", getIntent().getStringExtra(TAG_IDU));
                map.put("pesan", tulispesan.getText().toString());
                map.put("waktuKirim", dateFormat);
                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed()
    {
        if(levelU.equals("admin")) {
            Intent pindah = new Intent(Diskusi.this, AdminActivity.class);
            startActivity(pindah);
        } else
        {
            Intent pindah = new Intent(Diskusi.this, MainActivity.class);
            startActivity(pindah);
        }
    }
}
