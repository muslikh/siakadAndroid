package id.codemerindu.siakad;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> {

    Context context;
    public static final String TAG_IDU = "idu";
    ArrayList<HashMap<String ,String >> list_data;

    public AdapterList(DataSiswa dataSiswa, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = dataSiswa;
        this.list_data = list_data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_siswa, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        Glide.with(context)
//                .load("http://192.168.95.77/app_blogvolley/img/" + list_data.get(position).get("gambar"))
//                //.crossFade()
//                .placeholder(R.mipmap.ic_launcher)
//                .into(holder.imgsbaru);
        Picasso.with(context).load("http://smknprigen.sch.id/bkk/image/default.png").into(holder.imgsbaru);
        holder.namas.setText("Nama : "+list_data.get(position).get("nama"));
        holder.kodejurusan.setText("Prodi : "+list_data.get(position).get("kode_jurusan"));
        holder.kodekelas.setText("Kelas : "+list_data.get(position).get("kode_kelas"));
       // holder.nisn.setText(list_data.get(position).get("nisn"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idu = Integer.parseInt(list_data.get(position).get("id_siswa"));
                Intent pindah=new Intent(context,Profile.class);
                pindah.putExtra(TAG_IDU,idu);
                context.startActivity(pindah);
                Toast.makeText(context, "Recycle Click" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namas,kodekelas,kodejurusan;
        ImageView imgsbaru;

        public ViewHolder(View itemView) {
            super(itemView);

            namas = (TextView) itemView.findViewById(R.id.namas);
            kodekelas = (TextView) itemView.findViewById(R.id.kodekelas);
            kodejurusan = (TextView) itemView.findViewById(R.id.kodejurusan);
            imgsbaru = (ImageView) itemView.findViewById(R.id.imgsbaru);
        }
    }

//    void setFilter(ArrayList<HashMap<String ,String >>filter)
//    {
//        list_data = new ArrayList<>();
//        list_data.addAll(filter);
//        notifyDataSetChanged();
//    }

}
