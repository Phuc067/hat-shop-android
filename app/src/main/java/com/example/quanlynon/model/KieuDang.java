package com.example.quanlynon.model;

import java.io.Serializable;

public class KieuDang implements Serializable {

    private String maKieuDang;

    private String tenKieuDang;

    public KieuDang() {
    }

    public KieuDang(String maKieuDang, String tenKieuDang) {
        this.maKieuDang = maKieuDang;
        this.tenKieuDang = tenKieuDang;
    }

    public String getMaKieuDang() {
        return maKieuDang;
    }

    public void setMaKieuDang(String maKieuDang) {
        this.maKieuDang = maKieuDang;
    }

    public String getTenKieuDang() {
        return tenKieuDang;
    }

    public void setTenKieuDang(String tenKieuDang) {
        this.tenKieuDang = tenKieuDang;
    }
}
