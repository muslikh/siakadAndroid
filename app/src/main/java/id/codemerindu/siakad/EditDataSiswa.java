package id.codemerindu.siakad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditDataSiswa extends AppCompatActivity {

    String url = Server.URL +"siswa.php";
    final String TAG ="Edit";
    public final static String TAG_IDU = "idu";
    EditText EdnamaUser,EdttlUser,EdkodeKelas,Edjurusan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_siswa);
        editData(url);
    }

    public void editData(String url)
    {

        EdnamaUser = (EditText) findViewById(R.id.namaUser);
        EdttlUser = (EditText) findViewById(R.id.ttlUser);
        EdkodeKelas = (EditText) findViewById(R.id.kodeKelasUser);
        Edjurusan = (EditText)  findViewById(R.id.jurusanUser);
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
                                String tempatLahir = obj.getString("tempat_lahir");
                                String tanggalLahir = obj.getString("tanggal_lahir");
                                String kodekelas = obj.getString("kode_kelas");
                                String jurusanS = obj.getString("kode_jurusan");
                                if (extraId== id )
                                {
                                    EdnamaUser.setHint(nama);
                                    EdttlUser.setHint(tempatLahir +","+ tanggalLahir);
                                    EdkodeKelas.setHint(kodekelas);
                                    Edjurusan.setHint(jurusanS);
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
}
