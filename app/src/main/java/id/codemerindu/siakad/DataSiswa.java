package id.codemerindu.siakad;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

    Button refreshdata,hpsiswa;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
//    EditText caridata;

    String url_sbaru = "http://smknprigen.sch.id/siakad/api/siswa.php";
    String url_hpus = "http://smknprigen.sch.id/siakad/api/delete.php";
    ArrayList<HashMap<String ,String>> list_data;

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_siswa);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Siswa");

        hpsiswa = (Button) findViewById(R.id.hpssiswa);
        hpsiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DataSiswa.this);
                alert
                        .setMessage("Yakin Hapus Semua Data")
                        .setCancelable(false)
                        .setPositiveButton("Iyya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hpsiswa();
                                recreate();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                recreate();
                            }
                        });

                AlertDialog hpus = alert.create();
                hpus.show();
            }
        });
        refreshdata = (Button) findViewById(R.id.refreshdata);
        refreshdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                refresh();
                recreate();
            }
        });
        lvsbaru = (RecyclerView) findViewById(R.id.lvsbaru);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        lvsbaru.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();


//        caridata = (EditText) findViewById(R.id.caridata);
//
//        caridata.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence cs, int arg1, int arg2,
//                                      int arg3) {
//                DataSiswa.this.adapter.getFilter().filter(cs);
//            }
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1,
//                                          int arg2, int arg3) {
//            }
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                // TODO Auto-generated method stub
//            }
//        });

        requestQueue = Volley.newRequestQueue(DataSiswa.this);
        stringRequest = new StringRequest(Request.Method.GET, url_sbaru, new Response.Listener<String>() {
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
                        list_data.add(map);
                        AdapterList adapterList = new AdapterList(DataSiswa.this, list_data);
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


    public void hpsiswa()
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

}
