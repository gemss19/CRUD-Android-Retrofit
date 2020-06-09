package com.kelompok2.crudgaji;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kelompok2.Conf.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditor extends AppCompatActivity {

    //Inisialisasi Variable
    private Spinner mGenderSpinner;
    private EditText mName, mPosisi, mGaji, mEmail, mPhone, mStreet;
    private CircleImageView mImage;
    private FloatingActionButton mFabChoosePic;

    private int mGender = 0;
    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    private String name, desg, sal, email, phone, street, img;
    private int id, gender;
    private Handler mHandler = new Handler();

    private Menu action;
    private Bitmap bitmap;

    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }

        mName = findViewById(R.id.eName);
        mPosisi = findViewById(R.id.ePosisi);
        mGaji = findViewById(R.id.eGaji);
        mEmail = findViewById(R.id.eEmail);
        mPhone = findViewById(R.id.ePhone);
        mStreet = findViewById(R.id.eAlamat);
        mGenderSpinner = findViewById(R.id.gender);
        mImage = findViewById(R.id.img);
        mFabChoosePic = findViewById(R.id.fabChoisePic);

        mFabChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        setupSpinner();
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        name = intent.getStringExtra("nama");
        desg = intent.getStringExtra("posisi");
        sal = intent.getStringExtra("gaji");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("nphone");
        street = intent.getStringExtra("alamat");
        gender = intent.getIntExtra("gender", 0);
        img = intent.getStringExtra("gambar");

        setDataFormIntentExtra();
    }

    private void setDataFormIntentExtra() {
        if (id != 0) {
            readMode();
            getSupportActionBar().setTitle("Edit " + name.toString());
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            mName.setText(name);
            mPosisi.setText(desg);
            mGaji.setText(sal);
            mEmail.setText(email);
            mPhone.setText(phone);
            mStreet.setText(street);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.skipMemoryCache(true);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.placeholder(R.drawable.ic_user);
            requestOptions.error(R.drawable.ic_user);

            Glide.with(ActivityEditor.this)
                    .load(img)
                    .apply(requestOptions)
                    .override(245, 245)
                    .into(mImage);

            switch (gender) {
                case GENDER_MALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case GENDER_FEMALE:
                    mGenderSpinner.setSelection(2);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }
        } else {
            getSupportActionBar().setTitle("Tambah Karyawan");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender_options, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if(!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))){
                        mGender = GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))){
                        mGender = GENDER_FEMALE;
                    } else {
                        mGender = GENDER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                mGender = 0;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        action = menu;
        action.findItem(R.id.menuSave).setVisible(false);

        if (id == 0){
            action.findItem(R.id.menuEdit).setVisible(false);
            action.findItem(R.id.menuDelete).setVisible(false);
            action.findItem(R.id.menuSave).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menuEdit:
                editMode();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mName, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menuEdit).setVisible(false);
                action.findItem(R.id.menuDelete).setVisible(false);
                action.findItem(R.id.menuSave).setVisible(true);
                return true;
            case R.id.menuSave:

                if (id == 0) {
                    if (TextUtils.isEmpty(mName.getText().toString())||
                            TextUtils.isEmpty(mPosisi.getText().toString())||
                            TextUtils.isEmpty(mGaji.getText().toString())||
                            TextUtils.isEmpty(mEmail.getText().toString())||
                            TextUtils.isEmpty(mPhone.getText().toString())||
                            TextUtils.isEmpty(mStreet.getText().toString())) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setMessage("Please Complete the field!");
                        alertDialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    } else {
                        postData("insert");

                        action.findItem(R.id.menuEdit).setVisible(true);
                        action.findItem(R.id.menuDelete).setVisible(false);
                        action.findItem(R.id.menuSave).setVisible(true);

                        readMode();
                    }
                } else {
                    updateData("update", id);

                    action.findItem(R.id.menuEdit).setVisible(false);
                    action.findItem(R.id.menuDelete).setVisible(false);
                    action.findItem(R.id.menuSave).setVisible(true);

                    readMode();
                    mHandler.postDelayed(mEditTimeTask, 2000);
                }
                return true;
            case R.id.menuDelete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage("Yakin inginmenghapus Pegawai?");
                alertDialog.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                deleteData("delete", id, img);
                            }
                        }
                );
                alertDialog.setNegativeButton("Tidak",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.show();
                mHandler.postDelayed(mEditTimeTask, 2000);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodeImg = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return  encodeImg;
    }

    //method untuk memilih gambar
    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                mImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Method untuk menambah karyawan
    private void postData(final String key) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan...");
        progressDialog.show();

        readMode();

        String name = mName.getText().toString().trim();
        String desg = mPosisi.getText().toString().trim();
        String sal = mGaji.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();
        String street = mStreet.getText().toString().trim();
        int gender = mGender;
        String img = null;
        if (bitmap == null) {
            img = "";
        } else {
            img = getStringImage(bitmap);
        }

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Pegawai> call = apiInterface.insertPgw(key, name, desg, sal
        , email, phone, street, gender, img);

        call.enqueue(new Callback<Pegawai>() {
            @Override
            public void onResponse(Call<Pegawai> call, Response<Pegawai> response) {
                progressDialog.dismiss();

                Log.i(ActivityEditor.class.getSimpleName(), response.toString());

                String value = response.body().getVal();
                String msg = response.body().getMsg();

                if (value.equals("1")) {
                    finish();
                } else {
                    Toast.makeText(ActivityEditor.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pegawai> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ActivityEditor.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method untuk update karyawan
    private void updateData(final String key, final int id) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memperbarui...");
        progressDialog.show();

        readMode();

        String name = mName.getText().toString().trim();
        String desg = mPosisi.getText().toString().trim();
        String sal = mGaji.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();
        String street = mStreet.getText().toString().trim();
        int gender = mGender;
        String img = null;
        if (bitmap == null) {
            img = "";
        } else {
            img = getStringImage(bitmap);
        }

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Pegawai> call = apiInterface.updatePgw(key, id, name, desg, sal
                , email, phone, street, gender, img);

        call.enqueue(new Callback<Pegawai>() {
            @Override
            public void onResponse(Call<Pegawai> call, Response<Pegawai> response) {
                progressDialog.dismiss();

                Log.i(ActivityEditor.class.getSimpleName(), response.toString());

                String value = response.body().getVal();
                String msg = response.body().getMsg();

                if (value.equals("1")) {
                    Toast.makeText(ActivityEditor.this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityEditor.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pegawai> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ActivityEditor.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //method untuk menghapus karyawan
    private void deleteData(final String key, final int id, final String pic){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("menghapus...");
        progressDialog.show();

        readMode();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Pegawai> call = apiInterface.deletePgw(key, id, pic);

        call.enqueue(new Callback<Pegawai>() {
            @Override
            public void onResponse(Call<Pegawai> call, Response<Pegawai> response) {

                progressDialog.dismiss();

                Log.i(ActivityEditor.class.getSimpleName(), response.toString());

                String value = response.body().getVal();
                String msg = response.body().getMsg();

                if (value.equals("1")) {
                    Toast.makeText(ActivityEditor.this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityEditor.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pegawai> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ActivityEditor.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method untuk mengaktifkan mode baca tidak bisa edit
    @SuppressLint("RestrictedApi")
    void readMode(){

        mName.setFocusableInTouchMode(false);
        mPosisi.setFocusableInTouchMode(false);
        mGaji.setFocusableInTouchMode(false);
        mEmail.setFocusableInTouchMode(false);
        mPhone.setFocusableInTouchMode(false);
        mStreet.setFocusableInTouchMode(false);
        mGenderSpinner.setEnabled(false);
        mFabChoosePic.setVisibility(View.INVISIBLE);
    }

    //Method untuk mengaktifkan mode edit
    @SuppressLint("RestrictedApi")
    private void editMode(){

        mName.setFocusableInTouchMode(true);
        mPosisi.setFocusableInTouchMode(true);
        mGaji.setFocusableInTouchMode(true);
        mEmail.setFocusableInTouchMode(true);
        mPhone.setFocusableInTouchMode(true);
        mStreet.setFocusableInTouchMode(true);
        mGenderSpinner.setEnabled(true);
        mFabChoosePic.setVisibility(View.VISIBLE);
    }

    private Runnable mPostTimeTask = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(ActivityEditor.this, MainActivity.class));
        }
    };

    private Runnable mEditTimeTask = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(ActivityEditor.this, ActivityGetPegawai.class));
        }
    };

    private int count = 0;

    @Override
    public void onBackPressed(){
        count++;
        if (count >= 1) {
            Intent intent = new Intent(ActivityEditor.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(
                    R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right
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
