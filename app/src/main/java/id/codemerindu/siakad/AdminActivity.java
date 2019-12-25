package id.codemerindu.siakad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class AdminActivity extends AppCompatActivity {

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    SharedPreferences sharedpreferences;
    DrawerLayout drawer;
    Boolean session = false;
    public static final String TAG_ID = "id";
    private static final String TAG_LEVEL = "level";
    public static final String TAG_USERNAME = "username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SIAKAD");
        toolbar.setSubtitle("SMK NEGERI PRIGEN");
        toolbar.setLogo(R.mipmap.ic_logo);



        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_admin);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        menuKiriAdmin();

    }


    public void menuKiriAdmin()
    {
        NavigationView navigationView = findViewById(R.id.drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.keluarAdmin:
                        alertKeluar();
                        break;
                    case R.id.validasiSiswa:

                        Intent validasi = new Intent(AdminActivity.this, validasiPPDB.class);
                        finish();
                        startActivity(validasi);

//                        Fragment fragment = new validasiPPDB();
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.Fragment
//                                        , fragment, fragment.getClass().getSimpleName())
//                                .addToBackStack(null)
//                                .commit();
                        break;
                    case R.id.dataSiswa:
                        Intent datasiswa = new Intent(AdminActivity.this,DataSiswa.class);
                        startActivity(datasiswa);
//                        Fragment dataSiswa = new DataSiswa();
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.Fragment
//                                        , dataSiswa, dataSiswa.getClass().getSimpleName())
//                                .addToBackStack(null)
//                                .commit();
                        break;
                    case R.id.bukaMenu:
                        findViewById(R.id.btn_isiFormulir).setEnabled(false);
                        break;

                    case R.id.homeAdmin:
                        Intent homeAdmin = new Intent(AdminActivity.this,AdminActivity.class);
                        startActivity(homeAdmin);
                        break;

                }

                return false;
            }
        });
    }

    public void alertKeluar()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
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

                        Intent intent = new Intent(AdminActivity.this, Login.class);
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

    public boolean onCreateOptionsMenu(Menu menu)
    {
//
//        getMenuInflater().inflate(R.menu.menu_main, menu);

        return  true;
    }

    @Override
    public void onBackPressed()
    {
        alertKeluar();
//        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
//        {
//            super.onBackPressed();
////            SharedPreferences.Editor editor = sharedpreferences.edit();
////            editor.putBoolean(session_status, false);
////            editor.putString(TAG_ID, null);
////            editor.putString(TAG_USERNAME, null);
////            editor.commit();
////
////            Intent intent = new Intent(AdminActivity.this,Login.class);
////            startActivity(intent);
//            return;
//        }
//        else { Toast.makeText(getBaseContext(), "Tekan Back Sekali lagi untuk Keluar", Toast.LENGTH_SHORT).show(); }
//
//        mBackPressed = System.currentTimeMillis();
    }
}
