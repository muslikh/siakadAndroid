package id.codemerindu.siakad;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.SliderLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class MainActivity extends AppCompatActivity {
//implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener
    private SliderLayout sliderShow;

    ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    public final static String TOKEN = "&token=";
    public final static String TOKEN2 = "?token=";

    TextView txt_id,nmuser,welcomesemester,kelas,bantuan;
    Button lihatsemuajadwal,lihatPengumunan,websmk,elearning,keluar;
    String id, username, idu,level,levelU,nama,JWT,Token_jwt,kode_kelas,semester,strFoto;
//    NavigationView navigationView;
    SharedPreferences sharedpreferences;

    Boolean session = false;

    private static final String TAG_SUCCESS = "success";
    int  extraId;
//    private String url_slider = Server.URL + "slider.php";
final String ambilfoto = Server.URL+"siswa/detail/foto/";
    private String url_siswa = Server.URL + "siswa/detail?id=";
    public static final String TAG_ID = "id";
    public static final String TAG_IDU = "idu";
    private static final String TAG_LEVEL = "level";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_KELAS = "kode_kelas";
    public static final String TAG_SEMESTER = "semester";
    public static final String TAG_FOTO = "tag_foto";
    String TAG_WEB = "tag_web";
    String Title = "title";
    ImageView WelcomefotoProfile;

    private RecyclerView tampilResult;
    public  String tampil = Server.URL+"pengumuman";
    AdapterPengumuman adapterPengumuman;
    ArrayList<HashMap<String ,String>> list_data;

    private FloatingActionButton fabProfil;
    BottomNavigationViewEx navigationViewEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        final String token = FirebaseInstanceId.getInstance().getToken();

        Log.e("LOG","token: " + token);
        Toast.makeText(MainActivity.this,"Token = " +token, Toast.LENGTH_LONG).show();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("AKSI");
        toolbar.setSubtitle("Aplikasi Kesiswaan");
        toolbar.setLogo(R.mipmap.ic_logo);

//        cekdatakosong();
        pengumuman();


        // Cek session login jika TRUE
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        idu = sharedpreferences.getString(TAG_ID, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);
        levelU = sharedpreferences.getString(TAG_LEVEL, null);
        Token_jwt = sharedpreferences.getString(JWT, null);
        kode_kelas = sharedpreferences.getString(TAG_KELAS, null);
        semester = sharedpreferences.getString(TAG_SEMESTER, null);
        strFoto = sharedpreferences.getString(TAG_FOTO, null);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullPengumuman);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pengumuman(); // your code
                ambilfoto();
                pullToRefresh.setRefreshing(false);
            }
        });

        bantuan = findViewById(R.id.bantuan);
        bantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent webIntent = new Intent(android.content.Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=6285369000323&text=Assalamu'alaikum, Saya Ada Kendala, Bisa Minta Bantuannya"));
                startActivity(webIntent);
            }
        });
        ///foto Profil
        WelcomefotoProfile = findViewById(R.id.WelcomefotoProfile);
        ambilfoto();

        kelas = findViewById(R.id.welcomeKelas);
        kelas.setText(kode_kelas);
        welcomesemester = findViewById(R.id.welcomeSemester);
        welcomesemester.setText(semester);
        websmk = findViewById(R.id.websekolah);
        websmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent websmk = new Intent(MainActivity.this, WebviewSMK.class);
                websmk.putExtra(TAG_WEB, "http://smknprigen.sch.id/");
                websmk.putExtra(Title, "Web Sekolah");
                startActivity(websmk);


            }
        });
        elearning = findViewById(R.id.e_learning);
        elearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent elearning = new Intent(MainActivity.this, WebviewSMK.class);
                elearning.putExtra(TAG_WEB, "http://kbm.smknprigen.sch.id/Login");
                elearning.putExtra(Title, "E-Learning");
                startActivity(elearning);

            }
        });
