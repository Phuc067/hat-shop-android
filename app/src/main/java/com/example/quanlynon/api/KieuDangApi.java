package com.example.quanlynon.api;

import com.example.quanlynon.model.KieuDang;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface KieuDangApi {

    @GET("designs")
    Call<List<KieuDang>> getAllKieuDang();
}
