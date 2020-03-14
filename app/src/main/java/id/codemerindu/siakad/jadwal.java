package id.codemerindu.siakad;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class jadwal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarJadwal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Jadwal");
        final TabLayout tabLayoutJadwal = (TabLayout) findViewById(R.id.tab_layout_jadwal);
        final ViewPager viewPagerJadwal = (ViewPager) findViewById(R.id.view_pager_jadwal);

        //Memanggil dan Memasukan Value pada Class PagerAdapterData(FragmentManager dan JumlahTab)
        PagerAdapterJadwal pager = new PagerAdapterJadwal(getSupportFragmentManager(), tabLayoutJadwal.getTabCount());

        //Memasang Adapter pada ViewPager
        viewPagerJadwal.setAdapter(pager);

        /*
         Menambahkan Listener yang akan dipanggil kapan pun halaman berubah atau
         bergulir secara bertahap, sehingga posisi tab tetap singkron
         */
        viewPagerJadwal.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutJadwal));

        //Callback Interface dipanggil saat status pilihan tab berubah.
        tabLayoutJadwal.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Dipanggil ketika tab memasuki state/keadaan yang dipilih.
                viewPagerJadwal.setCurrentItem(tab.getPosition());
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
}
