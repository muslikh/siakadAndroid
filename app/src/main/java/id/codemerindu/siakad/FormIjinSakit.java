package id.codemerindu.siakad;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;
import static java.security.AccessController.getContext;

public class FormIjinSakit extends AppCompatActivity {


    String url_absen = Server.URL + "siswa/absen";
    String TAG_ID = "id";
    String TAG_Jenis = "jenis";
    String kenapatdkmasuk;
    Bitmap bitmap;
    public final static String TAG_KELAS = "kode_kelas";
    public final static String TAG_SEMESTER = "semester";
    String id, jenis, semester, kelas;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    RadioGroup kenapa;
    RadioButton radioSakit, radioIjin;
    EditText keterangan;
    ImageView fotosurat;
    Button btnKirim,pilihfoto;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    int PICK_IMAGE_REQUEST = 111;
    File mPhotoFile;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formijinsakit);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        kelas = sharedpreferences.getString(TAG_KELAS, null);
        semester = sharedpreferences.getString(TAG_SEMESTER, null);

//        kelas = getIntent().getStringExtra(TAG_KELAS);
//        semester = getIntent().getStringExtra(TAG_SEMESTER);

        btnKirim = findViewById(R.id.kirimIjin);
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProsesAbsen();
            }
        });
        pilihfoto = findViewById(R.id.pilihfoto);
        pilihfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihgambar();
            }
        });

        fotosurat = findViewById(R.id.fotosurat);


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

        checkPermission(Manifest.permission.CAMERA,
                CAMERA_PERMISSION_CODE);
        checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE);

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

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

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

                String jam = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String tgl = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//                String dateFormat = DateFormat.getInstance().format(tgl);
                Map<String, String> map = new HashMap<String, String>();
                map.put("siswaID", String.valueOf(id));
                map.put("tgl", tgl);
                map.put("jam", jam);
                map.put("jenis", "tidak masuk");
                map.put("kode_kelas", kelas);
                map.put("semester", semester);
                map.put("status", kenapatdkmasuk);
                map.put("foto", imageString);
                map.put("keterangan", keterangan.getText().toString());
                map.put("ket_masuk", "tepat waktu");
                map.put("ket_pulang", "tepat waktu");
                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    private void pilihgambar() {
        final CharSequence[] options = { "Ambil Foto", "Pilih Dari Gallery","Batal" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(FormIjinSakit.this);
        builder.setTitle("Ganti Foto!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Ambil Foto"))
                {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            // Error occurred while creating the File
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(FormIjinSakit.this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    photoFile);
                            mPhotoFile = photoFile;
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
;
                        }
                    }

                }
                else if (options[item].equals("Pilih Dari Gallery"))
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);


                }
                else if (options[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {

                bitmap =  BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
                Glide.with(FormIjinSakit.this)
                        .load(bitmap)
                        .apply(RequestOptions.circleCropTransform())
                        .into(fotosurat);
            }
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Glide.with(FormIjinSakit.this)
                        .load(bitmap)
                        .apply(RequestOptions.circleCropTransform())
                        .into(fotosurat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @android.support.annotation.NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(FormIjinSakit.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(FormIjinSakit.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(FormIjinSakit.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(FormIjinSakit.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(FormIjinSakit.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(FormIjinSakit.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(FormIjinSakit.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onBackPressed()
    {

        Intent pindah = new Intent(FormIjinSakit.this, kehadiran.class);

        startActivity(pindah);
//        }
    }

}