package id.codemerindu.siakad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class validasiPPDB extends AppCompatActivity {

    final String url = Server.URL+"siswabaru.php";
    final String url_pindah = Server.URL+"pindah.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validasi_ppdb);

        DtSiswaBaru();

        Button validasi = (Button) findViewById(R.id.aksiValidasi);
        validasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pindah();
            }
        });
//        TableRow tableRow = new TableRow(this);
//        tableRow.setClickable(true);  //allows you to select a specific row
//
//        tableRow.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                TableRow tablerow = (TableRow) view;
//                TextView sample = (TextView) tablerow.getChildAt(1);
//                String result=sample.getText().toString();
//
//                Toast toast = Toast.makeText(validasiPPDB.this, "wes", Toast.LENGTH_LONG);
//                toast.show();
//            }
//        });


        TableLayout tableLayout = (TableLayout) this.findViewById(R.id.tabelJadwal);
        TableRow row = (TableRow)getLayoutInflater().inflate(R.layout.jadwal_row, null);
        ((TextView)row.findViewById(R.id.noTabel)).setText("2");
        ((TextView)row.findViewById(R.id.jamTabel)).setText("8 - 10 ");
        ((TextView)row.findViewById(R.id.GuruTabel)).setText("aku ");
        ((TextView)row.findViewById(R.id.pelajaranTabel)).setText("validasi ");
    }

    public void pindah()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
        requestQueue.add(stringRequests);
    }

    public void DtSiswaBaru()
    {
       final TextView nisn = (TextView) findViewById(R.id.nisn);
        final   TextView nomor = (TextView) findViewById(R.id.nomor);
        final  TextView nama = (TextView) findViewById(R.id.nama);
        final  TextView program = (TextView) findViewById(R.id.programkeahlian);


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

                            for (int i =0; i<dataArray.length(); i++)
                            {

                                JSONObject obj = dataArray.getJSONObject(i);
                                nisn.setText(obj.getString("nisn"));
                                nomor.setText(obj.getString("id_siswaBaru"));
                                nama.setText(obj.getString("nama"));
                                program.setText(obj.getString("program"));



                            }
                        }  catch(
                                JSONException e)

                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        nama.setText(error.getLocalizedMessage());
                    }
                });
        requestQueue.add(stringRequests);
    }
}
