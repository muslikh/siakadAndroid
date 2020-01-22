//package id.codemerindu.siakad;
//
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//public class validasiPPDB extends Fragment{
//
//    final String url = Server.URL+"siswabaru.php";
//    final String url_pindah = Server.URL+"pindah.php";
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        View view=inflater.inflate(R.layout.activity_validasi_ppdb,container,false);
//
////        addHeaders(view);
////        addData();
//        DtSiswaBaru(view);
//
//        Button validasi = (Button) view.findViewById(R.id.aksiValidasi);
//        validasi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pindah();
//            }
//        });
////        TableRow tableRow = new TableRow(this);
////        tableRow.setClickable(true);  //allows you to select a specific row
////
////        tableRow.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View view) {
////                TableRow tablerow = (TableRow) view;
////                TextView sample = (TextView) tablerow.getChildAt(1);
////                String result=sample.getText().toString();
////
////                Toast toast = Toast.makeText(validasiPPDB.this, "wes", Toast.LENGTH_LONG);
////                toast.show();
////            }
////        });
//
////
////        TableLayout tableLayout = (TableLayout) this.findViewById(R.id.tabelJadwal);
////        TableRow row = (TableRow)getLayoutInflater().inflate(R.layout.jadwal_row, null);
////        ((TextView)row.findViewById(R.id.noTabel)).setText("2");
////        ((TextView)row.findViewById(R.id.jamTabel)).setText("8 - 10 ");
////        ((TextView)row.findViewById(R.id.GuruTabel)).setText("aku ");
////        ((TextView)row.findViewById(R.id.pelajaranTabel)).setText("validasi ");
//        return view;
//    }
//
//    public void pindah()
//    {
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        StringRequest stringRequests =
//                new StringRequest(Request.Method.POST, url_pindah, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray dataArray= new JSONArray(response);
//
//                        }  catch(
//                                JSONException e)
//
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//        requestQueue.add(stringRequests);
//    }
//
//    public void DtSiswaBaru(final  View v)
//    {
//       final TextView nisn = (TextView) v.findViewById(R.id.nisn);
//        final   TextView nomor = (TextView) v.findViewById(R.id.nomor);
//        final  TextView nama = (TextView) v.findViewById(R.id.nama);
//        final  TextView program = (TextView) v.findViewById(R.id.programkeahlian);
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        StringRequest stringRequests =
//                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject dataObj= new JSONObject(response);
//                            JSONArray dataArray = dataObj.getJSONArray("siswabaru");
//
//                            //TableLayout tv = (TableLayout) v.findViewById(R.id.TBSiswaBaru);
//                           // tv.removeViewInLayout(v);
//                          //  int flag = 1;
//
//                            int no = 0;
//                            for (int i =0; i<dataArray.length(); i++)
//                            {
//                                    JSONObject obj = dataArray.getJSONObject(i);
//
////                                TableRow tr = new TableRow(getActivity());
////                                tr.setLayoutParams(new ViewGroup.LayoutParams(
////                                        ViewGroup.LayoutParams.FILL_PARENT,
////                                        ViewGroup.LayoutParams.WRAP_CONTENT
////                                ));
////
//////                                if (flag == 1) {
////
////                                    TextView b6 = new TextView(getActivity());
////                                    b6.setText("No");
////                                    tr.addView(b6);
////                                    TextView b19 = new TextView(getActivity());
////                                    b19.setText("Nama Lengkap");
////                                    tr.addView(b19);
////                                    TextView b29 = new TextView(getActivity());
////                                    b29.setText("Program Keahlian");
////                                    tr.addView(b29);
////                                    tv.addView(tr);
////                                    final View vline = new View(getActivity());
////                                    vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 2));
////                                    vline.setBackgroundColor(Color.BLUE);
////                                    tv.addView(vline);
////                                    flag = 0;
////                                }else{
//
////                                    TextView no = new TextView(getActivity());
////                                    no.setText(obj.getString("id_siswaBaru"));
////
////                                   no.setTextColor(Color.BLACK);
////                                    tr.addView(no);
////
////                                    TextView nama = new TextView(getActivity());
////                                    nama.setText(obj.getString("nama"));
////                                   nama.setTextColor(Color.BLACK);
////                                    tr.addView(nama);
////                                    TextView program = new TextView(getActivity());
////                                    program.setText(obj.getString("program"));
////                                    tr.addView(program);
////                                    program.setTextColor(Color.BLACK);
////                                    tv.addView(tr);
////                                    final View vline1 = new View(getActivity());
////                                    vline1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
////                                    vline1.setBackgroundColor(Color.BLUE);
////                                    tv.addView(vline1);
//                                no = no++;
//                                nisn.setText(obj.getString("nisn"));
//                                nomor.setText(obj.getString("id_siswaBaru"));
//                                nama.setText(obj.getString("nama"));
//                          //      program.setText(obj.getString("program"));
//                 //             }
//
//
//                            }
//                        }  catch(
//                                JSONException e)
//
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                       // nama.setText(error.getLocalizedMessage());
//                    }
//                });
//        requestQueue.add(stringRequests);
//    }
//
//    private TextView getTextView (int id, String title, int color, int typeface, int bgColor)
//    {
//        TextView tv = new TextView(getActivity());
//        tv.setId(id);
//        tv.setText(title.toUpperCase());
//        tv.setTextColor(color);
//        tv.setTypeface(Typeface.DEFAULT,typeface);
//        return tv;
//
//
//    }
//
//
//    private TableRow.LayoutParams getLayoutParams()
//    {
//        TableRow.LayoutParams params = new TableRow.LayoutParams(
//                TableRow.LayoutParams.MATCH_PARENT,
//                TableRow.LayoutParams.WRAP_CONTENT
//        );
//        return params;
//    }
//
//    private TableLayout.LayoutParams getTBLayoutParams()
//    {
//        return new TableLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//    }
//    public void addData() {
////
////        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
////        StringRequest stringRequests =
////                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
////                    @Override
////                    public void onResponse(String response) {
////                        try {
////                            JSONArray dataArray= new JSONArray(response);
////\
////                            for (int i =0; i<dataArray.length(); i++)
////                            {
////
////                                JSONObject obj = dataArray.getJSONObject(i);
////
////                                    TableRow tr = new TableRow(getActivity());
////                                    tr.setLayoutParams(getLayoutParams());
////                                   //r.addView(getTextView(i + 1, dataArray[i], Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
////                                    tr.addView(getTextView(i + numCompanies, os[i], Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
////                                    tl.addView(tr, getTBLayoutParams());
////                                }
////
////
////                        }  catch(
////                                JSONException e);
////
////                        {
////                            e.printStackTrace();
////                        }
////                    }
////                }, new Response.ErrorListener() {
////                    @Override
////                    public void onErrorResponse(VolleyError error) {
////                        // nama.setText(error.getLocalizedMessage());
////                    }
////                });
////        requestQueue.add(stringRequests);
//
//    }
//
//    public void addHeaders(View view)
//    {
//        TableLayout tl = view.findViewById(R.id.TBSiswaBaru);
//        TableRow tr = new TableRow(getActivity());
//
//        tr.setLayoutParams(getLayoutParams());
//        tr.addView(getTextView(0, "COMPANY", Color.WHITE, Typeface.BOLD, Color.BLUE));
//        tr.addView(getTextView(0, "OS", Color.WHITE, Typeface.BOLD, Color.BLUE));
//        tl.addView(tr, getTBLayoutParams());
//    }
//
//  //}
//
//
//}


