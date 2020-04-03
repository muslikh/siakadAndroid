package id.codemerindu.siakad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdapterSbaru extends RecyclerView.Adapter<AdapterSbaru.ViewHolder>{

    Context context;
    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;

    String url_validasi = Server.URL+"siswa.php?aksi=validasi";
    String url_hapus= Server.URL+"siswa.php?aksi=hapusSbaru";

    public AdapterSbaru(validasiPPDB validasiPPDB, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = validasiPPDB;
        this.list_data = list_data;
        this.filterL = list_data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sppdb, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String idu = list_data.get(position).get("id_siswa");
        holder.namas.setText("Nama : "+list_data.get(position).get("nama"));
        holder.kodejurusan.setText("Prodi : "+list_data.get(position).get("kode_jurusan"));
        holder.kodekelas.setText("Kelas : "+list_data.get(position).get("kode_kelas"));
        holder.nisn.setText("NSN : "+list_data.get(position).get("nisn"));
        holder.btn_validasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_validasi, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject dataObj = new JSONObject(response);


                            Toast.makeText(context, dataObj.getString("message"), Toast.LENGTH_LONG).show();
                            // adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", "Error: " + error.getMessage());
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

                    @Override

                    protected Map<String,String> getParams() throws AuthFailureError {

                        Map<String,String> map = new HashMap<String, String>();
                        map.put("id_siswa", idu);
                        return map;
                    }

                };
                AppController.getInstance().addToRequestQueue(stringRequest);
            }
        });
        holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_hapus, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject dataObj = new JSONObject(response);


                            Toast.makeText(context, dataObj.getString("message"), Toast.LENGTH_LONG).show();
                            // adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", "Error: " + error.getMessage());
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

                    @Override

                    protected Map<String,String> getParams() throws AuthFailureError {

                        Map<String,String> map = new HashMap<String, String>();
                        map.put("id_siswa", idu);
                        return map;
                    }

                };
                Intent refresh = new Intent(context,validasiPPDB.class);
                context.startActivity(refresh);
                AppController.getInstance().addToRequestQueue(stringRequest);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namas,kodekelas,kodejurusan,nisn  ,btn_validasi,btn_hapus;
        ImageView imgsbaru;
        public ViewHolder(View itemView) {
            super(itemView);

            btn_hapus = (TextView) itemView.findViewById(R.id.hapusSbaru);
            btn_validasi = (TextView) itemView.findViewById(R.id.validSbaru);

            namas = (TextView) itemView.findViewById(R.id.namass);
            kodekelas = (TextView) itemView.findViewById(R.id.kodekelass);
            kodejurusan = (TextView) itemView.findViewById(R.id.kodejurusans);
            nisn = (TextView) itemView.findViewById(R.id.nisns);
        }
    }
    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        list_data = new ArrayList<HashMap<String, String>>();
        if (charText.length() == 0)
        {
            list_data.addAll(filterL);
        }else {
            for (HashMap<String, String> item : filterL)
            {
                if(item.toString().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    list_data.add(item);
                }
            }
        }
    }

}
