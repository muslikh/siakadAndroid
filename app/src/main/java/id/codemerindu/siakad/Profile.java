package id.codemerindu.siakad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;


public class Profile extends AppCompatActivity {

    TextView namaUser, ttlUser, kodeKelas, jurusan;
    String idu;
    SharedPreferences sharedpreferences;
    public final static String TAG = "Profile";
    public static final String TAG_ID = "id";
    public final static String TAG_IDU = "idu";
    public static final String TAG_USERNAME = "username";
    PagerAdapter pagerAdapter;
    Button btneditdata,btnrefresh;
    ImageView fotoProfile;
    Boolean session = false;


    @Override
    public void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        setContentView(R.layout.profile);


        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);

        btnrefresh = (Button) findViewById(R.id.btnrefreshData);

        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        fotoProfile = (ImageView) findViewById(R.id.fotoProfile);


        Picasso.with(this).load("http://smknprigen.sch.id/bkk/image/default.png").into(fotoProfile);


        btneditdata = (Button) findViewById(R.id.btneditData);
        btneditdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idu = getIntent().getStringExtra(TAG_IDU);
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                session = sharedpreferences.getBoolean(session_status, false);
                if (session) {
                    Intent profil = new Intent(Profile.this, EditDataSiswa.class);
                    profil.putExtra(TAG_IDU, idu);
                    startActivity(profil);
                }
            }
        });

        getSupportActionBar().setTitle("Detail Profil");

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        //Memanggil dan Memasukan Value pada Class PagerAdapter(FragmentManager dan JumlahTab)
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        //Memasang Adapter pada ViewPager
        viewPager.setAdapter(pagerAdapter);

        /*
         Menambahkan Listener yang akan dipanggil kapan pun halaman berubah atau
         bergulir secara bertahap, sehingga posisi tab tetap singkron
         */
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Callback Interface dipanggil saat status pilihan tab berubah.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Dipanggil ketika tab memasuki state/keadaan yang dipilih.
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Dipanggil saat tab keluar dari keadaan yang dipilih.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Dipanggil ketika tab yang sudah dipilih, dipilih lagi oleh user.
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            alertKeluar();
        }

        return false;
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

                        Intent intent = new Intent(Profile.this, Login.class);
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
}
