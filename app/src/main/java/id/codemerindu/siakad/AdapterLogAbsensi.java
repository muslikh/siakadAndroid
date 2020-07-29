package id.codemerindu.siakad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterLogAbsensi extends RecyclerView.Adapter<AdapterLogAbsensi.ViewHolder> {

    Context context;


    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;

    public AdapterLogAbsensi(Context context, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = context;
        this.list_data=list_data;

    }

    @Override
    public AdapterLogAbsensi.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_logabsen, null);
        return new AdapterLogAbsensi.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterLogAbsensi.ViewHolder holder, final int position) {


        holder.no.setText(list_data.get(position).get("no"));
        holder.tgl.setText(list_data.get(position).get("tgl"));
        holder.jam.setText(list_data.get(position).get("jam"));
        holder.status.setText(list_data.get(position).get("status"));
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView no,tgl,jam,status;

        public ViewHolder(View itemView) {
            super(itemView);


            no = itemView.findViewById(R.id.isiNo);
            tgl = itemView.findViewById(R.id.isiTgl);
            jam = itemView.findViewById(R.id.isiJam);
            status = itemView.findViewById(R.id.isiStatus);

        }
    }
}
