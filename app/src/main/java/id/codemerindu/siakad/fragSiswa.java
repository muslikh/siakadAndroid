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

import java.util.zip.Inflater;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class fragSiswa extends Fragment {

    final String url = Server.URL+"siswa/detail?id=";


    TextView namaUser,ttlUser,kodeKelas,jurusan,
            nisn,nipd,nik,jk,agama,kewarga,anakke,jmlsdrkandung,jmlsdrtiri,
            hobi,alamat,rt,rw,dusun,kab,prov,hp,stsTinggal,goldar,penyakit,
            tinggi,berat,lulusdari,noijasah,noskhun,nopeUser,pindahdari,alamatSsebelum,kelasAwal,thnmasuk,nounsmp;
   public int extraId;
    Boolean session = false;
    SharedPreferences sharedpreferences;
    RequestQueue  requestQueue;
    public final static String TAG_IDU = "idu";
    public final static String TOKEN = "&token=";
    String id,JWT,Token_jwt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view_fragsiswa = inflater.inflate(R.layout.fragdatapribadi,container,false);

        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id =  sharedpreferences.getString(TAG_IDU, null);
        Token_jwt = sharedpreferences.getString(JWT, null);


        namaUser = (TextView) view_fragsiswa.findViewById(R.id.namaUser);
        nopeUser = (TextView) view_fragsiswa.findViewById(R.id.nopeUser);
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
        jmlsdrtiri = (TextView)  view_fragsiswa.findViewById(R.id.jmlsdrtirUser);
        hobi = (TextView) view_fragsiswa.findViewById(R.id.hobiUser);
        alamat = (TextView)  view_fragsiswa.findViewById(R.id.alamatUser);
        rt = (TextView)  view_fragsiswa.findViewById(R.id.rtUser);
        rw = (TextView) view_fragsiswa.findViewById(R.id.rwUser);
        dusun = (TextView)  view_fragsiswa.findViewById(R.id.dusunUser);
        kab = (TextView)  view_fragsiswa.findViewById(R.id.kabUser);
        prov = (TextView) view_fragsiswa.findViewById(R.id.provUser);
        stsTinggal = (TextView) view_fragsiswa.findViewById(R.id.stsTinggalUser);
        goldar = (TextView) view_fragsiswa.findViewById(R.id.goldarUser);
        penyakit = (TextView) view_fragsiswa.findViewById(R.id.ketsakitUser);
        tinggi = (TextView) view_fragsiswa.findViewById(R.id.tgUser);
        berat = (TextView) view_fragsiswa.findViewById(R.id.bbUser);
        lulusdari = (TextView) view_fragsiswa.findViewById(R.id.lulusanUser);
        noijasah = (TextView) view_fragsiswa.findViewById(R.id.noijasahUser);
        noskhun= (TextView) view_fragsiswa.findViewById(R.id.noskhunUser);
        pindahdari = (TextView) view_fragsiswa.findViewById(R.id.pindahanUser);
        alamatSsebelum = (TextView) view_fragsiswa.findViewById(R.id.alamatsebelum);
        kelasAwal = (TextView) view_fragsiswa.findViewById(R.id.kelasawalUser);
        thnmasuk= (TextView) view_fragsiswa.findViewById(R.id.thnmskUser);
        nounsmp= (TextView) view_fragsiswa.findViewById(R.id.nounUser);

        ambilData();
        return view_fragsiswa;

    }


    public void ambilData()
    {

        extraId = Integer.parseInt(getActivity().getIntent().getStringExtra(TAG_IDU));


        RequestQueue  requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
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
                                String tempatLahir = obj.getString("tempat_lahir");
                                String tanggalLahir = obj.getString("tanggal_lahir");
                                if (extraId== id )
                                {
                                    namaUser.setText(obj.getString("nama"));
                                    nopeUser.setText(obj.getString("hp"));
                                    ttlUser.setText(tempatLahir +","+ tanggalLahir);
                                    kelasAwal.setText(obj.getString("kelas_awal"));
                                    kodeKelas.setText(obj.getString("kode_kelas"));
                                    thnmasuk.setText(obj.getString("tahun_masuk"));
                                    jurusan.setText(obj.getString("kode_jurusan"));
                                    nisn.setText(obj.getString("nisn"));
                                    nipd.setText(obj.getString("nipd"));
                                    nik .setText(obj.getString("nik"));
                                    nounsmp.setText(obj.getString("no_unsmp"));
                                    jk .setText(obj.getString("id_jenis_kelamin"));
                                    agama.setText(obj.getString("id_agama"));
                                    kewarga.setText(obj.getString("kewarganegaraan"));
                                    anakke.setText(obj.getString("anak_ke"));
                                    jmlsdrkandung.setText(obj.getString("jml_sdrkandung"));
                                    jmlsdrtiri.setText(obj.getString("jml_sdrtiri"));
                                    hobi.setText(obj.getString("hobi"));
                                    alamat.setText(obj.getString("alamat"));
                                    rt.setText(obj.getString("rt"));
                                    rw.setText(obj.getString("rw"));
                                    dusun.setText(obj.getString("dusun"));
                                    kab.setText(obj.getString("kabupaten"));
                                    prov.setText(obj.getString("provinsi"));
                                    stsTinggal.setText(obj.getString("jenis_tinggal"));
                                    goldar.setText(obj.getString("goldarah"));
                                    penyakit.setText(obj.getString("sakitygpernah"));
                                    tinggi.setText(obj.getString("tinggi_badan"));
                                    berat.setText(obj.getString("berat_badan"));
                                    lulusdari.setText(obj.getString("darisekolah"));
                                    noijasah.setText(obj.getString("no_ijasah"));
                                    noskhun.setText(obj.getString("no_skhu"));
                                    pindahdari.setText(obj.getString("pindahdari"));
                                    alamatSsebelum.setText(obj.getString("alamatsekolah"));

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
                        namaUser.setText(error.getLocalizedMessage());
                    }
                });
        requestQueue.add(stringRequests);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
