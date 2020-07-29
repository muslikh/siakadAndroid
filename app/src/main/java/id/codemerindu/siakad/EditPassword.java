package id.codemerindu.siakad;

import android.app.DatePickerDialog;
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

public class EditPassword extends AppCompatActivity {


    String ambildata = Server.URL +"siswa/daftarulang?id=";
    String simpandata  = Server.URL+"siswa/buatLogin/";
    final String TAG ="Edit";
    public final static String TAG_IDU = "idu";
    public final static String TAG_MESSAGE = "message";
    EditText EdGantiPass,EdGantiUser;
    Button btnSimpan;
    String status;

    public int extraId;
    SharedPreferences sharedpreferences;

    Boolean session = false;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_userpass);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarGantiPassword);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Buat / Ganti Data Login");

        EdGantiUser = findViewById(R.id.buatUsername);
        EdGantiPass = findViewById(R.id.buatPassword);
        btnSimpan = findViewById(R.id.btnGantiPass);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpanData();
            }
        });

        getData();
    }

    public void getData()
    {
        progressDialog = new ProgressDialog(EditPassword.this);
        progressDialog.setMessage("Proses Pengambilan Data, Mohon Tunggu...");
        progressDialog.show();

        extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDU));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, ambildata+extraId, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

                            progressDialog.dismiss();
                            for (int i =0; i<dataArray.length(); i++)
                            {


                                JSONObject obj = dataArray.getJSONObject(i);
                                int id = obj.getInt("id");
                                if (extraId== id )
                                {
                                    EdGantiUser.setText(obj.getString("username"));
                                    EdGantiPass.setText(obj.getString("password"));

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
                        Toast.makeText(EditPassword.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(stringRequests);
    }
    public void SimpanData()
    {
        progressDialog = new ProgressDialog(EditPassword.this);
        progressDialog.setMessage("Proses Simpan, Mohon Tunggu...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, simpandata+extraId+'?', new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);
                    progressDialog.dismiss();


                    String code = dataObj.getString(TAG_MESSAGE);
//                    status = dataObj.getString("status_siswa");
                    if (code.equals("sukses"))
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
                Toast.makeText(EditPassword.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();
//
                map.put("id", Integer.toString(extraId));
                  map.put("username",EdGantiUser.getText().toString());
                  map.put("password",EdGantiPass.getText().toString());


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
                .setMessage("Berhasil")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if(session){
//                            if(status=="belum_aktif")
//                            {
                            Intent back = new Intent(EditPassword.this,Profile.class);
                            back.putExtra(TAG_IDU,Integer.toString(extraId));
                            startActivity(back);
                            }else{
                            Intent back = new Intent(EditPassword.this,EditDataSiswa.class);
                            back.putExtra(TAG_IDU,Integer.toString(extraId));
                            startActivity(back);
                            }
//                        }else{
//                            pesan("Proses Gagal");
//
//                        }
                    }
                });

        AlertDialog berhasil = alert.create();
        berhasil.show();
    }
    public void  Gagal()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setMessage("Perubahan Data Belum Berhasil")
                .setCancelable(false)
                .setPositiveButton("Ulangi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        recreate();

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

                        recreate();

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
                .setNegativeButton("Ulangi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent backD = new Intent(EditDataSiswa.this,Login.class);
//                        startActivity(backD);
                        recreate();
                    }
                });

        android.app.AlertDialog kodesalah = alert.create();
        kodesalah.show();
    }

}
