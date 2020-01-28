package id.codemerindu.siakad;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;

public class FormulirPPDB extends AppCompatActivity {


    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;
    private String firstItem;
    private TextView txtjk,tgllahir,thnajaran;
    private Spinner ppdbjk,jurusan;
    private EditText username,password,nisn,nama,thnmasuk,tmplahir,edtlahir;
    private Button simpan;
    private String TAG_LEVEL = "level";
    private String TAG_SUCCESSS = "success";
    private String TAG_MESSAGE = "message";
    private static String url = Server.URL+"insert.php";
    private static String url_update = Server.URL + "update.php";
    String levelU;

    public static final String TAG_IDU = "idu";
    SharedPreferences sharedpreferences;

    Boolean session = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulir_ppdb);


        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        levelU = sharedpreferences.getString(TAG_LEVEL, null);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarPPDB);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Formulir Peserta Didik Baru");

        dateFormat = new SimpleDateFormat("dd-MM-yyy", Locale.US);
        ppdbjk = (Spinner) findViewById(R.id.ppdbjk);
        ppdbjk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String firstItem = String.valueOf(ppdbjk.getSelectedItem());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jurusan = (Spinner) findViewById(R.id.ppdb_jurusan);
        jurusan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final RequestQueue request = Volley.newRequestQueue(getApplicationContext());
        thnajaran = (TextView) findViewById(R.id.tahunppdb);
        username = (EditText) findViewById(R.id.ppdb_username);
        password = (EditText) findViewById(R.id.ppdb_password);
        nisn = (EditText) findViewById(R.id.ppdb_nisn);
        tmplahir = (EditText) findViewById(R.id.ppdb_tempatlahir);
        nama = (EditText) findViewById(R.id.ppdb_namaLengkap);
        //thnmasuk = (EditText) findViewById(R.id.ppdb_thnMasuk);
        simpan = (Button) findViewById(R.id.ppdbSimpan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }

        });
        tgllahir = (TextView) findViewById(R.id.ppdb_tanggallahir);
        edtlahir = (EditText) findViewById(R.id.ppdb_tanggallahir);
        edtlahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();

            }
        });
    }



    private void simpan()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);


                    int code = Integer.parseInt(dataObj.getString("code"));
                    if (code == 1)
                    {
                        daftarBerhasil();
                    }else if(code == 0)
                    {
                        daftarGAgal();
                    }

                        Toast.makeText(FormulirPPDB.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                       // adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(FormulirPPDB.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError{

                    Map<String,String> map = new HashMap<String, String>();
//                    params.put("id_siswaBaru", id_siswaBaru);
                map.put("username", username.getText().toString());
                map.put("password", password.getText().toString());
                map.put("tempat_lahir", tmplahir.getText().toString());
                map.put("tanggal_lahir", tgllahir.getText().toString());
                map.put("nama", nama.getText().toString());
                map.put("tahun_masuk", thnajaran.getText().toString());
                map.put("id_jenis_kelamin", String.valueOf(ppdbjk.getSelectedItem()));
                map.put("kode_jurusan", String.valueOf(jurusan.getSelectedItem()));
                map.put("kode_kelas", "X " + String.valueOf(jurusan.getSelectedItem()));
                map.put("kelas_awal", "X " + String.valueOf(jurusan.getSelectedItem()));
                map.put("nisn", nisn.getText().toString());
                  //  params.put("tahun_nmasuk", thnmasuk.getText().toString());
                //}
                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void daftarBerhasil()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setMessage("Pendaftaran Berhasil")
                .setCancelable(false)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent login = new Intent(FormulirPPDB.this, Login.class);
                        finish();
                        startActivity(login);

                    }
                });

        AlertDialog berhasil = alert.create();
        berhasil.show();
    }
    public void daftarGAgal()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setMessage("Pendaftaran Gagal")
                .setCancelable(false)
                .setNegativeButton("Ulangi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       recreate();
                    }
                });

        AlertDialog berhasil = alert.create();
        berhasil.show();
    }

    private void showDateDialog()
    {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,month,dayOfMonth);
                tgllahir.setText(dateFormat.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed()
    {
            Intent data = new Intent(FormulirPPDB.this, Login.class);
            startActivity(data);


    }
}
