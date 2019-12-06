package id.codemerindu.siakad;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class MainActivity extends AppCompatActivity
        implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener{

private SliderLayout sliderShow;


    TextView namaUser, txt_id;
    String id, username, idu;
    SharedPreferences sharedpreferences;
    NavigationView navigationView;

    Boolean session = false;

    private static final String TAG_SUCCESS = "success";
    int success;
    private String url = Server.URL + "masuk.php";
    public static final String TAG_ID = "id";
    public static final String TAG_IDU = "idu";
    public static final String TAG_USERNAME = "username";
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt_id = (TextView)findViewById(R.id.txt_id);

        getSupportActionBar().setTitle("SIAKAD");
        toolbar.setSubtitle("SMK NEGERI PRIGEN");
        toolbar.setLogo(R.mipmap.ic_logo);

        BottomNavigationView bottomNavigationView = findViewById(R.id.aksesMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case  R.id.beranda:
//                        Intent beranda = new Intent(MainActivity.this,MainActivity.class);
//                        startActivity(beranda);
                        break;
                    case  R.id.grup:
                        break;
                    case  R.id.profil:

                        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                        session = sharedpreferences.getBoolean(session_status, false);
                        if (session) {
                            Intent profil = new Intent(MainActivity.this, Profile.class);
                            profil.putExtra(TAG_IDU, idu);
                            startActivity(profil);
                        }
                        break;
                }
                return false;
            }
        });

//        txt_username = (TextView) findViewById(R.id.txt_username);
//        namaUser = (TextView) findViewById(R.id.namaUser);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        menuKiri();


        sliderShow = (SliderLayout) findViewById(R.id.slider);

        HashMap<String, String> url_maps = new HashMap();
        url_maps.put("Bengong", "https://lh3.googleusercontent.com/rQPEV7eJZZNU_1clOKBnHIzZZMaD_rgKebi3OJEGVH6oURVodWnVrtXMMhidN5JvuJg=h310");
        url_maps.put("Melongo", "https://i0.wp.com/www.amazine.co/wp-content/uploads/2013/12/Kucing_1.jpg");
        url_maps.put("Apaaa", "http://islamidia.com/wp-content/uploads/2016/07/Kucing-dan-Kedudukannya-Dalam-Pandangan-Islam.jpg");
        url_maps.put("Bobo", "https://hellosehat.com/wp-content/uploads/2016/11/tidur-dengan-kucing.jpg");

        //-- looping image stored
        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            sliderShow.addSlider(textSliderView);
        }
        sliderShow.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderShow.setCustomAnimation(new DescriptionAnimation());
        sliderShow.setDuration(3000);
        sliderShow.addOnPageChangeListener(this);



        // Cek session login jika TRUE maka langsung buka Profile
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        idu = sharedpreferences.getString(TAG_ID, null);

        id = getIntent().getStringExtra(TAG_ID);
  //      nama = getIntent().getStringExtra(TAG_NAMA);
        //username = getIntent().getStringExtra(TAG_MESSA);

    //    namaUser.setText("a"+username);
        txt_id.setText("ID : " + id);
    //    txt_username.setText("USERNAME : " + username);

//getData();

    }


    public void menuKiri()
    {
        NavigationView navigationView = findViewById(R.id.drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.logout:

                        // update login session ke FALSE dan mengosongkan nilai id dan username
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, false);
                        editor.putString(TAG_ID, null);
                        editor.putString(TAG_USERNAME, null);
                        editor.commit();

                        Intent intent = new Intent(MainActivity.this, Login.class);
                        finish();
                        startActivity(intent);

                        break;

                }

                return false;
            }
        });
    }

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

    public void getData()
    {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JSONObject jsonBody = new JSONObject();
//        success = jsonBody.getInt(TAG_SUCCESS);

        //final String requestBody = jsonBody.toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        namaUser.setText(jObj.getString(TAG_USERNAME));
                    }
                    // Check for error node in json
//                    if (success == 1) {
//                        String username = jObj.getString(TAG_USERNAME);
//                        String id = jObj.getString(TAG_ID);
//
//                        Log.e("Successfully Login!", jObj.toString());
//
//                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//
//                        // menyimpan login ke session
//                        SharedPreferences.Editor editor = sharedpreferences.edit();
//                        editor.putBoolean(session_status, true);
//                        editor.putString(TAG_ID, id);
//                        editor.putString(TAG_USERNAME, username);
//                        editor.commit();
//
//                        // Memanggil main activity
//                        Intent intent = new Intent(Login.this, MainActivity.class);
//                        intent.putExtra(TAG_ID, id);
//                        intent.putExtra(TAG_USERNAME, username);
//                        finish();
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(getApplicationContext(),
//                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//
//                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("onErrorResponse: Error", error.toString());
            }
            });
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
        }

    }