package id.codemerindu.siakad;

import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
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

public class daftarUlang extends AppCompatActivity {

    private RecyclerView tampilResult;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    AlertDialog dialog;
    String url_daftarUlang= Server.URL+"siswa.php?aksi=daftar_ulang";
    ArrayList<HashMap<String ,String>> list_data;
    private SearchView cari;

    AdapterListDaftarUlang adapterListDaftarUlang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_ulang);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarDaftarUlang);
        toolbar.setTitle("Daftar Ulang");
        setSupportActionBar(toolbar);
        cari = (SearchView) findViewById(R.id.cariNoUn);

        cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterListDaftarUlang.filter(newText);
                adapterListDaftarUlang.notifyDataSetChanged();
                return false;
            }
        });

        tampilResult = (RecyclerView) findViewById(R.id.tampilResult);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        tampilResult.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();

        requestQueue = Volley.newRequestQueue(daftarUlang.this);
        stringRequest = new StringRequest(Request.Method.GET, url_daftarUlang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);
                    for (int i =0; i<dataArray.length(); i++)
                    {

                        JSONObject json = dataArray.getJSONObject(i);
                        HashMap<String, String > map = new HashMap<String , String >();
                        map.put("id_siswa", json.getString("id_siswa"));
                        map.put("nama",json.getString("nama"));
                        map.put("kode_kelas",json.getString("kode_kelas"));
                        map.put("kode_jurusan",json.getString("kode_jurusan"));
                        map.put("no_unsmp",json.getString("no_unsmp"));
                        list_data.add(map);
                        adapterListDaftarUlang = new AdapterListDaftarUlang(daftarUlang.this, list_data);
                        tampilResult.setAdapter(adapterListDaftarUlang);

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
                Toast.makeText(daftarUlang.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
