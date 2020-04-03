package id.codemerindu.siakad;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AbsenQR extends AppCompatActivity {
    private ImageView bgContent;
    private CodeScanner codeScanner;
    private CodeScannerView codeScannerView;
    String url_absen = Server.URL+"siswa.php?aksi=absen";
    String TAG_IDU = "idu";
    String TAG_Jenis = "jenis";
    String idu,jenis,keterangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_qr);


        bgContent = findViewById(R.id.BgContent);
        codeScannerView = findViewById(R.id.scannerView);

        codeScanner = new CodeScanner(this,codeScannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String succes = result.getText();
                        if (succes.equals("http://smknprigen.sch.id"))
                        {
                            ProsesAbsen();
//                            pesan(idu);
                        }else{

                            pesan(";p");
                        }

                    }
                });
            }
        });

        chekCamera();


    }
    @Override
    protected void onResume()
    {
        super.onResume();
        chekCamera();
    }
    @Override
    protected void onPause()
    {
        codeScanner.releaseResources();
        super.onPause();
    }


    public void chekCamera()
    {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        codeScanner.startPreview();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }


    public void pesan(final String isiPesan)
    {  android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
        alert
                .setMessage(isiPesan)
                .setCancelable(false)
                .setNegativeButton("Okke", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent backD = new Intent(AbsenQR.this,kehadiran.class);
                        startActivity(backD);
                    }
                });

        android.app.AlertDialog kodesalah = alert.create();
        kodesalah.show();
    }

    public void ProsesAbsen()
    {
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
                Log.e("Absen", "Error: " + error.getMessage());
                pesan(error.getMessage());

            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError {

                idu = getIntent().getStringExtra(TAG_IDU);
                jenis = getIntent().getStringExtra("jenis");

                Date waktu = new Date();
                String dateFormat = DateFormat.getInstance().format(waktu);
                Map<String,String> map = new HashMap<String, String>();
                map.put("id_siswa", String.valueOf(idu));
                map.put("waktu", dateFormat);
                map.put("jenis", jenis);
                map.put("keterangan", "hadir");
                return map;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
