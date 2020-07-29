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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;


public class Profile extends AppCompatActivity {

    TextView namaUser, ttlUser, kodeKelas, jurusan;
    String idu,id,levelU,JWT,Token_jwt,strFoto;
    Boolean session = false;
    SharedPreferences sharedpreferences;
    public final String deluser = Server.URL+"delperid.php";
    public final static String TAG = "Profile";
    public static final String TAG_ID = "id";
    public final static String TAG_IDU = "idu";
    public static final String TAG_LEVEL = "level";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_FOTO = "tag_foto";
    public final static String TOKEN = "?token=";
    public final static String Param = "&foto=";

    public  static final int RequestPermissionCode  = 1 ;
    PagerAdapterData pagerAdapter;
    Button btneditdata,btnrefresh,btngantifoto;
    ImageView fotoProfile;
    final String ambilfoto = Server.URL+"siswa/detail/foto/";
    final String gantifoto = Server.URL+"siswa/detail/ganti_foto/";

    private String Document_img1="";
    Bitmap bitmap;
    ProgressDialog progressDialog;
    int PICK_IMAGE_REQUEST = 111;

    static final int REQUEST_TAKE_PHOTO = 1;
    File mPhotoFile;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    int extraId;

    @Override
    public void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        setContentView(R.layout.profile);
        ambilfoto();
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        idu = sharedpreferences.getString(TAG_ID, null);
        levelU = sharedpreferences.getString(TAG_LEVEL, null);
        Token_jwt = sharedpreferences.getString(JWT, null);
        strFoto = sharedpreferences.getString(TAG_FOTO, null);


