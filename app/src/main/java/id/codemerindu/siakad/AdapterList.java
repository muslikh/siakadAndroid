package id.codemerindu.siakad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder>{

    Context context;
    public static final String TAG_IDU = "idu";
    public final String deluser = Server.URL+"delperid.php";
    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;
//https://github.com/larntech/recyclerview-with-search-and-clicklistener/blob/master/app/src/main/java/net/larntech/recyclerview/UsersAdapter.java tutore ndek kene
    public AdapterList(DataSiswa dataSiswa, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = dataSiswa;
        this.list_data = list_data;
        this.filterL = list_data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_siswa, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//        Glide.with(context)
//                .load("http://192.168.95.77/app_blogvolley/img/" + list_data.get(position).get("gambar"))
//                //.crossFade()
//                .placeholder(R.mipmap.ic_launcher)
//                .into(holder.imgsbaru);
        String fotobase64 = list_data.get(position).get("foto");
        byte[] decodedString = Base64.decode(fotobase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (fotobase64.isEmpty()) {

            Picasso.with(context).load("http://smknprigen.sch.id/bkk/image/default.png").into(holder.imgsbaru);
        } else if (fotobase64.equals("null")) {

            Picasso.with(context).load("http://smknprigen.sch.id/bkk/image/default.png").into(holder.imgsbaru);
        } else {

            holder.imgsbaru.setImageBitmap(decodedByte);
        }
        holder.namas.setText("Nama : "+list_data.get(position).get("nama"));
        holder.kodejurusan.setText("Prodi : "+list_data.get(position).get("kode_jurusan"));
        holder.kodekelas.setText("Kelas : "+list_data.get(position).get("kode_kelas"));
        holder.thnmsk.setText("Tahun Masuk : "+list_data.get(position).get("tahun_masuk"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String  idu = list_data.get(position).get("id_siswa");
                final CharSequence[] options = { "Detail Data", "Hapus","Batal" };
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                builder.setTitle("Menu !");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Detail Data"))
                        {
                            Intent pindah=new Intent(context,Profile.class);
                            pindah.putExtra(TAG_IDU,idu);
                            context.startActivity(pindah);
                        }
                        else if (options[item].equals("Hapus"))
                        {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, deluser, new Response.Listener<String>() {
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
                            Intent refresh=new Intent(context,DataSiswa.class);
                            context.startActivity(refresh);
                            AppController.getInstance().addToRequestQueue(stringRequest);
                        }

                        else if (options[item].equals("Batal")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_data.size();
//        return filter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namas,kodekelas,kodejurusan,thnmsk;
        ImageView imgsbaru;

        public ViewHolder(View itemView) {
            super(itemView);

            namas = (TextView) itemView.findViewById(R.id.namas);
            kodekelas = (TextView) itemView.findViewById(R.id.kodekelas);
            kodejurusan = (TextView) itemView.findViewById(R.id.kodejurusan);
            imgsbaru = (ImageView) itemView.findViewById(R.id.imgsbaru);
            thnmsk = (TextView) itemView.findViewById(R.id.tahunmasuk);
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
