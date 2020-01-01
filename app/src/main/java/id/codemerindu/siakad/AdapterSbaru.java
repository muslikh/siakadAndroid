package id.codemerindu.siakad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterSbaru extends RecyclerView.Adapter<AdapterSbaru.ViewHolder>{

    Context context;
    ArrayList<HashMap<String ,String >> list_data;

    public AdapterSbaru(validasiPPDB validasiPPDB, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = validasiPPDB;
        this.list_data = list_data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sppdb, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Glide.with(context)
//                .load("http://192.168.95.77/app_blogvolley/img/" + list_data.get(position).get("gambar"))
//                //.crossFade()
//                .placeholder(R.mipmap.ic_launcher)
//                .into(holder.imgsbaru);
        holder.namas.setText("Nama : "+list_data.get(position).get("nama"));
        holder.kodejurusan.setText("Prodi : "+list_data.get(position).get("kode_jurusan"));
        holder.kodekelas.setText("Kelas : "+list_data.get(position).get("kode_kelas"));
        holder.nisn.setText("NSN : "+list_data.get(position).get("nisn"));
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namas,kodekelas,kodejurusan,nisn;
        ImageView imgsbaru;

        public ViewHolder(View itemView) {
            super(itemView);

            namas = (TextView) itemView.findViewById(R.id.namas);
            kodekelas = (TextView) itemView.findViewById(R.id.kodekelass);
            kodejurusan = (TextView) itemView.findViewById(R.id.kodejurusans);
            nisn = (TextView) itemView.findViewById(R.id.nisns);
        }
    }

}