//        lihatPengumunan = findViewById(R.id.lihatSemuaPengumuman);
//        lihatPengumunan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent pengumuman = new Intent(MainActivity.this, Pengumuman.class);
////                            jadwal.putExtra(TAG_IDU, idu);
//                startActivity(pengumuman);
//            }
//        });
        lihatsemuajadwal = findViewById(R.id.lihatJadwal);
        lihatsemuajadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session) {
                    Intent jadwal = new Intent(MainActivity.this, jadwal.class);
                            jadwal.putExtra(TAG_KELAS, kode_kelas );
                    startActivity(jadwal);

                }
            }
        });

        keluar = findViewById(R.id.logout);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert
                            .setMessage("Tekan Ya Untuk Keluar")
                            .setCancelable(false)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putBoolean(session_status, false);
                                    editor.putString(TAG_ID, null);
                                    editor.putString(TAG_USERNAME, null);
                                    editor.commit();

                                    Intent intent = new Intent(MainActivity.this, Login.class);
                                    finish();
                                    startActivity(intent);

                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog keluar = alert.create();
                    keluar.show();

            }
        });

        fabProfil = (FloatingActionButton)  findViewById(R.id.fabProfil);
        fabProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session) {
                    Intent Tugaas = new Intent(MainActivity.this, Profile.class);
                    Tugaas.putExtra(TAG_IDU, idu);
                    Tugaas.putExtra(JWT, Token_jwt);
                    Tugaas.putExtra(TAG_FOTO, strFoto);
                    startActivity(Tugaas);
                }
            }
        });
        BottomNavigationViewEx navigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bnve);
       // navigationViewEx.setIconSize(5, 5);
        navigationViewEx.setTextSize(12);

        navigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch (menuItem.getItemId())
                {
                    case  R.id.kehadiran:

                        if (session) {
                            Intent kehadiran = new Intent(MainActivity.this, kehadiran.class);
                            kehadiran.putExtra(TAG_IDU, idu);
                            kehadiran.putExtra(TAG_KELAS, kode_kelas);
                            kehadiran.putExtra(TAG_SEMESTER, semester);
                            startActivity(kehadiran);

                        }
                        break;
//                    case  R.id.grup:
//
//                        if (session) {
//                            Intent diskusi = new Intent(MainActivity.this, Diskusi.class);
//                            diskusi.putExtra(TAG_IDU, idu);
//                            startActivity(diskusi);
//                        }
//                        break;
                    case  R.id.kumpulannilai:

                        if (session) {
                            Intent Tugaas = new Intent(MainActivity.this, KumpulanNilai.class);
                            Tugaas.putExtra(TAG_IDU, idu);
                            startActivity(Tugaas);
                        }
                        break;


                }
                return false;
            }
        });


//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        menuKiri();
//        slider();


        if(levelU.equals("siswa"))
        {

            nmuser = (TextView) findViewById(R.id.welcomeNama) ;
            nmuser.setText(nama);

        }else if(levelU.equals("admin"))
        {
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_USERNAME, username);
            intent.putExtra(TAG_LEVEL, level);
            intent.putExtra(TAG_NAMA,nama);
            startActivity(intent);
        }else if(levelU.equals("siswabaru"))
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert
                    .setMessage("Maaf Data Belum Kami Validasi, Maksimal 1 Hari setelah Isi Form. Mohon Bersabar Yaaa\n \n Terima Kasih  :) \n \n Admin ")
                    .setCancelable(false)
                    .setNegativeButton("Siap Menunggu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(session_status, false);
                            editor.putString(TAG_ID, null);
                            editor.putString(TAG_USERNAME, null);
                            editor.commit();
                            Intent tunggu = new Intent(MainActivity.this,Login.class);
                            finish();
                            startActivity(tunggu);
                        }
                    });

            AlertDialog berhasil = alert.create();
            berhasil.show();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        TableLayout tableLayout = (TableLayout) this.findViewById(R.id.tabelJadwal);
        TableRow row = (TableRow)getLayoutInflater().inflate(R.layout.jadwal_row, null);
        ((TextView)row.findViewById(R.id.noTabel)).setText("1");
        ((TextView)row.findViewById(R.id.jamTabel)).setText("8 - 10 ");
        ((TextView)row.findViewById(R.id.GuruTabel)).setText("Muslikh ");
        ((TextView)row.findViewById(R.id.pelajaranTabel)).setText("JarDas ");

        return  true;
    }
//    public boolean onOptionsItemSelected(MenuItem item){
//        int id = item.getItemId();
//
//            if(id ==  R.id.leftMenu)
//            {
//
//        }
//        return false;
//    }
//
//    @Override
//    protected void onStop() {
//        sliderShow.stopAutoCycle();
//        super.onStop();
//    }
//
//    @Override
//    public void onSliderClick(BaseSliderView slider) {
//        Toast.makeText(this, slider.getBundle().get("extra") + "",
//                Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        Log.e("Slider Demo", "Page Changed: " + position);
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }

    @Override
    public void onBackPressed()
    {

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
        //super.onBackPressed();
            moveTaskToBack(true);
        }
        else { Toast.makeText(getBaseContext(), "Tekan Back Sekali lagi untuk Keluar", Toast.LENGTH_SHORT).show(); }
//
        mBackPressed = System.currentTimeMillis();
    }
