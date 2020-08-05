package id.codemerindu.siakad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Pengumuman extends AppCompatActivity {



    private RecyclerView tampilResult;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    public  String tampil = Server.URL+"pengumuman";
    AdapterPengumuman2 adapterPengumuman;
    ArrayList<HashMap<String ,String>> list_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengumuman);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarPengumuman);
        toolbar.setTitle("Pengumuman");
        setSupportActionBar(toolbar);


        tampilResult = (RecyclerView) findViewById(R.id.listPengumuman);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        tampilResult.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();

        requestQueue = Volley.newRequestQueue(Pengumuman.this);
        stringRequest = new StringRequest(Request.Method.GET, tampil, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);
                    for (int i =0; i<dataArray.length(); i++)
                    {

                        JSONObject json = dataArray.getJSONObject(i);
                        HashMap<String, String > map = new HashMap<String , String >();
                        map.put("isi_pengumuman", json.getString("isi_pengumuman"));
                        map.put("judul_pengumuman",json.getString("judul_pengumuman"));
                        map.put("tgl_pengumuman",json.getString("tgl_pengumuman"));
                        map.put("level",json.getString("level"));
                        list_data.add(map);
                        adapterPengumuman = new AdapterPengumuman2(Pengumuman.this, list_data);
                        tampilResult.setAdapter(adapterPengumuman);

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
                Toast.makeText(Pengumuman.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed()
    {
        Intent data = new Intent(Pengumuman.this, Login.class);
        startActivity(data);


    }
  }

