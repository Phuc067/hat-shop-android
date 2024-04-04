package com.example.quanlynon.model;

import java.io.Serializable;

public class SanPham implements Serializable {
    private String maSanPham;

    private String tenSanPham;
    private int soLuong;
    private String gioiTinh;
    private String mauSac;
    private String hinhAnh;
    private Boolean trangThai;
    private KieuDang kieuDang;
    private Double gia;

    public SanPham() {
    }

    public SanPham(String maSanPham, String tenSanPham, int soLuong, String gioiTinh, String mauSac, String hinhAnh, Boolean trangThai, KieuDang kieuDang, Double gia) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.gioiTinh = gioiTinh;
        this.mauSac = mauSac;
        this.hinhAnh = hinhAnh;
        this.trangThai = trangThai;
        this.kieuDang = kieuDang;
        this.gia = gia;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public KieuDang getKieuDang() {
        return kieuDang;
    }

    public void setKieuDang(KieuDang kieuDang) {
        this.kieuDang = kieuDang;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public Double getGia() {
        return gia;
    }

    public void setGia(Double gia) {
        this.gia = gia;
    }
}
