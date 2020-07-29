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

public class AdapterPengumuman extends RecyclerView.Adapter<AdapterPengumuman.ViewHolder> {

    Context context;

    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;

    public AdapterPengumuman(MainActivity Pengumuman, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = Pengumuman;
        this.list_data=list_data;

    }

    @Override
    public AdapterPengumuman.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row, null);
        return new AdapterPengumuman.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterPengumuman.ViewHolder holder, final int position) {

        holder.tv_isi.setText(list_data.get(position).get("isi_pengumuman"));
        holder.tv_judul.setText(list_data.get(position).get("judul_pengumuman"));
        holder.tv_tgl.setText(list_data.get(position).get("tgl_pengumuman"));
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_judul,tv_tgl,tv_isi;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_isi = (TextView) itemView.findViewById(R.id.isiPengumuman);
            tv_judul = (TextView) itemView.findViewById(R.id.judulpengumuman);
            tv_tgl = (TextView) itemView.findViewById(R.id.tanggalpengumuman);

        }
    }
}