package id.codemerindu.siakad;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class validasiPPDB extends AppCompatActivity {

    private RecyclerView lvsbaru;
    TextView validsemua,aksibnt;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    SearchView cari;
    String url_setting = Server.URL+"setting.php";
    AdapterSbaru adapterSbaru;
    String url_sbaru = Server.URL+"siswabaru.php";
    String url_pindah = Server.URL+"pindah.php";
    ArrayList<HashMap<String ,String>> list_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validasi_ppdb);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarvalid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Validasi Siswa Baru");


        validsemua = (TextView) findViewById(R.id.validasisemua);
        validsemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pindah();
            }
        });
        aksibnt = (TextView) findViewById(R.id.aksibtn);


        lvsbaru = (RecyclerView) findViewById(R.id.lvsbaru);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        lvsbaru.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();

        cari = (SearchView) findViewById(R.id.cari);

        cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterSbaru.filter(newText);
                adapterSbaru.notifyDataSetChanged();
                return false;
            }
        });

        requestQueue = Volley.newRequestQueue(validasiPPDB.this);
        stringRequest = new StringRequest(Request.Method.GET, url_sbaru, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);

                    for (int i =0; i<dataArray.length(); i++)
                    {

                        JSONObject json = dataArray.getJSONObject(i);
                        HashMap<String, String > map = new HashMap<String , String >();
                        map.put("id_siswaBaru", json.getString("id_siswaBaru"));
                        map.put("nama",json.getString("nama"));
                         map.put("kode_kelas",json.getString("kode_kelas"));
                        map.put("kode_jurusan",json.getString("kode_jurusan"));
                        map.put("nisn",json.getString("nisn"));
                        list_data.add(map);
                        adapterSbaru = new AdapterSbaru(validasiPPDB.this, list_data);
                        lvsbaru.setAdapter(adapterSbaru);

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
                Toast.makeText(validasiPPDB.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }


    public void pindah()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(validasiPPDB.this);
        StringRequest stringRequests =
                new StringRequest(Request.Method.POST, url_pindah, new Response.Listener<String>() {
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

        Intent refresh=new Intent(validasiPPDB.this,DataSiswa.class);
        startActivity(refresh);
        requestQueue.add(stringRequests);
    }

    public void onBackPressed()
    {

            Intent intent = new Intent(validasiPPDB.this,AdminActivity.class);
            startActivity(intent);
            return;

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_ppdb, menu);

        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {


        menu.findItem(R.id.Aktifkan);

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



       // final View login = LayoutInflater.from(this).inflate(R.layout.login,null);


        if (id == R.id.Aktifkan) {

            aksibnt.setText("Aktif");
            Setting();
//            TextView isiForm = (TextView) login.findViewById(R.id.btn_isiFormulir);
//            isiForm.setCursorVisible(false);
        }else if (id == R.id.Matikan)
        {

            aksibnt.setText("Mati");
            Setting();
        }

        return false;
    }
    private void Setting()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_setting, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);




                    //Toast.makeText(FormulirPPDB.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    // adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Error: " + error.getMessage());
                //Toast.makeText(FormulirPPDB.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<String, String>();
//                    params.put("id_siswaBaru", id_siswaBaru);
                map.put("id_setting","1");
                map.put("aksimenu", aksibnt.getText().toString());
                map.put("tahun_ajaran", "2020/2021");
                //  params.put("tahun_nmasuk", thnmasuk.getText().toString());
                //}
                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
