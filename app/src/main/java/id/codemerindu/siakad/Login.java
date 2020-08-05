package id.codemerindu.siakad;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kuncoro on 03/24/2017.
 */
public class Login extends AppCompatActivity {

    ProgressDialog pDialog;
    Button  btn_login, btn_pemberitahuan;
    EditText txt_username, txt_password;
    Intent intent;
    CheckBox lihatpass;
    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL + "masuk";
    private String url_gantidevice = Server.URL + "gantidevice";


    private static final String TAG = Login.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_LEVEL = "level";
    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID = "id";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_KELAS = "kode_kelas";
    public static final String TAG_SEMESTER = "semester";
    public static final String TAG_FOTO = "tag_foto";

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS= 7;

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, username,nama, level,JWT,Token_JWT,strFoto,kode_kelas,semester;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        startService(new Intent(this, BackgroundService.class));

        checkAndroidVersion();
        FirebaseGetSubscribe();

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);

        final TextView lupaPass = (TextView) findViewById(R.id.btn_lupaPass);lupaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent lupa = new Intent(Login.this,LupaPassword.class);
                startActivity(lupa);
//                Intent webIntent = new Intent(android.content.Intent.ACTION_VIEW);
//                webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=6285369000323&text=Assalamu'alaikum, Saya Lupa Password Login Saya, Bisa Minta Bantuannya"));
//                startActivity(webIntent);

            }
        });
        TextView daftarUlang = (TextView)  findViewById(R.id.daftarUlang);
        daftarUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent isiForm = new Intent(Login.this,daftarUlang.class);
                startActivity(isiForm);
            }
        });
        TextView isiForm = (TextView) findViewById(R.id.btn_isiFormulir);

        isiForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent isiForm = new Intent(Login.this,FormulirPPDB.class);
                startActivity(isiForm);
            }
        });
        lihatpass = (CheckBox) findViewById(R.id.lihatpass);
        lihatpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lihatpass.isChecked())
                {
                    txt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    txt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        btn_login = (Button) findViewById(R.id.btn_login);





        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);

//        username = sharedpreferences.getString(TAG_USERNAME, null);
       // nama = sharedpreferences.getString(TAG_NAMA, null);

        if (session) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_USERNAME, username);
            intent.putExtra(TAG_LEVEL, level);
            intent.putExtra(TAG_FOTO, strFoto);
            intent.putExtra(TAG_NAMA, nama);
            intent.putExtra(JWT,Token_JWT);
            intent.putExtra(TAG_KELAS, kode_kelas);
            intent.putExtra(TAG_SEMESTER, semester);
            finish();
            startActivity(intent);
        }
//        else {
//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putBoolean(session_status, false);
//            editor.putString(TAG_ID, null);
//            editor.putString(TAG_USERNAME, null);
//            editor.commit();
//
//            Intent intent = new Intent(Login.this, Login.class);
//            finish();
//            startActivity(intent);
//        }


        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();

                // mengecek kolom yang kosong
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(username, password);
                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mohon Tunggu...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        String username = jObj.getString(TAG_USERNAME);
                        String id = jObj.getString(TAG_ID);
                        String level = jObj.getString(TAG_LEVEL);
                        String nama = jObj.getString("nama");
                        Token_JWT = jObj.getString("token");
                        kode_kelas = jObj.getString("kode_kelas");
                        semester = jObj.getString("semester");
                        strFoto = jObj.getString("file");

                        Log.e("Successfully Login!", jObj.toString());

//                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(TAG_ID, id);
                        editor.putString(TAG_USERNAME, username);
                        editor.putString(TAG_LEVEL, level);
                        editor.putString(TAG_NAMA, nama);
                        editor.putString(TAG_FOTO, strFoto);
                        editor.putString(JWT, Token_JWT);
                        editor.putString(TAG_KELAS, kode_kelas);
                        editor.putString(TAG_SEMESTER, semester);
                        editor.commit();
//                        if(level.equals("siswa")) {
                            // Memanggil main activity
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra(TAG_ID, id);
                            intent.putExtra(TAG_USERNAME, username);
                            intent.putExtra(TAG_LEVEL, level);
                            intent.putExtra(TAG_NAMA,nama);
                            intent.putExtra(JWT,Token_JWT);
                            intent.putExtra(TAG_FOTO, strFoto);
                            intent.putExtra(TAG_KELAS, kode_kelas);
                            intent.putExtra(TAG_SEMESTER, semester);
                           // finish();
                            startActivity(intent);
//                        }else if(level.equals("admin")){
//
//                            // Memanggil main activity
//                            Intent intent = new Intent(Login.this, AdminActivity.class);
//                            intent.putExtra(TAG_ID, id);
//                            intent.putExtra(TAG_USERNAME, username);
//                            intent.putExtra(TAG_LEVEL, level);
//                            //finish();
//                            startActivity(intent);
//                        }
                    }  else {
                        cekUserAktif("User Sedang Aktif Di Device Lain, Klik Ganti Device Untuk Login Pada Device Ini");
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {
//            //This is for Headers If You Needed
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json; charset=UTF-8");
//                headers.put("Authorization", "Bearer " + Token_JWT);
//                return headers;
//            }
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public void cekUserAktif(String isiPesan)
    {

         AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert
                    .setMessage(isiPesan)
                    .setCancelable(false)
                    .setPositiveButton("Ganti Device", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gantiDevice();
                        }
                    })
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

            AlertDialog kodesalah = alert.create();
            kodesalah.show();

    }

    public void gantiDevice ()
    {


        final String password = txt_password.getText().toString();
        final String username = txt_username.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gantidevice, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);

                    String pesan = dataObj.getString("message");
                    if(pesan.equals("success")){
                        checkLogin(username,password);
                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("gantidevice", "Error: " + error.getMessage());

            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<String, String>();
                map.put("aktif", "0");
                map.put("username", username);
                map.put("deviceId", "NULL");

                return map;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    public void FirebaseGetSubscribe()
    {
        //kirim notif semua user
        FirebaseMessaging.getInstance().subscribeToTopic("umum")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d("pesan subscribe", msg);
//                        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    public void onBackPressed()
    {

        moveTaskToBack(true);
    }

    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();

        } else {
            // code for lollipop and pre-lollipop devices
        }

    }


    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int uuid = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (uuid != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("in fragment on request", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                        Log.d("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("in fragment on request", "Some permissions are not granted ask again ");
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE ) ||
                                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION )) {
                            showDialogOK("Camera and Storage Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }

                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}
