package id.codemerindu.siakad;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;
import static java.security.AccessController.getContext;

public class FormIjinSakit extends AppCompatActivity {


    String url_absen = Server.URL + "siswa/absen";
    String TAG_ID = "id";
    String TAG_Jenis = "jenis";
    String kenapatdkmasuk;
    public final static String TAG_KELAS = "kode_kelas";
    public final static String TAG_SEMESTER = "semester";
    String id, jenis, semester, kelas;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    RadioGroup kenapa;
    RadioButton radioSakit, radioIjin;
    EditText keterangan;
    Button btnKirim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formijinsakit);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);


        kelas = getIntent().getStringExtra(TAG_KELAS);
        semester = getIntent().getStringExtra(TAG_SEMESTER);

        btnKirim = findViewById(R.id.kirimIjin);
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProsesAbsen();
            }
        });

        kenapa = findViewById(R.id.radioKenapa);
        radioIjin = findViewById(R.id.radioSakit);
        radioSakit = findViewById(R.id.radioIjin);
        kenapa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                kenapatdkmasuk = rb.getText().toString();
            }
        });

        keterangan = findViewById(R.id.edKeterangan);

    }


    public void pesan(final String isiPesan) {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
        alert
                .setMessage(isiPesan)
                .setCancelable(false)
                .setNegativeButton("Okke", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent backD = new Intent(FormIjinSakit.this, kehadiran.class);
                        backD.putExtra(TAG_ID,id);
                        backD.putExtra(TAG_KELAS,kelas);
                        backD.putExtra(TAG_SEMESTER,semester);
                        startActivity(backD);
                    }
                });

        android.app.AlertDialog kodesalah = alert.create();
        kodesalah.show();
    }


    private void ProsesAbsen() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_absen, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);

                    pesan(dataObj.getString("message"));


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "Error: " + error.getMessage());
                pesan(error.getMessage());
            }
        }) {

            @Override

            protected Map<String, String> getParams() throws AuthFailureError {

                Date tgl = new Date();
                Date jam = new Date();
                String dateFormat = DateFormat.getInstance().format(tgl);
                Map<String, String> map = new HashMap<String, String>();
                map.put("siswaID", String.valueOf(id));
                map.put("tgl", dateFormat);
                map.put("jam", dateFormat);
                map.put("jenis", "tidak masuk");
                map.put("kode_kelas", kelas);
                map.put("semester", semester);
                map.put("status", kenapatdkmasuk);
                map.put("keterangan", keterangan.getText().toString());
                map.put("ket_masuk", "tepat waktu");
                map.put("ket_pulang", "tepat waktu");
                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}