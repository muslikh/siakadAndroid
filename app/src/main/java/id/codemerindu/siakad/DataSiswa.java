package id.codemerindu.siakad;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class DataSiswa extends AppCompatActivity {

    private RecyclerView lvsbaru;

    Button tambah;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    ProgressDialog progressDialog;

    AlertDialog dialog;
    String url_sbaru = Server.URL+"siswa.php";
    String url_hpus = Server.URL+"delete.php";
    ArrayList<HashMap<String ,String>> list_data;
    private SearchView cari;

    AdapterList adapterList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_siswa);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        tambah = (Button) findViewById(R.id.tambahDatasiswa);
//        tambah.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent tmbhData = new Intent(DataSiswa.this,FormulirPPDB.class);
//                startActivity(tmbhData);
//            }
//        });

        cari = (SearchView) findViewById(R.id.cari);

        cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterList.filter(newText);
                adapterList.notifyDataSetChanged();
                return false;
            }
        });
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Intent refresh=new Intent(DataSiswa.this,DataSiswa.class);
                startActivity(refresh);
                pullToRefresh.setRefreshing(false);
            }
        });
        lvsbaru = (RecyclerView) findViewById(R.id.lvsbaru);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        lvsbaru.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();


        progressDialog = new ProgressDialog(DataSiswa.this);
        progressDialog.setMessage("Proses Pengambilan Data, Mohon Tunggu...");
        progressDialog.show();

        requestQueue = Volley.newRequestQueue(DataSiswa.this);
        stringRequest = new StringRequest(Request.Method.GET, url_sbaru, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);
                    progressDialog.cancel();
                    for (int i =0; i<dataArray.length(); i++)
                    {

                        JSONObject json = dataArray.getJSONObject(i);
                        HashMap<String, String > map = new HashMap<String , String >();
                        map.put("id_siswa", json.getString("id_siswa"));
                        map.put("nama",json.getString("nama"));
                        map.put("kode_kelas",json.getString("kode_kelas"));
                        map.put("kode_jurusan",json.getString("kode_jurusan"));
                        map.put("tahun_masuk",json.getString("tahun_masuk"));
                        map.put("foto",json.getString("foto"));
                        list_data.add(map);
                       adapterList = new AdapterList(DataSiswa.this, list_data);
                        lvsbaru.setAdapter(adapterList);

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
                Toast.makeText(DataSiswa.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }


    public void hpssemua()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(DataSiswa.this);
        StringRequest stringRequests =
                new StringRequest(Request.Method.POST, url_hpus, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

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
            Intent pindah = new Intent(DataSiswa.this, AdminActivity.class);
            startActivity(pindah);

    }


}
