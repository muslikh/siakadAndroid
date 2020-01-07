package id.codemerindu.siakad;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditDataSiswa extends AppCompatActivity {


    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;
    String url = Server.URL +"siswa.php";
    String url_update  = Server.URL+"update.php";
    final String TAG ="Edit";
    public final static String TAG_IDU = "idu";
    public final static String TAG_MESSAGE = "message";
    EditText EdnamaUser,Edtmplahir,EdtgllahirUser;
    TextView idUser,nipd,nisnUser,TvkodeKelas,Tvjurusan,Tvtgllahir,kelasAwal,thnmasuk;
    EditText namaUser,ttlUser,kodeKelas,jurusan,
    nisn,nik,kewarga,anakke,jmlsdrkandung,jmlsdrtiri,
    hobi,alamat,rt,rw,dusun,kab,prov,hp,stsTinggal,goldar,penyakit,
    tinggi,berat,lulusdari,noijasah,noskhun,nopeUser,pindahdari,alamatSsebelum;

    Spinner agama,jk;
    Button updateSiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_siswa);
        editData();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbareddaata);
        setSupportActionBar(toolbar);

        dateFormat = new SimpleDateFormat("dd-MM-yyy", Locale.US);
        getSupportActionBar().setTitle("Ubah Data");

        updateSiswa = (Button)  findViewById(R.id.updateSiswa);
        updateSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });

        EdtgllahirUser = (EditText) findViewById(R.id.tanggallahirUser);
        EdtgllahirUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        idUser = (TextView)  findViewById(R.id.idSiswa);
        nisnUser = (TextView)  findViewById(R.id.nisnUser);
        EdnamaUser = (EditText) findViewById(R.id.namaUser);
        Edtmplahir = (EditText) findViewById(R.id.tmplahir);
        Tvtgllahir = (TextView) findViewById(R.id.tanggallahirUser);
        TvkodeKelas = (TextView) findViewById(R.id.kodeKelasUser);
        Tvjurusan = (TextView)  findViewById(R.id.jurusanUser);


        nopeUser = ( EditText)  findViewById(R.id.nopeUser);
        nipd = ( TextView)   findViewById(R.id.nipdUser);
        nik = ( EditText)   findViewById(R.id.nikUser);
        jk = (Spinner)   findViewById(R.id.jklUser);
        agama = ( Spinner)   findViewById(R.id.agmUser);
        kewarga = ( EditText)   findViewById(R.id.wargaUser);
        anakke = ( EditText)   findViewById(R.id.anakKeUser);
        jmlsdrkandung = ( EditText)   findViewById(R.id.jmlsdrkanUser);
        jmlsdrtiri = ( EditText)   findViewById(R.id.jmlsdrtirUser);
        hobi = ( EditText)  findViewById(R.id.hobiUser);
        alamat = ( EditText)   findViewById(R.id.alamatUser);
        rt = ( EditText)   findViewById(R.id.rtUser);
        rw = ( EditText)  findViewById(R.id.rwUser);
        dusun = ( EditText)   findViewById(R.id.dusunUser);
        kab = ( EditText)   findViewById(R.id.kabUser);
        prov = ( EditText)  findViewById(R.id.provUser);
        stsTinggal = ( EditText)  findViewById(R.id.stsTinggalUser);
        goldar = ( EditText)  findViewById(R.id.goldarUser);
        penyakit = ( EditText)  findViewById(R.id.ketsakitUser);
        tinggi = ( EditText)  findViewById(R.id.tgUser);
        berat = ( EditText)  findViewById(R.id.bbUser);
        lulusdari = ( EditText)  findViewById(R.id.lulusanUser);
        noijasah = ( EditText)  findViewById(R.id.noijasahUser);
        noskhun= ( EditText)  findViewById(R.id.noskhunUser);
        pindahdari = ( EditText)  findViewById(R.id.pindahanUser);
        alamatSsebelum = ( EditText)  findViewById(R.id.alamatsebelum);
        kelasAwal = ( TextView)  findViewById(R.id.kelasawalUser);
        thnmasuk= ( TextView)  findViewById(R.id.thnmskUser);

    }

    public void editData()
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

                            for (int i =0; i<dataArray.length(); i++)
                            {


                                JSONObject obj = dataArray.getJSONObject(i);
                                int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDU));
                                String nama = obj.getString("nama");
                                int id = obj.getInt("id_siswa");
                                String id_siswa = obj.getString("id_siswa");
                                String nisn = obj.getString("nisn");
                                String tempatLahir = obj.getString("tempat_lahir");
                                String kodekelas = obj.getString("kode_kelas");
                                String jurusanS = obj.getString("kode_jurusan");
                                if (extraId== id )
                                {
                                    idUser.setText(id_siswa);
                                    nisnUser.setText(nisn);
                                    EdnamaUser.setText(nama);
                                    Edtmplahir.setText(tempatLahir);
                                    EdtgllahirUser.setText(obj.getString("tanggal_lahir"));
                                    TvkodeKelas.setText(kodekelas);
                                    Tvjurusan.setText(jurusanS);

                                    String code = obj.getString("code");
                                    if (code.equals("sukses"))
                                    {
                                        ubahBerhasil();
                                    }else if (code.equals("gagal"))
                                    {
                                        ubahGagal();
                                    }
                                }
                            }
                            Log.d(TAG, "onResponse:" + response);
                        }  catch(
                                JSONException e)

                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        EdnamaUser.setText(error.getLocalizedMessage());
                    }
                });
        requestQueue.add(stringRequests);
    }

    private void simpan()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);


                    Toast.makeText(EditDataSiswa.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    // adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(EditDataSiswa.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();
//
                map.put("id_siswa", idUser.getText().toString());
                map.put("nama", EdnamaUser.getText().toString());
                map.put("tempat_lahir", Edtmplahir.getText().toString());
                map.put("tanggal_lahir", EdtgllahirUser.getText().toString());
                // params.put("tahun_nmasuk", thnmasuk.getText().toString());

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    public void ubahBerhasil()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setMessage("Perubahan Data Berhasil")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent login = new Intent(EditDataSiswa.this, Profile.class);
                        finish();
                        startActivity(login);

                    }
                });

        AlertDialog berhasil = alert.create();
        berhasil.show();
    }
    public void ubahGagal()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setMessage("Perubahan Data Belum Berhasil")
                .setCancelable(false)
                .setPositiveButton("Ulangi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        recreate();

                    }
                });

        AlertDialog berhasil = alert.create();
        berhasil.show();
    }


    private void showDateDialog()
    {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,month,dayOfMonth);
                Tvtgllahir.setText(dateFormat.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
