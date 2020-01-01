package id.codemerindu.siakad;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.zip.Inflater;

public class fragSiswa extends Fragment {

    final String url = Server.URL+"siswa.php";

    TextView namaUser,ttlUser,kodeKelas,jurusan, nisn,nipd,nik,jk,agama,kewarga,anakke,jmlsdrkandung;
    String id;
    SharedPreferences sharedpreferences;
    public final static String TAG="Profile";
    public final static String TAG_IDU = "idu";
    RequestQueue  requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view_fragsiswa = inflater.inflate(R.layout.fragdatapribadi,container,false);

        namaUser = (TextView) view_fragsiswa.findViewById(R.id.namaUser);
        ttlUser = (TextView)  view_fragsiswa.findViewById(R.id.ttlUser);
        kodeKelas = (TextView)  view_fragsiswa.findViewById(R.id.kodeKelasUser);
        jurusan = (TextView)  view_fragsiswa.findViewById(R.id.jurusanUser);
        nisn = (TextView)  view_fragsiswa.findViewById(R.id.nisnUser);
        nipd = (TextView)  view_fragsiswa.findViewById(R.id.nipdUser);
        nik = (TextView)  view_fragsiswa.findViewById(R.id.nikUser);
        jk = (TextView)  view_fragsiswa.findViewById(R.id.jklUser);
        agama = (TextView)  view_fragsiswa.findViewById(R.id.agmUser);
        kewarga = (TextView)  view_fragsiswa.findViewById(R.id.wargaUser);
        anakke = (TextView)  view_fragsiswa.findViewById(R.id.anakKeUser);
        jmlsdrkandung = (TextView)  view_fragsiswa.findViewById(R.id.jmlsdrkanUser);
        RequestQueue  requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

                            for (int i =0; i<dataArray.length(); i++)
                            {

                                JSONObject obj = dataArray.getJSONObject(i);
                                int extraId = Integer.parseInt(getActivity().getIntent().getStringExtra(TAG_IDU));
                                String nama = obj.getString("nama");
                                int id = obj.getInt("id_siswa");
                                String tempatLahir = obj.getString("tempat_lahir");
                                String tanggalLahir = obj.getString("tanggal_lahir");
                                String kodekelas = obj.getString("kode_kelas");
                                String jurusanS = obj.getString("kode_jurusan");
                                if (extraId== id )
                                {
                                    namaUser.setText(nama);
                                    ttlUser.setText(tempatLahir +","+ tanggalLahir);
                                    kodeKelas.setText(kodekelas);
                                    jurusan.setText(jurusanS);
                                    nisn.setText(obj.getString("nisn"));
                                    nipd.setText(obj.getString("nipd"));
                                    nik .setText(obj.getString("nik"));
                                    jk .setText(obj.getString("id_jenis_kelamin"));
                                    agama.setText(obj.getString("agama"));
                                    kewarga.setText(obj.getString("kewarganegaraan"));
                                    anakke.setText(obj.getString("anak_ke"));
                                    jmlsdrkandung.setText(obj.getString("jml_sdrkandung"));
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
                        namaUser.setText(error.getLocalizedMessage());
                    }
                });
        requestQueue.add(stringRequests);
        return view_fragsiswa;

    }


//    public void getData(View view_fragsiswa)
//    {
//
//
//    }

}
