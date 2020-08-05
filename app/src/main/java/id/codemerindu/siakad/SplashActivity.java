package id.codemerindu.siakad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {


    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
   final  Boolean Onboarding = sharedpreferences.getBoolean(session_status, false);

//        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
//        if(pref.getBoolean("activity_executed", false)){
//            startActivity(new Intent(this, OnboardingActivity.class));
//            finish();
//        } else {
//            SharedPreferences.Editor editor= pref.edit();
//            editor.putBoolean("activity_executed", true);
//            editor.apply();
//        }





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(Onboarding == false)
                {

                    startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                    finish();
                }else{

                    startActivity(new Intent(SplashActivity.this, FormulirPPDB.class));
                    finish();
                }
            }
        }, 3000);

    }
}
