package com.kelompok2.crudgaji;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.util.Log;
import android.widget.Toast;

import com.kelompok2.Conf.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityGetPegawai extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter adapter;
    private List<Pegawai> pgwList;
    ApiInterface apiInterface;
    Adapter.RecyclerViewClickListener listener;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pegawai);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        progressBar = findViewById(R.id.progres);
        recyclerView = findViewById(R.id.recycleGet);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        listener = new Adapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent intent = new Intent(ActivityGetPegawai.this, ActivityEditor.class);
                intent.putExtra("id", pgwList.get(position).getId());
                intent.putExtra("nama", pgwList.get(position).getName());
                intent.putExtra("posisi", pgwList.get(position).getDesg());
                intent.putExtra("gaji", pgwList.get(position).getSal());
                intent.putExtra("email", pgwList.get(position).getEmail());
                intent.putExtra("nphone", pgwList.get(position).getNphone());
                intent.putExtra("alamat", pgwList.get(position).getStreet());
                intent.putExtra("gender", pgwList.get(position).getGen());
                intent.putExtra("gambar", pgwList.get(position).getImg());
                startActivity(intent);
                overridePendingTransition(
                        R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left
                );
            }
        };

        final SwipeRefreshLayout doRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe);
        doRefresh.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
        );

        doRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                oItemRefresh();
            }

            public void oItemRefresh(){
                getPegawai();
                onItemLoad();
            }

            public void onItemLoad(){
                doRefresh.setRefreshing(false);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.menuSearch);

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName())
        );

        searchView.setQueryHint("Search Karyawan...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);
        return true;
    }

    public void getPegawai(){

        Call<List<Pegawai>>call = apiInterface.getPegawai();
        call.enqueue(new Callback<List<Pegawai>>() {
            @Override
            public void onResponse(Call<List<Pegawai>> call, Response<List<Pegawai>> response) {
                progressBar.setVisibility(View.GONE);
                pgwList = response.body();
                Log.i(ActivityGetPegawai.class.getSimpleName(), response.body().toString());
                adapter = new Adapter(pgwList, ActivityGetPegawai.this, listener);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Pegawai>> call, Throwable t) {
                Toast.makeText(
                        ActivityGetPegawai.this, "connection: " +
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        getPegawai();
    }

    private int count = 0;

    @Override
    public void onBackPressed(){
        count++;
        if (count >= 1) {
            Intent intent = new Intent(ActivityGetPegawai.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(
                    R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left
            );
            finishAffinity();
        } else {
            Toast.makeText(
                    this, "Tekan sekali lagi untuk keluar!", Toast.LENGTH_SHORT
            ).show();
            Handler handler = new Handler();
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            count = 0;
                        }
                    }, 2000
            );
        }

        super.onBackPressed();
    }
}
