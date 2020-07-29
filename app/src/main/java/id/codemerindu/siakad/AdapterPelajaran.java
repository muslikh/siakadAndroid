package id.codemerindu.siakad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterPelajaran extends RecyclerView.Adapter<AdapterPelajaran.ViewHolder> {

    Context context;


    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;

    public AdapterPelajaran(Context context, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = context;
        this.list_data=list_data;

    }

    @Override
    public AdapterPelajaran.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pelajaran, null);
        return new AdapterPelajaran.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterPelajaran.ViewHolder holder, final int position) {

        holder.no.setText(list_data.get(position).get("no"));
        holder.hari.setText(list_data.get(position).get("hari"));
        holder.jamke.setText(list_data.get(position).get("jam_ke"));
        holder.namaGuru.setText(list_data.get(position).get("nama_guru"));
        holder.namaMapel.setText(list_data.get(position).get("id_mapel"));
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView no,namaGuru,jamke,hari,namaMapel;

        public ViewHolder(View itemView) {
            super(itemView);


            no = itemView.findViewById(R.id.noPelajaran);
            hari = itemView.findViewById(R.id.hariPelajaran);
            jamke = itemView.findViewById(R.id.jamke);
            namaGuru = itemView.findViewById(R.id.guruPelajaran);
            namaMapel = itemView.findViewById(R.id.mapelPelajaran);

        }
    }
}
