package id.codemerindu.siakad;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class LupaPassword extends AppCompatActivity {


    String cekemail = Server.URL +"lupapass?email=";
    String gantipass  = Server.URL+"gantipass";
    final String TAG ="Edit";
    public final static String TAG_IDU = "idu";
    public final static String TAG_MESSAGE = "message";
    EditText EdGantiPass,edEmail,edTulisKode;
    Button btnSimpan,btnkirim;
    String email;
    TextView kode,tvPWBAru;

    public int extraId;
    SharedPreferences sharedpreferences;

    Boolean session = false;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lupa_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarlupaPassword);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Lupa Password");

        edEmail = findViewById(R.id.tulisEmail);

        EdGantiPass = findViewById(R.id.tulisPWBaru);
        btnkirim = findViewById(R.id.btnKirimEmail);
        btnkirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekEmail();
            }
        });
        btnSimpan = findViewById(R.id.btnGantiPW);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gantiPass();
            }
        });
        edTulisKode = findViewById(R.id.edTulisKode);
        kode = findViewById(R.id.tvKode);
        tvPWBAru = findViewById(R.id.tvPWBAru);

    }

    public void cekEmail()
    {
        progressDialog = new ProgressDialog(LupaPassword.this);
        progressDialog.setMessage("Proses , Mohon Tunggu...");
        progressDialog.show();

        email = edEmail.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, cekemail+email, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

                            progressDialog.dismiss();
                            for (int i =0; i<dataArray.length(); i++)
                            {


                                JSONObject obj = dataArray.getJSONObject(i);
                                String pesan = obj.getString("message");
                                if (pesan.equals("sukses") )
                                {

                                    pesan("Kode Terikirim,Silahkan Periksa Emailmu");
                                    edEmail.setVisibility(View.GONE);
                                    btnkirim.setVisibility(View.GONE);
                                    EdGantiPass.setVisibility(View.VISIBLE);
                                    edTulisKode.setVisibility(View.VISIBLE);
                                    btnSimpan.setVisibility(View.VISIBLE);
                                    tvPWBAru.setVisibility(View.VISIBLE);
//                                    kode.setText();

                                }else if (pesan.equals("gagal")) {
                                    Gagal();
                                    EdGantiPass.setVisibility(View.GONE);
                                    edTulisKode.setVisibility(View.GONE);
                                    btnSimpan.setVisibility(View.GONE);
                                    tvPWBAru.setVisibility(View.GONE);
                                    edEmail.setVisibility(View.VISIBLE);
                                    btnkirim.setVisibility(View.VISIBLE);
                                }

                            }
                            Log.d(TAG, "onResponse:" + response);
                        }  catch(
                                JSONException e)

                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                        Toast.makeText(LupaPassword.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(stringRequests);
    }
    public void gantiPass()
    {
        progressDialog = new ProgressDialog(LupaPassword.this);
        progressDialog.setMessage("Proses Simpan, Mohon Tunggu...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, gantipass, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);

                    progressDialog.dismiss();

                    String code = dataObj.getString(TAG_MESSAGE);
//                    status = dataObj.getString("status_siswa");
                    if (code.equals("berhasil"))
                    {

                        Berhasil();
                    }else if (code.equals("terpakai"))
                    {
                        Terpakai();
                    }else if (code.equals("gagal"))
                    {
                        Gagal();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(LupaPassword.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();
//
//                map.put("id", Integer.toString(extraId));
                  map.put("kode",edTulisKode.getText().toString());
                map.put("password",EdGantiPass.getText().toString());
                map.put("email",email);


                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    public void  Berhasil()
    {

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setMessage("Perubahan Password Berhasil, Silahkan Login")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent kelogin = new Intent(LupaPassword.this,Login.class);
                         startActivity(kelogin);

//                            Intent back = new Intent(LupaPassword.this,Profile.class);
//                            back.putExtra(TAG_IDU,Integer.toString(extraId));
//                            startActivity(back);

                    }
                });

        AlertDialog berhasil = alert.create();
        berhasil.show();
    }
    public void  Gagal()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setMessage("Perubahan Gagal Mohon Periksa Email Dan Kode dengan Benar")
                .setCancelable(false)
                .setPositiveButton("Ulangi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        recreate();

                    }
                });

        AlertDialog berhasil = alert.create();
        berhasil.show();
    }
    public void  Terpakai()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setMessage("Username Sudah Di Gunakan / Silahkan Gunakan Yang Lain")
                .setCancelable(false)
                .setPositiveButton("Ulangi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        recreate();

                    }
                });

        AlertDialog berhasil = alert.create();
        berhasil.show();
    }

    public void pesan(final String isiPesan)
    {  android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
        alert
                .setMessage(isiPesan)
                .setCancelable(false)
                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent backD = new Intent(EditDataSiswa.this,Login.class);
//                        startActivity(backD);
//                        recreate();
                    }
                });

        android.app.AlertDialog kodesalah = alert.create();
        kodesalah.show();
    }

}
