package id.codemerindu.siakad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterNilai  extends RecyclerView.Adapter<AdapterNilai.ViewHolder> {

    Context context;

    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;

    public AdapterNilai(KumpulanNilai KumpulanNilai, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = KumpulanNilai;
        this.list_data=list_data;

    }

    @Override
    public AdapterNilai.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_nilai, null);
        return new AdapterNilai.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterNilai.ViewHolder holder, final int position) {

        holder.tv_noNilai.setText(list_data.get(position).get("noNilai"));
        holder.tv_mapelNilai.setText(list_data.get(position).get("nama_mapel"));
        holder.tv_kkmNilai.setText(list_data.get(position).get("kkm"));
        holder.tv_nilaiAngkaPengetahuan.setText(list_data.get(position).get("nilaiAngkaPengetahuan"));
        holder.tv_nilaiHurufPengetahuan.setText(list_data.get(position).get("nilaiHurufPengetahuan"));
        holder.tv_nilaiAngkaKeterampilan.setText(list_data.get(position).get("nilaiAngkaKeterampilan"));
        holder.tv_nilaiHurufKeterampilan.setText(list_data.get(position).get("nilaiHurufKeterampilan"));
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_noNilai,tv_mapelNilai,tv_kkmNilai,tv_nilaiAngkaPengetahuan,tv_nilaiHurufPengetahuan,tv_nilaiAngkaKeterampilan,tv_nilaiHurufKeterampilan;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_noNilai = (TextView) itemView.findViewById(R.id.noNilai);
            tv_mapelNilai = (TextView) itemView.findViewById(R.id.mapelNilai);
            tv_kkmNilai = (TextView) itemView.findViewById(R.id.kkmNilai);
            tv_nilaiAngkaPengetahuan = (TextView) itemView.findViewById(R.id.nilaiAngkaPengetahuan);
            tv_nilaiHurufPengetahuan = (TextView) itemView.findViewById(R.id.nilaiHurufPengetahuan);
            tv_nilaiAngkaKeterampilan = (TextView) itemView.findViewById(R.id.nilaiAngkaKeterampilan);
            tv_nilaiHurufKeterampilan = (TextView) itemView.findViewById(R.id.nilaiHurufKeterampilan);

        }
    }
}
