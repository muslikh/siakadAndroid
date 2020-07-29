package id.codemerindu.siakad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterUjian extends RecyclerView.Adapter<AdapterUjian.ViewHolder> {

    Context context;


    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;

    public AdapterUjian(Context context, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = context;
        this.list_data=list_data;

    }

    @Override
    public AdapterUjian.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ujian, null);
        return new AdapterUjian.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterUjian.ViewHolder holder, final int position) {

        holder.no.setText(list_data.get(position).get("no"));
        holder.hari.setText(list_data.get(position).get("hari"));
        holder.waktu.setText(list_data.get(position).get("waktu"));
        holder.namaGuru.setText(list_data.get(position).get("nama_guru"));
        holder.namaMapel.setText(list_data.get(position).get("id_mapel"));
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView no,namaGuru,waktu,hari,namaMapel;

        public ViewHolder(View itemView) {
            super(itemView);


            no = itemView.findViewById(R.id.noUjian);
            hari = itemView.findViewById(R.id.hariUjian);
            waktu = itemView.findViewById(R.id.waktuUjian);
            namaGuru = itemView.findViewById(R.id.guruUjian);
            namaMapel = itemView.findViewById(R.id.mapelUjian);

        }
    }
}
