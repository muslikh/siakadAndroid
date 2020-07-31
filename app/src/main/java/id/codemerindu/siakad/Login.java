package id.codemerindu.siakad;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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

//        if (session) {
//            Intent intent = new Intent(Login.this, MainActivity.class);
//            intent.putExtra(TAG_ID, id);
//            intent.putExtra(TAG_USERNAME, username);
//            intent.putExtra(TAG_LEVEL, level);
//            intent.putExtra(TAG_FOTO, strFoto);
//            intent.putExtra(TAG_NAMA, nama);
//            intent.putExtra(JWT,Token_JWT);
//            intent.putExtra(TAG_KELAS, kode_kelas);
//            intent.putExtra(TAG_SEMESTER, semester);
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

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

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

}
