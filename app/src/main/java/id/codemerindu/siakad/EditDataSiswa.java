package id.codemerindu.siakad;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class EditDataSiswa extends AppCompatActivity {


    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;
    String url = Server.URL +"siswa.php?aksi=tampil_siswa";
    String url_update  = Server.URL+"siswa.php?aksi=editDataSiswa";
    final String TAG ="Edit";
    public final static String TAG_IDU = "idu";
    public final static String TAG_MESSAGE = "message";
    EditText EdnamaUser,Edtmplahir,EdtgllahirUser;
    TextView idUser,nipd,nisnUser,TvkodeKelas,Tvjurusan,Tvtgllahir,kelasAwal,thnmasuk,textfoto;
    EditText namaUser,ttlUser,kodeKelas,jurusan,
    nisn,nik,kewarga,anakke,jmlsdrkandung,jmlsdrtiri,
    hobi,alamat,rt,rw,dusun,kab,prov,hp,stsTinggal,goldar,penyakit,
    tinggi,berat,lulusdari,noijasah,noskhun,nounsmp,nopeUser,pindahdari,alamatSsebelum,
            namaAyah,ttlAyah,agamaAyah,KewargaAyah,pendidikanAyah,kerjaAyah,pengeluaranAyah,AlamatAyah,nohpAyah,
            namaIbu,ttlIbu,agamaIbu,KewargaIbu,pendidikanIbu,kerjaIbu,pengeluaranIbu,AlamatIbu,nohpIbu,
            namaWali,ttlWali,agamaWali,KewargaWali,pendidikanWali,kerjaWali,pengeluaranWali,AlamatWali,nohpWali,
            password,username;
    int extraId;
    Spinner agama,jk;
    Button updateSiswa;
    ImageView fotoProfile;

    SharedPreferences sharedpreferences;

    Boolean session = false;
    
    ProgressDialog progressDialog;

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
        jk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        agama = ( Spinner)   findViewById(R.id.agmUser);
        agama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
        nounsmp = (EditText) findViewById(R.id.nounUser);

        namaAyah = (EditText) findViewById(R.id.ednamaAyah);
        ttlAyah = (EditText) findViewById(R.id.edttlAyah);
        agamaAyah = (EditText) findViewById(R.id.edagamaAyah);
        KewargaAyah= (EditText) findViewById(R.id.edKewargaAyah);
        pendidikanAyah = (EditText) findViewById(R.id.edpendidikanAyah);
        kerjaAyah= (EditText) findViewById(R.id.edkerjaAyah);
        pengeluaranAyah =(EditText) findViewById(R.id.edpengeluaranAyah);
        AlamatAyah = (EditText) findViewById(R.id.edAlamatAyah);
        nohpAyah = (EditText) findViewById(R.id.ednohpAyah);
        namaIbu = (EditText) findViewById(R.id.ednamaIbu);
        ttlIbu= (EditText) findViewById(R.id.edttlIbu);
        agamaIbu = (EditText) findViewById(R.id.edagamaIbu);
        KewargaIbu = (EditText) findViewById(R.id.edKewargaIbu);
        pendidikanIbu= (EditText) findViewById(R.id.edpendidikanIbu);
        kerjaIbu= (EditText) findViewById(R.id.edkerjaIbu);
        pengeluaranIbu= (EditText) findViewById(R.id.edpengeluaranIbu);
        AlamatIbu= (EditText) findViewById(R.id.edAlamatIbu);
        nohpIbu= (EditText) findViewById(R.id.ednohpIbu);
        namaWali= (EditText) findViewById(R.id.ednamaWali);
        ttlWali= (EditText) findViewById(R.id.edttlWali);
        agamaWali= (EditText) findViewById(R.id.edagamaWali);
        KewargaWali= (EditText) findViewById(R.id.edKewargaWali);
        pendidikanWali= (EditText) findViewById(R.id.edpendidikanWali);
        kerjaWali= (EditText) findViewById(R.id.edkerjaWali);
        pengeluaranWali= (EditText) findViewById(R.id.edpengeluaranWali);
        AlamatWali= (EditText)  findViewById(R.id.edAlamatWali);
        nohpWali= (EditText) findViewById(R.id.ednohpWali);
        username= (EditText) findViewById(R.id.edUsername);
        password= (EditText) findViewById(R.id.edPassword);
    }

    public void editData()
    {
        progressDialog = new ProgressDialog(EditDataSiswa.this);
        progressDialog.setMessage("Proses Pengambilan Data, Mohon Tunggu...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

                            progressDialog.dismiss();
                            for (int i =0; i<dataArray.length(); i++)
                            {


                                JSONObject obj = dataArray.getJSONObject(i);
                                extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDU));
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

                                    nounsmp.setText(obj.getString("no_unsmp"));
                                    nopeUser.setText(obj.getString("hp"));
                                    nipd.setText(obj.getString("nipd"));
                                    nik.setText(obj.getString("nik"));
                                    kewarga.setText(obj.getString("kewarganegaraan"));
                                    anakke.setText(obj.getString("anak_ke"));
                                    jmlsdrkandung.setText(obj.getString("jml_sdrkandung"));
                                    jmlsdrtiri.setText(obj.getString("jml_sdrtiri"));
                                    hobi.setText(obj.getString("hobi"));
                                    alamat.setText(obj.getString("alamat"));
                                    rt.setText(obj.getString("rt"));
                                    rw.setText(obj.getString("rw"));
                                    dusun.setText(obj.getString("dusun"));
                                    kab.setText(obj.getString("kabupaten"));
                                    prov.setText(obj.getString("provinsi"));
                                    stsTinggal.setText(obj.getString("jenis_tinggal"));
                                    goldar.setText(obj.getString("goldarah"));
                                    penyakit.setText(obj.getString("sakitygpernah"));
                                    tinggi.setText(obj.getString("tinggi_badan"));
                                    berat.setText(obj.getString("berat_badan"));
                                    lulusdari.setText(obj.getString("darisekolah"));
                                    noijasah.setText(obj.getString("no_ijasah"));
                                    noskhun.setText(obj.getString("no_skhu"));
                                    pindahdari.setText(obj.getString("pindahdari"));
                                    alamatSsebelum.setText(obj.getString("alamatsekolah"));
                                    kelasAwal.setText(obj.getString("kelas_awal"));
                                    thnmasuk.setText(obj.getString("tahun_masuk"));

                                    namaAyah.setText(obj.getString("nama_ayah"));
                                    ttlAyah.setText(obj.getString("tanggal_lahir_ayah"));
                                    agamaAyah.setText(obj.getString("agama_ayah"));
                                    KewargaAyah.setText(obj.getString("kewarga_ayah"));
                                    pendidikanAyah.setText(obj.getString("pendidikan_ayah"));
                                    kerjaAyah.setText(obj.getString("pekerjaan_ayah"));
                                    pengeluaranAyah.setText(obj.getString("pengeluaran_ayah"));
                                    AlamatAyah.setText(obj.getString("alamat_ayah"));
                                    nohpAyah.setText(obj.getString("no_telpon_ayah"));
                                    namaIbu.setText(obj.getString("nama_ibu"));
                                    ttlIbu.setText(obj.getString("tanggal_lahir_ibu"));
                                    agamaIbu.setText(obj.getString("agama_ibu"));
                                    KewargaIbu.setText(obj.getString("kewarga_ibu"));
                                    pendidikanIbu.setText(obj.getString("pendidikan_ibu"));
                                    kerjaIbu.setText(obj.getString("pekerjaan_ibu"));
                                    pengeluaranIbu.setText(obj.getString("pengeluaran_ibu"));
                                    AlamatIbu.setText(obj.getString("alamat_ibu"));
                                    nohpIbu.setText(obj.getString("no_telpon_ibu"));
                                    namaWali.setText(obj.getString("nama_wali"));
                                    ttlWali.setText(obj.getString("tanggal_lahir_wali"));
                                    agamaWali.setText(obj.getString("agama_wali"));
                                    KewargaWali.setText(obj.getString("kewarga_wali"));
                                    pendidikanWali.setText(obj.getString("pendidikan_wali"));
                                    kerjaWali.setText(obj.getString("pekerjaan_wali"));
                                    pengeluaranWali.setText(obj.getString("pengeluaran_wali"));
                                    AlamatWali.setText(obj.getString("alamat_wali"));
                                    nohpWali.setText(obj.getString("no_telpon_wali"));


                                    username.setText(obj.getString("username"));
                                    password.setText(obj.getString("password"));

//                                    String cekUsername = obj.getString("username");
//                                    String cekPassword = obj.getString("password");
//                                    if(cekUsername.isEmpty())
//                                    {
//                                        username.setEnabled(true);
//                                    }else if(cekPassword.isEmpty())
//                                    {
//                                        username.setEnabled(true);
//                                    }
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
        progressDialog = new ProgressDialog(EditDataSiswa.this);
        progressDialog.setMessage("Proses Simpan, Mohon Tunggu...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);
                    progressDialog.dismiss();


                    String code = dataObj.getString(TAG_MESSAGE);
                    if (code.equals("sukses"))
                    {
                        ubahBerhasil();
                    }else if (code.equals("gagal"))
                    {
                        ubahGagal();
                    }

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
                map.put("id_jenis_kelamin",String.valueOf(jk.getSelectedItem()));
                map.put("id_agama",String.valueOf(agama.getSelectedItem()));
                map.put("hp", nopeUser.getText().toString());
                map.put("no_unsmp", nounsmp.getText().toString());
                map.put("nipd", nipd.getText().toString());
                map.put("nik", nik.getText().toString());
                map.put("kewarganegaraan", kewarga.getText().toString());
                map.put("anak_ke", anakke.getText().toString());
                map.put("jml_sdrkandung", jmlsdrkandung.getText().toString());
                map.put("jml_sdrtiri", jmlsdrtiri.getText().toString());
                map.put("hobi", hobi.getText().toString());
                map.put("alamat", alamat.getText().toString());
                map.put("rt", rt.getText().toString());
                map.put("rw", rw.getText().toString());
                map.put("dusun", dusun.getText().toString());
                map.put("kabupaten", kab.getText().toString());
                map.put("provinsi", prov.getText().toString());
                map.put("jenis_tinggal", stsTinggal.getText().toString());
                map.put("goldarah", goldar.getText().toString());
                map.put("sakitygpernah", penyakit.getText().toString());
                map.put("tinggi_badan", tinggi.getText().toString());
                map.put("berat_badan", berat.getText().toString());
                map.put("darisekolah", lulusdari.getText().toString());
                map.put("no_ijasah", noijasah.getText().toString());
                map.put("no_skhu", noskhun.getText().toString());
                map.put("pindahdari", pindahdari.getText().toString());
                map.put("alamatsekolah", alamatSsebelum.getText().toString());
                map.put("kelas_awal", kelasAwal.getText().toString());
                map.put("tahun_masuk", thnmasuk.getText().toString());

                map.put("nama_ayah",namaAyah.getText().toString());
                map.put("tanggal_lahir_ayah",ttlAyah.getText().toString());
                 map.put("agama_ayah",agamaAyah.getText().toString());
                 map.put("kewarga_ayah",KewargaAyah.getText().toString());
                 map.put("pendidikan_ayah",pendidikanAyah.getText().toString());
                 map.put("pekerjaan_ayah",kerjaAyah.getText().toString());
                 map.put("pengeluaran_ayah",pengeluaranAyah.getText().toString());
                 map.put("alamat_ayah",AlamatAyah.getText().toString());
                 map.put("no_telpon_ayah",nohpAyah.getText().toString());
                 map.put("nama_ibu",namaIbu.getText().toString());
                 map.put("tanggal_lahir_ibu",ttlIbu.getText().toString());
                 map.put("agama_ibu",agamaIbu.getText().toString());
                  map.put("kewarga_ibu",KewargaIbu.getText().toString());
                  map.put("pendidikan_ibu",pendidikanIbu.getText().toString());
                  map.put("pekerjaan_ibu",kerjaIbu.getText().toString());
                  map.put("pengeluaran_ibu",pengeluaranIbu.getText().toString());
                  map.put("alamat_ibu",AlamatIbu.getText().toString());
                  map.put("no_telpon_ibu",nohpIbu.getText().toString());
                  map.put("nama_wali",namaWali.getText().toString());
                  map.put("tanggal_lahir_wali",ttlWali.getText().toString());
                  map.put("agama_wali",agamaWali.getText().toString());
                  map.put("kewarga_wali",KewargaWali.getText().toString());
                  map.put("pendidikan_wali",pendidikanWali.getText().toString());
                  map.put("pekerjaan_wali",kerjaWali.getText().toString());
                  map.put("pengeluaran_wali",pengeluaranWali.getText().toString());
                  map.put("alamat_wali",AlamatWali.getText().toString());
                  map.put("no_telpon_wali",nohpWali.getText().toString());
                  map.put("username",username.getText().toString());
                  map.put("password",password.getText().toString());


                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    public void ubahBerhasil()
    {

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setMessage("Perubahan Data Berhasil")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(session){

                            Intent back = new Intent(EditDataSiswa.this,Profile.class);
                            back.putExtra(TAG_IDU,Integer.toString(extraId));
                            startActivity(back);
                        }else{
                            pesan(
                                    "Daftar Ulang Berhasil, Silahkan Login Dan dapatkan Info Terbaru Dari Aplikasi");

                        }
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

    public void pesan(final String isiPesan)
    {  android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
        alert
                .setMessage(isiPesan)
                .setCancelable(false)
                .setNegativeButton("Okke", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent backD = new Intent(EditDataSiswa.this,Login.class);
                        startActivity(backD);
                    }
                });

        android.app.AlertDialog kodesalah = alert.create();
        kodesalah.show();
    }
}
