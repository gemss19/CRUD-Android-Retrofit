package com.kelompok2.crudgaji;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    CardView mGet;
    CardView mAdd;
    CardView mAbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGet = (CardView) findViewById(R.id.get_pgw);
        mAdd = (CardView) findViewById(R.id.add_pgw);
        mAbt = (CardView) findViewById(R.id.about_app);

        mGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityGetPegawai.class);
                startActivity(intent);
                overridePendingTransition(
                        R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_right
                );
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityEditor.class);
                startActivity(intent);
                overridePendingTransition(
                        R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left
                );
            }
        });

        mAbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                overridePendingTransition(
                        R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_right
                );
            }
        });

    }

    private boolean doubleBack = false;

    @Override
    public void onBackPressed(){
        if (doubleBack){
            super.onBackPressed();
            finish();
        }

        this.doubleBack = true;
        Toast.makeText(
                this, "Tekan sekali lagi untuk keluar!", Toast.LENGTH_SHORT
        ).show();

        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        doubleBack = false;
                    }
                }, 2000
        );
    }
}
