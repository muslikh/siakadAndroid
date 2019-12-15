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

    final String url = "http://152746201341.ip-dynamic.com/login/siswa.php";

    TextView namaUser,ttlUser,kodeKelas,jurusan;
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
