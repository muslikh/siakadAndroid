package id.codemerindu.siakad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class AdminActivity extends AppCompatActivity {


    SharedPreferences sharedpreferences;

    Boolean session = false;
    public static final String TAG_ID = "id";
    private static final String TAG_LEVEL = "level";
    public static final String TAG_USERNAME = "username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

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

                        // update login session ke FALSE dan mengosongkan nilai id dan username
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, false);
                        editor.putString(TAG_ID, null);
                        editor.putString(TAG_USERNAME, null);
                        editor.commit();

                        Intent keluar = new Intent(AdminActivity.this, Login.class);
                        finish();
                        startActivity(keluar);
                        break;
                    case R.id.validasiSiswa:

                        Intent validasi = new Intent(AdminActivity.this, validasiPPDB.class);
                        finish();
                        startActivity(validasi);
                        break;
                    case R.id.bukaMenu:
                        findViewById(R.id.btn_isiFormulir).setEnabled(false);
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

        return  true;
    }
}
