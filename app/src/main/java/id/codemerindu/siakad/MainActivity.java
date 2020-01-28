package id.codemerindu.siakad;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class MainActivity extends AppCompatActivity
        implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener{

    private SliderLayout sliderShow;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    TextView namaUser, txt_id,nmuser;
    String id, username, idu,level,levelU,nama;
    NavigationView navigationView;
    SharedPreferences sharedpreferences;

    Boolean session = false;

    private static final String TAG_SUCCESS = "success";
    int success;
    private String url_slider = Server.URL + "slider.php";
    private String url_siswa = Server.URL + "siswa.php";
    public static final String TAG_ID = "id";
    public static final String TAG_IDU = "idu";
    private static final String TAG_LEVEL = "level";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("SIAKAD");
        toolbar.setSubtitle("SMK NEGERI PRIGEN");
        toolbar.setLogo(R.mipmap.ic_logo);


//        Bundle bundle = new Bundle();
//        bundle.putString(TAG_IDU,idu);
// set Fragmentclass Arguments
//        fragSiswa fragobj = new fragSiswa();
//        fragobj.setArguments(bundle);

        txt_id = (TextView)findViewById(R.id.txt_id);

        BottomNavigationView bottomNavigationView = findViewById(R.id.aksesMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                session = sharedpreferences.getBoolean(session_status, false);
                switch (menuItem.getItemId())
                {
                    case  R.id.jadwal:

                        if (session) {
                            Intent jadwal = new Intent(MainActivity.this, jadwal.class);
//                            jadwal.putExtra(TAG_IDU, idu);
                            startActivity(jadwal);

                        }
                        break;
                    case  R.id.grup:

                        if (session) {
                        Intent diskusi = new Intent(MainActivity.this, Diskusi.class);
                            diskusi.putExtra(TAG_IDU, idu);
                        startActivity(diskusi);
                        }
                        break;

                    case  R.id.profil:

                        if (session) {
                            Intent profil = new Intent(MainActivity.this, Profile.class);
                            profil.putExtra(TAG_IDU, idu);
                            startActivity(profil);
//                            Fragment fragment = new fragSiswa();
//                            getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.fragsiswa
//                                            , fragment, fragment.getClass().getSimpleName())
//                                    .addToBackStack(null)
//                                    .commit();
                        }

                        break;

                }
                return false;
            }
        });


//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        menuKiri();
        slider();
        cekdatakosong();


        // Cek session login jika TRUE maka langsung buka Profile
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        idu = sharedpreferences.getString(TAG_ID, null);
        levelU = sharedpreferences.getString(TAG_LEVEL, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);

        if(levelU.equals("siswa"))
        {

//            nmuser = (TextView) findViewById(R.id.navnamaUser);
//            nmuser.setText(nama);

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
//
//    public void alertKeluar()
//    {
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert
//                .setMessage("Tekan Ya Untuk Keluar")
//                .setCancelable(false)
//                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        SharedPreferences.Editor editor = sharedpreferences.edit();
//                        editor.putBoolean(session_status, false);
//                        editor.putString(TAG_ID, null);
//                        editor.putString(TAG_USERNAME, null);
//                        editor.commit();
//
//                        Intent intent = new Intent(MainActivity.this, Login.class);
//                        finish();
//                        startActivity(intent);
//
//                    }
//                })
//                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog keluar = alert.create();
//        keluar.show();
//
//    }

//    public void menuKiri()
//    {
//        NavigationView navigationView = findViewById(R.id.drawer);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//                switch (menuItem.getItemId())
//                {
//                    case R.id.logout:
//
//                        Toast.makeText(MainActivity.this,"PRoses",Toast.LENGTH_LONG).show();
//                        // update login session ke FALSE dan mengosongkan nilai id dan username
////                   alertKeluar();
//                        break;
//
//                }
//
//                return false;
//            }
//        });
//    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
//
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        TableLayout tableLayout = (TableLayout) this.findViewById(R.id.tabelJadwal);
        TableRow row = (TableRow)getLayoutInflater().inflate(R.layout.jadwal_row, null);
        ((TextView)row.findViewById(R.id.noTabel)).setText("2");
        ((TextView)row.findViewById(R.id.jamTabel)).setText("8 - 10 ");
        ((TextView)row.findViewById(R.id.GuruTabel)).setText("aku ");
        ((TextView)row.findViewById(R.id.pelajaranTabel)).setText("ngoding ");

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

    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed()
    {

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
        //super.onBackPressed();
            moveTaskToBack(true);
////            Intent intent = new Intent(MainActivity.this,Login.class);
////            startActivity(intent);
////            return;
        }
        else { Toast.makeText(getBaseContext(), "Tekan Back Sekali lagi untuk Keluar", Toast.LENGTH_SHORT).show(); }
//
        mBackPressed = System.currentTimeMillis();
    }

    public void slider()
    {


        sliderShow = (SliderLayout) findViewById(R.id.slider);

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest = new StringRequest(Request.Method.GET, url_slider, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);

                    for (int i =0; i<dataArray.length(); i++)
                    {

                        JSONObject json = dataArray.getJSONObject(i);

                        HashMap<String, String> url_maps = new HashMap();
                        url_maps.put(json.getString("deskripsi"), json.getString("url"));
                        url_maps.put(json.getString("deskripsi"), json.getString("url"));



                        //-- looping image stored
                        for(String name : url_maps.keySet()){
                            TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                            textSliderView
                                    .description(name)
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(MainActivity.this);

                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", name);

                            sliderShow.addSlider(textSliderView);
                        }
                        sliderShow.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        sliderShow.setCustomAnimation(new DescriptionAnimation());
                        sliderShow.setDuration(3000);
                        sliderShow.addOnPageChangeListener(MainActivity.this);


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
    public void cekdatakosong()
    {


        sliderShow = (SliderLayout) findViewById(R.id.slider);

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest = new StringRequest(Request.Method.GET, url_siswa, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);

                    for (int i =0; i<dataArray.length(); i++)
                    {

                        JSONObject obj = dataArray.getJSONObject(i);
//                        int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDU));
//                        String nama = obj.getString("nama");
//                        int id = obj.getInt("id_siswa");
//                        String id_siswa = obj.getString("id_siswa");
//                        String nisn = obj.getString("nisn");
                        String tempatLahir = obj.getString("tempat_lahir");
//                        String tanggalLahir = obj.getString("tanggal_lahir");
//                        String kodekelas = obj.getString("kode_kelas");
//                        String jurusanS = obj.getString("kode_jurusan");
                        if (tempatLahir.equals("null") || tempatLahir.isEmpty())
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
}

