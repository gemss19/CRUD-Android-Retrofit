package com.kelompok2.Conf;

import com.google.gson.annotations.SerializedName;

public class Pegawai {

    @SerializedName("id")
    private int id;

    @SerializedName("nama")
    private String name;

    @SerializedName("posisi")
    private String desg;

    @SerializedName("gaji")
    private String sal;

    @SerializedName("email")
    private String email;

    @SerializedName("nphone")
    private String nphone;

    @SerializedName("alamat")
    private String street;

    @SerializedName("gender")
    private int gen;

    @SerializedName("gambar")
    private String img;

    @SerializedName("value")
    private String val;

    @SerializedName("message")
    private String msg;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesg() {
        return desg;
    }

    public void setDesg(String desg) {
        this.desg = desg;
    }

    public String getSal() {
        return sal;
    }

    public void setSal(String sal) {
        this.sal = sal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNphone() {
        return nphone;
    }

    public void setNphone(String nphone) {
        this.nphone = nphone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getGen() {
        return gen;
    }

    public void setGen(int gen) {
        this.gen = gen;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
