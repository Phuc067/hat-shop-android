package com.example.quanlynon.api;

import com.example.quanlynon.model.SanPham;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SanPhamApi {
    @GET("products")
    Call<List<SanPham>> getAllSanPham();

    @POST("products")
    Call<SanPham> insert(@Body SanPham sanPham);

    @PUT("products")
    Call<SanPham> update(@Body SanPham sanPham);

    @DELETE("products/{id}")
    Call<Void> delete(@Path("id") String id);
}
