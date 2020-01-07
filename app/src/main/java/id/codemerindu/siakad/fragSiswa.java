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

    TextView namaUser,ttlUser,kodeKelas,jurusan,
            nisn,nipd,nik,jk,agama,kewarga,anakke,jmlsdrkandung,jmlsdrtiri,
            hobi,alamat,rt,rw,dusun,kab,prov,hp,stsTinggal,goldar,penyakit,
            tinggi,berat,lulusdari,noijasah,noskhun,pindahdari,alamatSsebelum,kelasAwal,thnmasuk;
                    ;
    String id;
    SharedPreferences sharedpreferences;
    public final static String TAG="Profile";
    public final static String TAG_IDU = "idu";
    RequestQueue  requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view_fragsiswa = inflater.inflate(R.layout.fragdatapribadi,container,false);
        ambilData();
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
        thnmasuk= (TextView) view_fragsiswa.findViewById(R.id.tahunmasuk);


        return view_fragsiswa;
    }


    public void ambilData()
    {


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

                                    jmlsdrtiri.setText(obj.getString("jml_sdrkandung"));
                                    hobi.setText(obj.getString("jml_sdrkandung"));
                                    alamat.setText(obj.getString("jml_sdrkandung"));
                                    rt.setText(obj.getString("jml_sdrkandung"));
                                    rw.setText(obj.getString("jml_sdrkandung"));
                                    dusun.setText(obj.getString("jml_sdrkandung"));
                                    kab.setText(obj.getString("jml_sdrkandung"));
                                    prov.setText(obj.getString("jml_sdrkandung"));
                                    stsTinggal.setText(obj.getString("jml_sdrkandung"));
                                    goldar.setText(obj.getString("jml_sdrkandung"));
                                    penyakit.setText(obj.getString("jml_sdrkandung"));
                                    tinggi.setText(obj.getString("jml_sdrkandung"));
                                    berat.setText(obj.getString("jml_sdrkandung"));
                                    lulusdari.setText(obj.getString("jml_sdrkandung"));
                                    noijasah.setText(obj.getString("jml_sdrkandung"));
                                    noskhun.setText(obj.getString("jml_sdrkandung"));
                                    pindahdari.setText(obj.getString("jml_sdrkandung"));
                                    alamatSsebelum.setText(obj.getString("jml_sdrkandung"));
                                    kelasAwal.setText(obj.getString("jml_sdrkandung"));
                                    thnmasuk.setText(obj.getString("jml_sdrkandung"));

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
    }

}
