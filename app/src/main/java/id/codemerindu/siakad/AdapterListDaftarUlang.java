package id.codemerindu.siakad;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdapterListDaftarUlang extends RecyclerView.Adapter<AdapterListDaftarUlang.ViewHolder>{

    Context context;
    public static final String TAG_IDU = "idu";
    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;
    public  String url_cek = Server.URL+"siswa.php?aksi=cek_kode";
    private static final String TAG_SUCCESS = "success";
    int success;
    ProgressDialog pDialog;

    public AdapterListDaftarUlang(daftarUlang daftarUlang, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = daftarUlang;
        this.list_data = list_data;
        this.filterL = list_data;
    }
    @Override
    public AdapterListDaftarUlang.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sppdb, null);
        return new AdapterListDaftarUlang.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterListDaftarUlang.ViewHolder holder, final int position) {

        final String  idu = list_data.get(position).get("id_siswa");
        holder.noun.setText("NO UN : "+list_data.get(position).get("no_unsmp"));
        holder.nisn.setText("NISN : "+list_data.get(position).get("nisn"));
        holder.namas.setText("Nama : "+list_data.get(position).get("nama"));
        holder.kodejurusan.setText("Prodi : "+list_data.get(position).get("kode_jurusan"));
        holder.kodekelas.setText("Kelas : "+list_data.get(position).get("kode_kelas"));
        holder.btn_hapus.setVisibility(View.INVISIBLE);
        holder.btn_validasi.setVisibility(View.INVISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.form_kode,null);
                dialog.setView(view);
                dialog.setCancelable(false);
//                dialog.setIcon(R.mipmap.ic_smk);
                dialog.setTitle("Masukkan Kode");

//                final TextView tv_pesan = (TextView) view.findViewById(R.id.pesan);

                final  EditText tv_kode = (EditText) view.findViewById(R.id.TulisKode);
//               TextView mintakode = (TextView) view.findViewById(R.id.reqKode);

                dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String kode = tv_kode.getText().toString();

                                // mengecek kolom yang kosong
                                if (kode.trim().length() == 0) {

                                   pesan("Kolom tidak boleh kosong");
                                } else
                                {
                                    cekKode(kode,idu );
                                }

                            }
                        });
                dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

//                final String  idu = list_data.get(position).get("id_siswa");
//                final CharSequence[] options = { "Daftar Ulang","Batal" };
//                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
//                builder.setTitle("Menu !");
//                builder.setItems(options, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//                        if (options[item].equals("Daftar Ulang"))
//                        {
//                            Intent pindah=new Intent(context, EditDataSiswa.class);
//                            pindah.putExtra(TAG_IDU,idu);
//                            context.startActivity(pindah);
//                        }
//                        else if (options[item].equals("Batal")) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                builder.show();


                        AlertDialog cekKode = dialog.create();
                cekKode.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_data.size();
//        return filter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namas,kodekelas,kodejurusan,nisn,noun,btn_validasi,btn_hapus;;
        ImageView imgsbaru;

        public ViewHolder(View itemView) {
            super(itemView);

            btn_hapus = (TextView) itemView.findViewById(R.id.hapusSbaru);
            btn_validasi = (TextView) itemView.findViewById(R.id.validSbaru);
            namas = (TextView) itemView.findViewById(R.id.namass);
            kodekelas = (TextView) itemView.findViewById(R.id.kodekelass);
            kodejurusan = (TextView) itemView.findViewById(R.id.kodejurusans);
            nisn = (TextView) itemView.findViewById(R.id.nisns);
            noun = (TextView) itemView.findViewById(R.id.noun);
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

    public void cekKode (final String kode, final String idu)
    {
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mohon Tunggu...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_cek, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt("success");

//                    String kode = jObj.getString("kode");

                    // Check for error node in json
                    if (success == 1){
                        Intent intent = new Intent(context, EditDataSiswa.class);
                        intent.putExtra(TAG_IDU, idu);
                        // finish();
                        context.startActivity(intent);
                    }  else {

                        pesan("Kode Salah \n  Silahkan Periksa Email Yang Tredaftar,Kalau Tidak Ada Silahkan Hubungi Admin \n WA: 085369000323 \n Terima Kasih ");
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("kode", kode);
                params.put("id_siswa", idu);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void pesan(final String isiPesan)
    {  AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert
                .setMessage(isiPesan)
                .setCancelable(false)
                .setNegativeButton("Okke", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog kodesalah = alert.create();
        kodesalah.show();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}