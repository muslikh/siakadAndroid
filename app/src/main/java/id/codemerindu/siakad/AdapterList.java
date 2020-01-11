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

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> implements Filterable {

    Context context;
    public static final String TAG_IDU = "idu";
    ArrayList<HashMap<String ,String >> list_data;
    List<AdapterList> filter;
//https://github.com/larntech/recyclerview-with-search-and-clicklistener/blob/master/app/src/main/java/net/larntech/recyclerview/UsersAdapter.java tutore ndek kene
    public AdapterList(DataSiswa dataSiswa, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = dataSiswa;
        this.list_data = list_data;
        this.filter = list_data;
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
        return filter.size();
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

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if(charSequence == null | charSequence.length() == 0){
                    filterResults.count = getUserModelListFiltered.size();
                    filterResults.values = getUserModelListFiltered;

                }else{
                    String searchChr = charSequence.toString().toLowerCase();

                    List<UserModel> resultData = new ArrayList<>();

                    for(UserModel userModel: getUserModelListFiltered){
                        if(userModel.getUserName().toLowerCase().contains(searchChr)){
                            resultData.add(userModel);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                userModelList = (List<UserModel>) filterResults.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }

}
