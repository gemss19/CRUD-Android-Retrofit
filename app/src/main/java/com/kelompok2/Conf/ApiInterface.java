package com.kelompok2.Conf;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("get_pgw.php")
    Call<List<Pegawai>> getPegawai();

    @FormUrlEncoded
    @POST("add_pgw.php")
    Call<Pegawai> insertPgw(
            @Field("key") String key,
            @Field("nama") String name,
            @Field("posisi") String desg,
            @Field("gaji") String sal,
            @Field("email") String email,
            @Field("nphone") String phone,
            @Field("alamat") String street,
            @Field("gender") int gen,
            @Field("gambar") String img
    );

    @FormUrlEncoded
    @POST("update_pgw.php")
    Call<Pegawai> updatePgw(
            @Field("key") String key,
            @Field("id") int id,
            @Field("nama") String name,
            @Field("posisi") String desg,
            @Field("gaji") String sal,
            @Field("email") String email,
            @Field("nphone") String phone,
            @Field("alamat") String street,
            @Field("gender") int gen,
            @Field("gambar") String img
    );

    @FormUrlEncoded
    @POST("delete_pgw.php")
    Call<Pegawai> deletePgw(
            @Field("key") String key,
            @Field("id") int id,
            @Field("gambar") String img
    );
}
