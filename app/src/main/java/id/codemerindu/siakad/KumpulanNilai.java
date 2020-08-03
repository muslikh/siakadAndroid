package id.codemerindu.siakad;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class KumpulanNilai extends AppCompatActivity {


    final String url = Server.URL+"siswa/nilai?users_id=";
    final String url_nilairapor = Server.URL+"siswa/nilairapor?users_id=";

    ProgressDialog progressDialog;
    public int extraId;
    Boolean session = false;
    SharedPreferences sharedpreferences;
    RequestQueue requestQueue;
    public final static String TAG_IDU = "idu";
    String id;
    Button btn1,btn2,btn3,btn4,btn5;
    TableLayout headertabelNilai;
    String semester;
    Spinner spinnerSemester;
    TextView tvSemester,ratapengetahuan,rataketerampilan,totalrata;
    RecyclerView recyclerView;
    AdapterNilai adapterNilai;
    ArrayList<HashMap<String, String>> list_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kumpulan_nilai);

        Toolbar toolbarDkn = findViewById(R.id.toolbarDKN);
        setSupportActionBar(toolbarDkn);
        getSupportActionBar().setTitle("Data Nilai Rapor");
        recyclerView = (RecyclerView) findViewById(R.id.listNilai);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();

        tvSemester = (TextView) findViewById(R.id.tvSemester);
        headertabelNilai = findViewById(R.id.headertabelNilai);

        ratapengetahuan = findViewById(R.id.rataP);
        rataketerampilan = findViewById(R.id.rataK);
        totalrata = findViewById(R.id.TotalRata);

        spinnerSemester = (Spinner) findViewById(R.id.spinSemseter);
        spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tvSemester.setText(String.valueOf(spinnerSemester.getSelectedItemPosition()).toString());
                list_data.clear();
                getDataNilai();
                getNilaiAkhir();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        getDataNilai();
//        getNilaiAkhir();
    }


    public void getDataNilai()
    {
//        progressDialog = new ProgressDialog(KumpulanNilai.this);
//        progressDialog.setMessage("Proses Simpan, Mohon Tunggu...");
//        progressDialog.show();

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id =  sharedpreferences.getString(TAG_IDU, null);

        extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDU));

        String semester= tvSemester.getText().toString();
        RequestQueue  requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url+extraId+"&semester="+semester, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray dataArray= new JSONArray(response);

//                            progressDialog.dismiss();
                            for (int i =0; i<dataArray.length(); i++)
                            {
                                headertabelNilai.setVisibility(View.VISIBLE);

                                HashMap<String, String > map = new HashMap<String , String >();

                                JSONObject json = dataArray.getJSONObject(i);

                                int key = i+1;
                                map.put("noNilai", Integer.toString(key));
                                map.put("kkm",json.getString("kkm"));
                                map.put("nama_mapel", json.getString("nama_mapel"));
                                map.put("nilaiAngkaPengetahuan",json.getString("nilaiAngkaPengetahuan"));
                                map.put("nilaiHurufPengetahuan", json.getString("nilaiHurufPengetahuan"));
                                map.put("nilaiAngkaKeterampilan",json.getString("nilaiAngkaKeterampilan"));
                                map.put("nilaiHurufKeterampilan",json.getString("nilaiHurufKeterampilan"));
//                                map.put("semester",json.getString("semester"));
                                list_data.add(map);
                                adapterNilai = new AdapterNilai(KumpulanNilai.this, list_data);

                                recyclerView.setAdapter(adapterNilai);

//                                recreate();
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
                        Toast.makeText(KumpulanNilai.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(stringRequests);
    }

    public void getNilaiAkhir()
    {
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id =  sharedpreferences.getString(TAG_IDU, null);

        extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDU));

        String semester= tvSemester.getText().toString();
        RequestQueue  requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url_nilairapor+extraId+"&semester="+semester, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray dataArray= new JSONArray(response);



                            for (int i =0; i<dataArray.length(); i++)
                            {

                                JSONObject json = dataArray.getJSONObject(i);
                                rataketerampilan.setText(json.getString("rerataK"));
                                ratapengetahuan.setText(json.getString("rerataP"));
                                totalrata.setText(json.getString("total"));



//                                recreate();
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
                        Toast.makeText(KumpulanNilai.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(stringRequests);
    }
}