//
//    public void slider()
//    {
//
//
//        sliderShow = (SliderLayout) findViewById(R.id.slider);
//
//        requestQueue = Volley.newRequestQueue(MainActivity.this);
//        stringRequest = new StringRequest(Request.Method.GET, url_slider, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try{
//                    JSONArray dataArray= new JSONArray(response);
//
//                    for (int i =0; i<dataArray.length(); i++)
//                    {
//
//                        JSONObject json = dataArray.getJSONObject(i);
//
//                        HashMap<String, String> url_maps = new HashMap();
//                        url_maps.put(json.getString("deskripsi"), json.getString("url"));
//
//
//
//                        //-- looping image stored
//                        for(String name : url_maps.keySet()){
//                            TextSliderView textSliderView = new TextSliderView(MainActivity.this);
//                            textSliderView
//                                    .description(name)
//                                    .image(url_maps.get(name))
//                                    .setScaleType(BaseSliderView.ScaleType.Fit)
//                                    .setOnSliderClickListener(MainActivity.this);
//
//                            textSliderView.bundle(new Bundle());
//                            textSliderView.getBundle()
//                                    .putString("extra", name);
//
//                            sliderShow.addSlider(textSliderView);
//                        }
//                        sliderShow.setPresetTransformer(SliderLayout.Transformer.Accordion);
//                        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//                        sliderShow.setCustomAnimation(new DescriptionAnimation());
//                        sliderShow.setDuration(60000);
//                        sliderShow.addOnPageChangeListener(MainActivity.this);
//
//
//                    }
//                } catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener()
//        {
//            public void onErrorResponse(VolleyError error)
//            {
//                //Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
//            }
//        });
//        requestQueue.add(stringRequest);
//
//    }
    public void cekdatakosong()
    {


        extraId = Integer.parseInt(idu);
        sliderShow = (SliderLayout) findViewById(R.id.slider);

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest = new StringRequest(Request.Method.GET, url_siswa+extraId+TOKEN+Token_jwt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);

                    for (int i =0; i<dataArray.length(); i++)
                    {

                        JSONObject obj = dataArray.getJSONObject(i);
                        String nama = obj.getString("nama");
                        int id = obj.getInt("id");
//                        String nisn = obj.getString("nisn");
                        String tempatLahir = obj.getString("tempat_lahir");
                        String tanggalLahir = obj.getString("tanggal_lahir");
//                        String kodekelas = obj.getString("kode_kelas");
//                        String jurusanS = obj.getString("kode_jurusan");
                        if(extraId==id) {
                            if (tempatLahir.equals("null") || tempatLahir.isEmpty() ) {
                                Lengkapi();
                            }else if (tanggalLahir.equals("null") || tanggalLahir.isEmpty() ) {
                                Lengkapi();
                            }
                        }

                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);

    }

    public void Lengkapi()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert
                .setMessage("Ada Data Yang masih kosong Nih, Lengkapi Yuukk !")
                .setCancelable(false)
                .setPositiveButton("Siap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        Intent cek = new Intent(MainActivity.this, EditDataSiswa.class);
                        cek.putExtra(TAG_IDU, idu);
                        startActivity(cek);

                    }
                })
                .setNegativeButton("Nanti Saja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog keluar = alert.create();
        keluar.show();
    }

    public void pengumuman()
    {
        tampilResult = (RecyclerView) findViewById(R.id.listPengumuman);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        tampilResult.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest = new StringRequest(Request.Method.GET, tampil, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);
                    for (int i =0; i<dataArray.length(); i++)
                    {

                        JSONObject json = dataArray.getJSONObject(i);
                        HashMap<String, String > map = new HashMap<String , String >();
                        map.put("isi_pengumuman", json.getString("isi_pengumuman"));
                        map.put("judul_pengumuman",json.getString("judul_pengumuman"));
                        map.put("tgl_pengumuman",json.getString("tgl_pengumuman"));
                        map.put("level",json.getString("level"));
                        list_data.add(map);
                        adapterPengumuman = new AdapterPengumuman(MainActivity.this, list_data);
                        tampilResult.setAdapter(adapterPengumuman);

                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }
    private void ambilfoto()
    {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Proses Pengambilan Data, Mohon Tunggu...");
        progressDialog.show();


        StringRequest request = new StringRequest(Request.Method.GET, ambilfoto+idu+TOKEN2+Token_jwt, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONArray dataArray= new JSONArray(response);

                    for (int i =0; i<dataArray.length(); i++) {

                        JSONObject obj = dataArray.getJSONObject(i);
                        String fotofile = obj.getString("file");


//
                        if (fotofile.isEmpty()) {
                            Glide.with(getApplication())
                                    .load("http://muslikh.my.id/default.png")
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(WelcomefotoProfile);
//                                    Picasso.with(getApplication()).load("http://muslikh.my.id/default.png").into(fotoProfile);
                        } else if (fotofile.equals("null")) {

                            Glide.with(getApplication())
                                    .load("http://muslikh.my.id/default.png")
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(WelcomefotoProfile);
//                                    Picasso.with(getApplication()).load("http://muslikh.my.id/default.png").into(fotoProfile);
                        } else {

                            Glide.with(getApplication())
                                    .load(Server.data+"/images/"+fotofile)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(WelcomefotoProfile);
//                                    fotoProfile.setImageBitmap(decodedByte);
                        }
//                            }
//                        }
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
    }


}

