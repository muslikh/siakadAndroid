package id.codemerindu.siakad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static id.codemerindu.siakad.Login.my_shared_preferences;
import static id.codemerindu.siakad.Login.session_status;


public class Profile extends AppCompatActivity implements View.OnClickListener {

    TextView namaUser, ttlUser, kodeKelas, jurusan;
    String idu,levelU;
    SharedPreferences sharedpreferences;
    public final String deluser = Server.URL+"delperid.php";
    public final static String TAG = "Profile";
    public static final String TAG_ID = "id";
    public final static String TAG_IDU = "idu";
    public static final String TAG_LEVEL = "level";
    public static final String TAG_USERNAME = "username";
    PagerAdapter pagerAdapter;
    Button btneditdata,btnrefresh,bntgantifoto;
    ImageView fotoProfile;
    Boolean session = false;
    final String gantifoto = Server.URL+"gantifotosiswa.php";

    private String Document_img1="";

    private RequestQueue requestQueue;
    private StringRequest stringRequest;


    @Override
    public void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        setContentView(R.layout.profile);


        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        levelU = sharedpreferences.getString(TAG_LEVEL, null);
        idu = sharedpreferences.getString(TAG_IDU, null);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);

        bntgantifoto = (Button)  findViewById(R.id.btngantifoto);
        bntgantifoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gantifoto();
            }
        });
        btnrefresh = (Button) findViewById(R.id.btnrefreshData);

        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        fotoProfile = (ImageView) findViewById(R.id.fotoProfile);
        fotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihgambar();
            }
        });


        Picasso.with(this).load("http://smknprigen.sch.id/bkk/image/default.png").into(fotoProfile);


        btneditdata = (Button) findViewById(R.id.btneditData);
        btneditdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idu = getIntent().getStringExtra(TAG_IDU);
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                session = sharedpreferences.getBoolean(session_status, false);
                if (session) {
                    Intent profil = new Intent(Profile.this, EditDataSiswa.class);
                    profil.putExtra(TAG_IDU, idu);
                    startActivity(profil);
                }
            }
        });

        getSupportActionBar().setTitle("Detail Profil");

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        //Memanggil dan Memasukan Value pada Class PagerAdapter(FragmentManager dan JumlahTab)
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

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


    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {

        if(levelU.equals("admin"))
        {


//                Intent pindah = new Intent(Profile.this,AdminActivity.class);
//                startActivity(pindah);

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
                                map.put("id_siswa", idu);
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
    private void gantifoto()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, gantifoto, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);


                    // adapter.notifyDataSetChanged();



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
//
                map.put("id_siswa", idu);
                map.put("foto", Document_img1);
//                map.put("tempat_lahir", Edtmplahir.getText().toString());

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void pilihgambar() {
        final CharSequence[] options = { "Ambil Foto", "Pilih Dari Gallery","Batal" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Profile.this);
        builder.setTitle("Ganti Foto!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
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
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap=getResizedBitmap(bitmap, 400);
                    fotoProfile.setImageBitmap(bitmap);
                    BitMapToString(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail=getResizedBitmap(thumbnail, 400);
                Log.w("path", picturePath+"");
                fotoProfile.setImageBitmap(thumbnail);
                BitMapToString(thumbnail);
            }
        }
    }
    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void onClick(View v) {
        if (Document_img1.equals("") || Document_img1.equals(null)) {
            ContextThemeWrapper ctw = new ContextThemeWrapper( Profile.this, R.style.AppTheme);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
            alertDialogBuilder.setTitle("Id Prof Can't Empty ");
            alertDialogBuilder.setMessage("Id Prof Can't empty please select any one document");
            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            alertDialogBuilder.show();
            return;
        }
        else{

                gantifoto();

        }
    }

}
