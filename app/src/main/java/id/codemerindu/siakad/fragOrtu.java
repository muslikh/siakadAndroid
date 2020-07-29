package id.codemerindu.siakad;

import android.content.Context;
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

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class fragOrtu extends Fragment {

    TextView namaAyah,ttlAyah,agamaAyah,KewargaAyah,pendidikanAyah,kerjaAyah,pengeluaranAyah,AlamatAyah,nohpAyah,
            namaIbu,ttlIbu,agamaIbu,KewargaIbu,pendidikanIbu,kerjaIbu,pengeluaranIbu,AlamatIbu,nohpIbu,
            namaWali,ttlWali,agamaWali,KewargaWali,pendidikanWali,kerjaWali,pengeluaranWali,AlamatWali,nohpWali;

    final String url = Server.URL+"siswa/detail?id=";
    public int extraId;
    Boolean session = false;
    SharedPreferences sharedpreferences;
    public final static String TAG_IDU = "idu";
    public final static String TOKEN = "&token=";
    String JWT,id,Token_jwt;
    RequestQueue  requestQueue;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view_fragOrtu = inflater.inflate(R.layout.fragdataortu,container,false);

        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id =  sharedpreferences.getString(TAG_IDU, null);
        Token_jwt = sharedpreferences.getString(JWT, null);

        namaAyah = (TextView) view_fragOrtu.findViewById(R.id.namaAyah);
        ttlAyah = (TextView) view_fragOrtu.findViewById(R.id.ttlAyah);
        agamaAyah = (TextView) view_fragOrtu.findViewById(R.id.agamaAyah);
        KewargaAyah= (TextView) view_fragOrtu.findViewById(R.id.KewargaAyah);
        pendidikanAyah = (TextView) view_fragOrtu.findViewById(R.id.pendidikanAyah);
        kerjaAyah= (TextView) view_fragOrtu.findViewById(R.id.kerjaAyah);
        pengeluaranAyah = (TextView) view_fragOrtu.findViewById(R.id.pengeluaranAyah);
        AlamatAyah = (TextView) view_fragOrtu.findViewById(R.id.AlamatAyah);
        nohpAyah = (TextView) view_fragOrtu.findViewById(R.id.nohpAyah);
        namaIbu = (TextView) view_fragOrtu.findViewById(R.id.namaIbu);
        ttlIbu= (TextView) view_fragOrtu.findViewById(R.id.ttlIbu);
        agamaIbu = (TextView) view_fragOrtu.findViewById(R.id.agamaIbu);
        KewargaIbu = (TextView) view_fragOrtu.findViewById(R.id.KewargaIbu);
        pendidikanIbu= (TextView) view_fragOrtu.findViewById(R.id.pendidikanIbu);
        kerjaIbu= (TextView) view_fragOrtu.findViewById(R.id.kerjaIbu);
        pengeluaranIbu= (TextView) view_fragOrtu.findViewById(R.id.pengeluaranIbu);
        AlamatIbu= (TextView) view_fragOrtu.findViewById(R.id.AlamatIbu);
        nohpIbu= (TextView) view_fragOrtu.findViewById(R.id.nohpIbu);
        namaWali= (TextView) view_fragOrtu.findViewById(R.id.namaWali);
        ttlWali= (TextView) view_fragOrtu.findViewById(R.id.ttlWali);
        agamaWali= (TextView) view_fragOrtu.findViewById(R.id.agamaWali);
        KewargaWali= (TextView) view_fragOrtu.findViewById(R.id.KewargaWali);
        pendidikanWali= (TextView) view_fragOrtu.findViewById(R.id.pendidikanWali);
        kerjaWali= (TextView) view_fragOrtu.findViewById(R.id.kerjaWali);
        pengeluaranWali= (TextView) view_fragOrtu.findViewById(R.id.pengeluaranWali);
        AlamatWali= (TextView) view_fragOrtu.findViewById(R.id.AlamatWali);
        nohpWali= (TextView) view_fragOrtu.findViewById(R.id.nohpWali);

        ambilData();
        return view_fragOrtu;

    }



    public void ambilData()
    {

        extraId = Integer.parseInt(getActivity().getIntent().getStringExtra(TAG_IDU));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url+extraId+TOKEN+Token_jwt, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

                            for (int i =0; i<dataArray.length(); i++)
                            {

                                JSONObject obj = dataArray.getJSONObject(i);
                                // int extraId = Integer.parseInt(getActivity().getIntent().getStringExtra(TAG_IDU));

                                int id = obj.getInt("id");
                                if (extraId== id )
                                {
                                    namaAyah.setText(obj.getString("nama_ayah"));
                                    ttlAyah.setText(obj.getString("tanggal_lahir_ayah"));
                                    agamaAyah.setText(obj.getString("agama_ayah"));
                                    KewargaAyah.setText(obj.getString("kewarga_ayah"));
                                    pendidikanAyah.setText(obj.getString("pendidikan_ayah"));
                                    kerjaAyah.setText(obj.getString("pekerjaan_ayah"));
                                    pengeluaranAyah.setText(obj.getString("pengeluaran_ayah"));
                                    AlamatAyah.setText(obj.getString("alamat_ayah"));
                                    nohpAyah.setText(obj.getString("no_telpon_ayah"));
                                    namaIbu.setText(obj.getString("nama_ibu"));
                                    ttlIbu.setText(obj.getString("tanggal_lahir_ibu"));
                                    agamaIbu.setText(obj.getString("agama_ibu"));
                                    KewargaIbu.setText(obj.getString("kewarga_ibu"));
                                    pendidikanIbu.setText(obj.getString("pendidikan_ibu"));
                                    kerjaIbu.setText(obj.getString("pekerjaan_ibu"));
                                    pengeluaranIbu.setText(obj.getString("pengeluaran_ibu"));
                                    AlamatIbu.setText(obj.getString("alamat_ibu"));
                                    nohpIbu.setText(obj.getString("no_telpon_ibu"));
                                    namaWali.setText(obj.getString("nama_wali"));
                                    ttlWali.setText(obj.getString("tanggal_lahir_wali"));
                                    agamaWali.setText(obj.getString("agama_wali"));
                                    KewargaWali.setText(obj.getString("kewarga_wali"));
                                    pendidikanWali.setText(obj.getString("pendidikan_wali"));
                                    kerjaWali.setText(obj.getString("pekerjaan_wali"));
                                    pengeluaranWali.setText(obj.getString("pengeluaran_wali"));
                                    AlamatWali.setText(obj.getString("alamat_wali"));
                                    nohpWali.setText(obj.getString("no_telpon_wali"));
                                }
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

                    }
                });
        requestQueue.add(stringRequests);
    }
}
