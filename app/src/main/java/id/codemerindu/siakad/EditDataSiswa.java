package id.codemerindu.siakad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class EditDataSiswa extends AppCompatActivity {

    String url = Server.URL +"siswa.php";
    String url_update  = Server.URL+"update.php";
    final String TAG ="Edit";
    public final static String TAG_IDU = "idu";
    public final static String TAG_MESSAGE = "message";
    EditText EdnamaUser,EdttlUser,EdkodeKelas,Edjurusan;
    TextView idUser;
    Button updateSiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_siswa);
        editData(url);
    }

    public void editData(String url)
    {
        idUser = (TextView)  findViewById(R.id.idUser);
        EdnamaUser = (EditText) findViewById(R.id.namaUser);
        EdttlUser = (EditText) findViewById(R.id.ttlUser);
        EdkodeKelas = (EditText) findViewById(R.id.kodeKelasUser);
        Edjurusan = (EditText)  findViewById(R.id.jurusanUser);
        updateSiswa = (Button)  findViewById(R.id.updateSiswa);
        updateSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });

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
                                int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDU));
                                String nama = obj.getString("nama");
                                int id = obj.getInt("id_siswa");
                                String id_siswa = obj.getString("id_siswa");
                                String tempatLahir = obj.getString("tempat_lahir");
                                String tanggalLahir = obj.getString("tanggal_lahir");
                                String kodekelas = obj.getString("kode_kelas");
                                String jurusanS = obj.getString("kode_jurusan");
                                if (extraId== id )
                                {
                                   idUser.setText(id_siswa);
                                    EdnamaUser.setText(nama);
                                    EdttlUser.setText(tempatLahir +","+ tanggalLahir);
                                    EdkodeKelas.setText(kodekelas);
                                    Edjurusan.setText(jurusanS);
                                }
                            }
                            Log.d(TAG, "onResponse:" + response);
                        }  catch(
                                JSONException e)

                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        EdnamaUser.setText(error.getLocalizedMessage());
                    }
                });
        requestQueue.add(stringRequests);
    }

    private void simpan()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);


                    Toast.makeText(EditDataSiswa.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    // adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(EditDataSiswa.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();

                map.put("id_siswa", idUser.getText().toString());
                map.put("nama", EdnamaUser.getText().toString());
                map.put("kode_jurusan", Edjurusan.getText().toString());
                map.put("kode_kelas", EdkodeKelas.getText().toString());
                // params.put("tahun_nmasuk", thnmasuk.getText().toString());

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
