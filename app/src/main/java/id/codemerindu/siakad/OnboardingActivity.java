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

public class OnboardingActivity extends AppCompatActivity {


    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    SharedPreferences sharedpreferences;
    Button klogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
       Boolean Onboarding = sharedpreferences.getBoolean(session_status, false);

        if(Onboarding == true)
        {

            startActivity(new Intent(OnboardingActivity.this, Login.class));
            finish();
        }
        klogin = findViewById(R.id.kelogin);
        klogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(session_status, true);
                editor.commit();
                Intent pind = new Intent(OnboardingActivity.this,FormulirPPDB.class);
                startActivity(pind);
            }
        });
    }
}
