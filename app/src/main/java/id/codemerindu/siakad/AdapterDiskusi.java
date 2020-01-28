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
import java.util.Locale;
import java.util.Map;

public class AdapterDiskusi extends RecyclerView.Adapter<AdapterDiskusi.ViewHolder>{

    Context context;

    public final String deluser = Server.URL+"diskusi.php";
    ArrayList<HashMap<String ,String >> txt_message;
    ArrayList<HashMap<String ,String >>  filterL;
    //https://github.com/larntech/recyclerview-with-search-and-clicklistener/blob/master/app/src/main/java/net/larntech/recyclerview/UsersAdapter.java tutore ndek kene
    public AdapterDiskusi(Diskusi diskusi, ArrayList<HashMap<String ,String >>txt_message)
    {
        this.context = diskusi;
        this.txt_message = txt_message;
    }
    @Override
    public AdapterDiskusi.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.txt_message, null);
        return new AdapterDiskusi.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterDiskusi.ViewHolder holder, final int position) {

//        Glide.with(context)
//                .load("http://192.168.95.77/app_blogvolley/img/" + list_data.get(position).get("gambar"))
//                //.crossFade()
//                .placeholder(R.mipmap.ic_launcher)
//                .into(holder.imgsbaru);

        holder.isipesan.setText(txt_message.get(position).get("pesan"));
        holder.namapengirim.setText(txt_message.get(position).get("nama"));
        holder.waktuKirim.setText(txt_message.get(position).get("waktuKirim"));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String  idu = txt_message.get(position).get("id_siswa");
//                final CharSequence[] options = { "Detail Data", "Hapus","Batal" };
//                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
//                builder.setTitle("Menu !");
//                builder.setItems(options, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//                        if (options[item].equals("Detail Data"))
//                        {
//
//                        }
//                        else if (options[item].equals("Hapus"))
//                        {
//
//
//
//                        }
//
//                        else if (options[item].equals("Batal")) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                builder.show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return txt_message.size();
//        return filter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView isipesan,namapengirim,waktuKirim;
        ImageView imgsbaru;

        public ViewHolder(View itemView) {
            super(itemView);

            isipesan = (TextView) itemView.findViewById(R.id.message_text);
            namapengirim = (TextView) itemView.findViewById(R.id.name_text);
            waktuKirim = (TextView) itemView.findViewById(R.id.time_text);
        }
    }


}