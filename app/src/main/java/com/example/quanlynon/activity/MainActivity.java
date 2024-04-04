package com.example.quanlynon.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quanlynon.R;
import com.example.quanlynon.adapter.SanPhamAdapter;
import com.example.quanlynon.api.SanPhamApi;
import com.example.quanlynon.constant.RequestCode;
import com.example.quanlynon.model.SanPham;
import com.example.quanlynon.retrofit.RetrofitService;
import com.example.quanlynon.service.FirebaseService;
import com.example.quanlynon.util.ListUtil;

import java.util.List;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerViewManHinhChinh;

    SanPhamAdapter sanPhamAdapter;

    List<SanPham> sanPhamList;

    Button btnThem;

    SwipeRefreshLayout swipeToRefresh;

    int preActionPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();
    }

    private void setEvent() {
        loadData();
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SanPhamActivity.class);
                startActivityForResult(intent, RequestCode.ADD_PRODUCT);
                LinearLayoutManager llayoutManager = (LinearLayoutManager) recyclerViewManHinhChinh.getLayoutManager();
                preActionPosition = llayoutManager.findLastVisibleItemPosition();
            }
        });

        swipeToRefresh.setOnRefreshListener(() -> {
            loadData();
            swipeToRefresh.setRefreshing(false);

        });
    }

    private void loadData() {
        sanPhamAdapter = new SanPhamAdapter(MainActivity.this);
        RetrofitService retrofitService = new RetrofitService();
        SanPhamApi sanPhamApi = retrofitService.getRetrofit().create(SanPhamApi.class);
        sanPhamApi.getAllSanPham().enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                Toast.makeText(MainActivity.this, "Call api success", Toast.LENGTH_SHORT).show();
                sanPhamList =  response.body();
                sanPhamAdapter.setSanPhams(sanPhamList);
                recyclerViewManHinhChinh.setLayoutManager(layoutManager);
                recyclerViewManHinhChinh.setHasFixedSize(true);
                recyclerViewManHinhChinh.setAdapter(sanPhamAdapter);
            }
            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable throwable) {
                Toast.makeText(MainActivity.this,"Call api error " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("error", throwable.getMessage());
            }
        });
        sanPhamAdapter.setOnItemClickListener(new SanPhamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                preActionPosition = position;
                SanPham clickedItem = sanPhamList.get(position);
                Intent intent = new Intent(MainActivity.this, SanPhamActivity.class);
                intent.putExtra("selectedProduct", clickedItem);
                startActivityForResult(intent, RequestCode.EDIT_PRODUCT);
            }
        });

        sanPhamAdapter.setOnItemLongClickListener(new SanPhamAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SanPham sanPham = sanPhamList.get(position);
                        if(sanPham.getHinhAnh()!= null)
                        {
                            FirebaseService.deleteImageFromFirebase(MainActivity.this, sanPham.getHinhAnh());
                        }
                        sanPhamApi.delete(sanPham.getMaSanPham()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    sanPhamList.remove(position);
                                    Toast.makeText(MainActivity.this,"Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                    sanPhamAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(MainActivity.this,"Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable throwable) {
                                Toast.makeText(MainActivity.this,"Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Không", null);
                builder.show();
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.ADD_PRODUCT && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("newProduct")) {
                SanPham newProduct = (SanPham) data.getSerializableExtra("newProduct");
                sanPhamList.add(0, newProduct);
                Toast.makeText(MainActivity.this, newProduct.getTenSanPham(), Toast.LENGTH_SHORT).show();
                sanPhamAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == RequestCode.EDIT_PRODUCT && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("editedProduct")) {
                SanPham editedProduct = (SanPham) data.getSerializableExtra("editedProduct");
                int index = ListUtil.getPositionInList(editedProduct, sanPhamList);
                sanPhamList.set(index, editedProduct);
                sanPhamAdapter.notifyDataSetChanged();
            }
        }
        recyclerViewManHinhChinh.scrollToPosition(preActionPosition-1);
    }
    private void setControl() {
            layoutManager = new GridLayoutManager(MainActivity.this, 2);
            recyclerViewManHinhChinh = findViewById(R.id.recycleViewManHinhChinh);
            btnThem = findViewById(R.id.btnThem);
            swipeToRefresh = findViewById(R.id.swipeToRefresh);
    }
}