package id.codemerindu.siakad;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.HashMap;

public class fragJadwalUjian extends Fragment {
    TextView no,hari,waktu,namaGuru,namaMapel;

    final String url = Server.URL+"jadwalUjian?kode_kelas=";
    public String  kelas;
    Boolean session = false;
    SharedPreferences sharedpreferences;
    RequestQueue requestQueue;
    public final static String TAG_KELAS = "kode_kelas";

    ArrayList<HashMap<String ,String>> list_data;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    AdapterUjian adapterUjian;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_jadwal_ujian, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.jadwalUjian);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        list_data = new ArrayList<HashMap<String, String>>();




        no = view.findViewById(R.id.noUjian);
        hari = view.findViewById(R.id.hariUjian);

        waktu = view.findViewById(R.id.waktuUjian);
        namaGuru = view.findViewById(R.id.guruUjian);
        namaMapel = view.findViewById(R.id.mapelUjian);

        getUjian();
        return view;
    }


    public void getUjian()
    {

        kelas = getActivity().getIntent().getStringExtra(TAG_KELAS);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url+kelas, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

                            for (int i =0; i<dataArray.length(); i++)
                            {


                                JSONObject json = dataArray.getJSONObject(i);
                                HashMap<String, String > map = new HashMap<String , String >();
                                int key = i+1;
                                map.put("no", Integer.toString(key));
                                map.put("hari",json.getString("hari"));
                                map.put("waktu",json.getString("waktu"));
                                map.put("nama_guru",json.getString("nama_guru"));
                                map.put("id_mapel",json.getString("id_mapel"));
                                list_data.add(map);
                                adapterUjian = new AdapterUjian(getActivity(), list_data);
                                mRecyclerView.setAdapter(adapterUjian);
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