        fotoProfile = (ImageView) findViewById(R.id.fotoProfile);
        fotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihgambar();
            }
        });
        btngantifoto = (Button) findViewById(R.id.btngantifoto);
        btngantifoto.setEnabled(false);
        btngantifoto.setVisibility(View.GONE);
        btngantifoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gantifoto();
            }
        });
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
        EnableRuntimePermissionToAccessCamera();

        Picasso.with(this).load("http://muslikh.my.id/default.png").into(fotoProfile);


        btneditdata = (Button) findViewById(R.id.btneditData);
        btneditdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session) {
                    Intent profil = new Intent(Profile.this, EditDataSiswa.class);
                    profil.putExtra(TAG_IDU, idu);
                    startActivity(profil);
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Profil");

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        //Memanggil dan Memasukan Value pada Class PagerAdapterData(FragmentManager dan JumlahTab)
        PagerAdapterData pagerAdapter = new PagerAdapterData(getSupportFragmentManager(), tabLayout.getTabCount());

        //Memasang Adapter pada ViewPager
        viewPager.setAdapter(pagerAdapter);

        /*
         Menambahkan Listener yang akan dipanggil kapan pun halaman berubah atau
         bergulir secara bertahap, sehingga posisi tab tetap singkron
         */
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Callback Interface dipanggil saat status pilihan tab berubah.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Dipanggil ketika tab memasuki state/keadaan yang dipilih.
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Dipanggil saat tab keluar dari keadaan yang dipilih.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Dipanggil ketika tab yang sudah dipilih, dipilih lagi oleh user.
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {

        if(levelU.equals("admin"))
        {
            menu.findItem(R.id.logout).setEnabled(false).setVisible(false);
            menu.findItem(R.id.hapus).setEnabled(true).setVisible(true);
        }else if(levelU.equals("siswa"))
        {
            menu.findItem(R.id.logout).setEnabled(true).setVisible(true);
            menu.findItem(R.id.hapus).setEnabled(false).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            alertKeluar();
        }else if (id== R.id.hapus)
        {
            alertHapus();

        }

        return false;
    }
    public void alertKeluar()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setMessage("Tekan Ya Untuk Keluar")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, false);
                        editor.putString(TAG_ID, null);
                        editor.putString(TAG_USERNAME, null);
                        editor.commit();

                        Intent intent = new Intent(Profile.this, Login.class);
                        finish();
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog keluar = alert.create();
        keluar.show();

    }

    public void alertHapus()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setMessage("Yakin Hapus Data Siswa Ini ??")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, deluser, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject dataObj = new JSONObject(response);


                                    Toast.makeText(Profile.this, dataObj.getString("message"), Toast.LENGTH_LONG).show();
                                    // adapter.notifyDataSetChanged();

                                    if (dataObj.getString("message").equals("sukses"))
                                    {
                                        Intent kembali = new Intent(Profile.this,DataSiswa.class);
                                        startActivity(kembali);
                                        finish();
                                    }else if(dataObj.getString("message").equals("gagal"))
                                    {
                                        recreate();
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
                                Toast.makeText(Profile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                            @Override

                            protected Map<String,String> getParams() throws AuthFailureError {

                                idu = getIntent().getStringExtra(TAG_IDU);
                                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                                Map<String,String> map = new HashMap<String, String>();
                                map.put("id", idu);
                                return map;
                            }

                        };

                        AppController.getInstance().addToRequestQueue(stringRequest);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog keluar = alert.create();
        keluar.show();

    }


    @Override
    public void onBackPressed()
    {
        if(levelU.equals("admin")) {
            Intent pindah = new Intent(Profile.this, DataSiswa.class);
            startActivity(pindah);
        } else
        {
            Intent pindah = new Intent(Profile.this, MainActivity.class);
            startActivity(pindah);
        }
    }
    private void ambilfoto()
    {
        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setMessage("Proses Pengambilan Data, Mohon Tunggu...");
        progressDialog.show();

        extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDU));
        String Token = getIntent().getStringExtra(JWT);

        StringRequest request = new StringRequest(Request.Method.GET, ambilfoto+extraId+TOKEN+Token, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONArray dataArray= new JSONArray(response);

                    for (int i =0; i<dataArray.length(); i++) {

                        JSONObject obj = dataArray.getJSONObject(i);

//                        int id = obj.getInt("id");
//                        if (extraId == id) {
                            String fotobase64 = strFoto;
                            byte[] decodedString = Base64.decode(fotobase64, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                            if (extraId== id ) {
//
                            if (fotobase64.isEmpty()) {
                                    Glide.with(getApplication())
                                            .load("http://muslikh.my.id/default.png")
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(fotoProfile);
//                                    Picasso.with(getApplication()).load("http://muslikh.my.id/default.png").into(fotoProfile);
                                } else if (fotobase64.equals("null")) {

                                    Glide.with(getApplication())
                                            .load("http://muslikh.my.id/default.png")
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(fotoProfile);
//                                    Picasso.with(getApplication()).load("http://muslikh.my.id/default.png").into(fotoProfile);
                                } else {

                                    Glide.with(getApplication())
                                            .load(decodedByte)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(fotoProfile);
//                                    fotoProfile.setImageBitmap(decodedByte);
                                }
//                            }
//                        }
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
                Toast.makeText(Profile.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(Profile.this);
        rQueue.add(request);
    }


    private void pilihgambar() {
        final CharSequence[] options = { "Ambil Foto", "Pilih Dari Gallery","Batal" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Profile.this);
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
                            Uri photoURI = FileProvider.getUriForFile(Profile.this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    photoFile);
                            mPhotoFile = photoFile;
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                            btngantifoto.setEnabled(true);
                            btngantifoto.setVisibility(View.VISIBLE);
                        }
                    }

                }
                else if (options[item].equals("Pilih Dari Gallery"))
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

                    btngantifoto.setEnabled(true);
                    btngantifoto.setVisibility(View.VISIBLE);
                }
                else if (options[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }
    private void gantifoto()
    {
        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setMessage("Proses Simpan, Mohon Tunggu...");
        progressDialog.show();


        extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDU));
        String Token = getIntent().getStringExtra(JWT);
        //converting image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, gantifoto+extraId+TOKEN+Token+Param, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);
                    progressDialog.dismiss();


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(Profile.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();

                map.put("id", getIntent().getStringExtra(TAG_IDU));
                map.put("foto",imageString);

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {

                bitmap =  BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
                Glide.with(Profile.this)
                        .load(bitmap)
                        .apply(RequestOptions.circleCropTransform())
                        .into(fotoProfile);
            }
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Glide.with(Profile.this)
                        .load(bitmap)
                        .apply(RequestOptions.circleCropTransform())
                        .into(fotoProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // Requesting runtime permission to access camera.
    public void EnableRuntimePermissionToAccessCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(Profile.this,
                Manifest.permission.CAMERA))
        {

            // Printing toast message after enabling runtime permission.
            Toast.makeText(Profile.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Profile.this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

}
