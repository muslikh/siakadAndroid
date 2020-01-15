package id.codemerindu.siakad;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder>{

    Context context;
    public static final String TAG_IDU = "idu";
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
        holder.thnmsk.setText("Tahun Masuk : "+list_data.get(position).get("tahun_masuk"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  idu = list_data.get(position).get("id_siswa");
                Intent pindah=new Intent(context,Profile.class);
                pindah.putExtra(TAG_IDU,idu);
                context.startActivity(pindah);
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
//
//    @Override
//    protected FilterResults performFiltering(CharSequence charSequence)
//    {
//        filter.clear();
//        final FilterResults results = new FilterResults();
//        if (charSequence.length() == 0)
//        {
//            filter.addAll(list_data);
//        }else {
//            final String filterPattern = charSequence.toString().toLowerCase().trim();
//            for (HashMap<String ,String > item : list_data)
//            {
//                if(item.toString().toLowerCase().contains(filterPattern))
//                {
//                    filter.add(item);
//                }
//            }
//        }
//        results.values = filter;
//        results.count = filter.size();
//        return results;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    protected void publishResults(CharSequence charSequence, FilterResults filterResults)
//    {
//            list_data.clear();
//            list_data.add(filterResults.values);
//            notifyDataSetChanged();
//    }

//    @Override
//    public Filter getFilter() {
//        Filter filter = new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                FilterResults filterResults = new FilterResults();
//
//                if(charSequence == null | charSequence.length() == 0){
//                    filterResults.count = getUserModelListFiltered.size();
//                    filterResults.values = getUserModelListFiltered;
//
//                }else{
//                    String searchChr = charSequence.toString().toLowerCase();
//
//                    List<UserModel> resultData = new ArrayList<>();
//
//                    for(UserModel userModel: getUserModelListFiltered){
//                        if(userModel.getUserName().toLowerCase().contains(searchChr)){
//                            resultData.add(userModel);
//                        }
//                    }
//                    filterResults.count = resultData.size();
//                    filterResults.values = resultData;
//
//                }
//
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//
//                userModelList = (List<UserModel>) filterResults.values;
//                notifyDataSetChanged();
//
//            }
//        };
//        return filter;
//    }



}
