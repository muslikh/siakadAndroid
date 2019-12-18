package id.codemerindu.siakad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        Button keluadAdmin = (Button) findViewById(R.id.keluarAdmin);
        keluadAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update login session ke FALSE dan mengosongkan nilai
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.putString(TAG_LEVEL, null);
                editor.commit();

                Intent intent = new Intent(AdminActivity.this, Login.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
