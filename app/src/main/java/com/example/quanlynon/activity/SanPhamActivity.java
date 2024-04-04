package com.example.quanlynon.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quanlynon.R;
import com.example.quanlynon.adapter.KieuDangAdapter;
import com.example.quanlynon.adapter.SanPhamAdapter;
import com.example.quanlynon.api.KieuDangApi;
import com.example.quanlynon.api.SanPhamApi;
import com.example.quanlynon.constant.Constant;
import com.example.quanlynon.constant.RequestCode;
import com.example.quanlynon.model.KieuDang;
import com.example.quanlynon.model.SanPham;
import com.example.quanlynon.retrofit.RetrofitService;
import com.example.quanlynon.service.FirebaseService;
import com.example.quanlynon.util.ListUtil;
import com.example.quanlynon.validation.Validator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SanPhamActivity extends AppCompatActivity {

    TextView tvTitle;
    EditText edtMaSanPham, edtTenSanPham, edtSoLuong, edtMauSac, edtGia;

    Spinner spGioiTinh, spKieuDang;
    ImageView ivSanPham;

    ImageButton  btnRemoveImage;
    Button btnChonAnh, btnSubmit;
    ArrayAdapter<String> gioiTinhAdapter;

    List<KieuDang> kieuDangList;
    KieuDangAdapter kieuDangAdapter;
    Uri imageUri;
    String imageUrl;

    LinearLayout llChk;

    CheckBox chkTrangThai;

    StorageReference storageReference;
    ProgressDialog progressDialog;

    int gioiTinhPosition, kieuDangPosition;

    Boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san_pham);
        setControl();
        setEvent();
        if (isEdit == false) {
            tvTitle.setText("Thêm sản phẩm");
        } else {
            tvTitle.setText("Sửa sản phẩm");
            edtMaSanPham.setEnabled(false);
            llChk.setVisibility(View.VISIBLE);
        }
    }

    private void setEvent() {
        SanPham sanPham = (SanPham) getIntent().getSerializableExtra("selectedProduct");
        khoiTao(sanPham);
        if (sanPham != null) {
            hienThiThongTin(sanPham);
            isEdit = true;
        }
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonAnh();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEditTexts() == false) return;
                if (!isEdit) {
                    themSanPham();
                } else {

                    suaSanPham(sanPham);
                }
            }
        });
        spGioiTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                gioiTinhPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(SanPhamActivity.this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            }
        });

        btnRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(SanPhamActivity.this).clear(ivSanPham);
                imageUri = null;
                btnRemoveImage.setEnabled(false);
            }
        });

        spKieuDang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                kieuDangPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(SanPhamActivity.this, "Vui lòng chọn kiểu dáng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chonAnh() {
        Intent intent = new Intent();
        intent.setType("image/**");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RequestCode.CAMERA_REQUEST);
    }

    private void themSanPham() {

        SanPham sanPham = new SanPham();
        sanPham.setMaSanPham(edtMaSanPham.getText().toString().trim());
        sanPham.setTenSanPham(edtTenSanPham.getText().toString().trim());
        sanPham.setSoLuong(Integer.parseInt(edtSoLuong.getText().toString().trim()));
        sanPham.setGioiTinh(Constant.gioiTinhOptions.get(gioiTinhPosition));
        sanPham.setMauSac(edtMauSac.getText().toString().trim());
        sanPham.setTrangThai(true);
        sanPham.setKieuDang(kieuDangList.get(kieuDangPosition));
        sanPham.setGia(Double.parseDouble(edtGia.getText().toString()));

        if (imageUri != null) { // Kiểm tra xem ảnh có tồn tại không

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading file....");
            progressDialog.show();
            SimpleDateFormat formater = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
            Date now = new Date();
            String fileName = formater.format(now);
            storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Lấy đường dẫn của ảnh từ Firebase Storage
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                    Log.d("Image URL", imageUrl);
                                    sanPham.setHinhAnh(imageUrl);
                                    insert(sanPham);
                                    ivSanPham.setImageURI(null);
                                    Toast.makeText(SanPhamActivity.this, "Successfully uploaded image", Toast.LENGTH_SHORT).show();
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Toast.makeText(SanPhamActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            Log.e("error upload image:", e.getMessage());
                        }
                    });
        } else {
            sanPham.setHinhAnh(null);
            insert(sanPham);
        }
    }

    private void suaSanPham(SanPham sanPham) {
        sanPham.setMaSanPham(edtMaSanPham.getText().toString().trim());
        sanPham.setTenSanPham(edtTenSanPham.getText().toString().trim());
        sanPham.setSoLuong(Integer.parseInt(edtSoLuong.getText().toString().trim()));
        sanPham.setGioiTinh(Constant.gioiTinhOptions.get(gioiTinhPosition));
        sanPham.setMauSac(edtMauSac.getText().toString().trim());
        sanPham.setTrangThai(chkTrangThai.isChecked());
        sanPham.setKieuDang(kieuDangList.get(kieuDangPosition));
        sanPham.setGia(Double.parseDouble(edtGia.getText().toString()));
        //Nếu người dùng chọn ảnh mà ảnh đó khác với ảnh cũ của sản phẩm
        if ((imageUri != null && sanPham.getHinhAnh()==null) || (imageUri != null && !imageUri.equals(Uri.parse(sanPham.getHinhAnh())))) {
            if(sanPham.getHinhAnh()!=null)
            {
                FirebaseService.deleteImageFromFirebase( SanPhamActivity.this ,sanPham.getHinhAnh());
            }
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading file....");
            progressDialog.show();
            SimpleDateFormat formater = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
            Date now = new Date();
            String fileName = formater.format(now);
            storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Lấy đường dẫn của ảnh từ Firebase Storage
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                    Log.d("Image URL", imageUrl);
                                    sanPham.setHinhAnh(imageUrl);
                                    update(sanPham);
                                    ivSanPham.setImageURI(null);
                                    Toast.makeText(SanPhamActivity.this, "Successfully uploaded image", Toast.LENGTH_SHORT).show();
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Toast.makeText(SanPhamActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            Log.e("error upload image:", e.getMessage());
                        }
                    });
        }
        //nếu người dùng chọn ảnh mà ảnh đó trùng với ảnh cũ của sản phẩm hoặc không đổi ảnh
        else if((imageUri != null && imageUri.equals(Uri.parse(sanPham.getHinhAnh())))){
            update(sanPham);
        }
        else if(imageUri == null && sanPham.getHinhAnh() !=null)
        {
            FirebaseService.deleteImageFromFirebase(SanPhamActivity.this, sanPham.getHinhAnh());
            sanPham.setHinhAnh(null);
            update(sanPham);
        }
    }

    private void hienThiThongTin(SanPham sanPham) {
        edtMaSanPham.setText(sanPham.getMaSanPham());
        edtTenSanPham.setText(sanPham.getTenSanPham());
        edtSoLuong.setText(String.valueOf(sanPham.getSoLuong()));
        gioiTinhPosition = ListUtil.getPositionInList(sanPham.getGioiTinh(), Constant.gioiTinhOptions);
        if (gioiTinhPosition != -1) {
            spGioiTinh.setSelection(gioiTinhPosition);
        }
        edtMauSac.setText(sanPham.getMauSac());
        Glide.with(this)
                .load(sanPham.getHinhAnh())
                .into(ivSanPham);
        if(sanPham.getHinhAnh()!=null)
        {
            imageUri = Uri.parse(sanPham.getHinhAnh());
            btnRemoveImage.setEnabled(true);
        }

        edtGia.setText(String.valueOf(sanPham.getGia()));
        chkTrangThai.setChecked(sanPham.getTrangThai());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.CAMERA_REQUEST && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivSanPham.setImageURI(imageUri);
        }
    }


    private void khoiTao(SanPham sanPham) {
        //Thêm dữ liệu vào combobox giới tính
        gioiTinhAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Constant.gioiTinhOptions);
        gioiTinhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGioiTinh.setAdapter(gioiTinhAdapter);

        //Lấy api danh sách kiểu dáng
        RetrofitService retrofitService = new RetrofitService();
        KieuDangApi kieuDangApi = retrofitService.getRetrofit().create(KieuDangApi.class);
        kieuDangApi.getAllKieuDang().enqueue(new Callback<List<KieuDang>>() {
            @Override
            public void onResponse(Call<List<KieuDang>> call, Response<List<KieuDang>> response) {
                kieuDangList = response.body();
                kieuDangAdapter = new KieuDangAdapter(SanPhamActivity.this, kieuDangList);
                kieuDangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spKieuDang.setAdapter(kieuDangAdapter);
                if (sanPham != null) {
                    kieuDangPosition = ListUtil.getPositionInList(sanPham.getKieuDang(), kieuDangList);
                    if (kieuDangPosition != -1) spKieuDang.setSelection(kieuDangPosition);
                }
            }

            @Override
            public void onFailure(Call<List<KieuDang>> call, Throwable throwable) {
                Log.e("Error: ", throwable.getMessage());
            }
        });


    }

    private void setControl() {
        tvTitle = findViewById(R.id.tvTitle);
        edtMaSanPham = findViewById(R.id.edtMaSanPham);
        edtTenSanPham = findViewById(R.id.edtTenSanPham);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        edtMauSac = findViewById(R.id.edtMauSac);
        spGioiTinh = findViewById(R.id.spGioiTinh);
        edtGia = findViewById(R.id.edtGia);
        spKieuDang = findViewById(R.id.spKieuDang);
        ivSanPham = findViewById(R.id.ivSanPham);
        btnRemoveImage = findViewById(R.id.btnRemoveImage);
        btnChonAnh = findViewById(R.id.btnChonAnh);
        llChk = findViewById(R.id.llChk);
        chkTrangThai = findViewById(R.id.chkTrangThai);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private boolean validateEditTexts() {
        boolean isValid = true;

        if (TextUtils.isEmpty(edtMaSanPham.getText().toString().trim())) {
            edtMaSanPham.setError("Mã sản phẩm không được để trống");
            isValid = false;
        }

        if (TextUtils.isEmpty(edtTenSanPham.getText().toString().trim())) {
            edtTenSanPham.setError("Tên sản phẩm không được để trống");
            isValid = false;
        }

        if (TextUtils.isEmpty(edtSoLuong.getText().toString().trim())) {
            edtSoLuong.setError("Số lượng không được để trống");
            isValid = false;
        } else if (!Validator.isInteger(edtSoLuong.getText().toString().trim())) {
            edtSoLuong.setError("Số lượng không hợp lệ");
            isValid = false;
        }

        if (TextUtils.isEmpty(edtMauSac.getText().toString().trim())) {
            edtMauSac.setError("Màu sắc không được để trống");
            isValid = false;
        }

        if (TextUtils.isEmpty(edtGia.getText().toString().trim())) {
            edtGia.setError("Giá không được để trống");
            isValid = false;
        } else if (!Validator.isDouble(edtGia.getText().toString().trim())) {
            edtGia.setError("Giá không hợp lệ");
            isValid = false;
        }
        return isValid;
    }

    private void insert(SanPham sanPham)
    {
        RetrofitService retrofitService = new RetrofitService();
        SanPhamApi sanPhamApi = retrofitService.getRetrofit().create(SanPhamApi.class);
        sanPhamApi.insert(sanPham).enqueue(new Callback<SanPham>() {
            @Override
            public void onResponse(Call<SanPham> call, Response<SanPham> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(SanPhamActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("newProduct", sanPham);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void onFailure(Call<SanPham> call, Throwable throwable) {
                Toast.makeText(SanPhamActivity.this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                Log.e("ERROR INSERT PRODUCT: ", throwable.getMessage());
            }
        });
    }


    private void update(SanPham sanPham)
    {
        RetrofitService retrofitService = new RetrofitService();
        SanPhamApi sanPhamApi = retrofitService.getRetrofit().create(SanPhamApi.class);
        sanPhamApi.update(sanPham).enqueue(new Callback<SanPham>() {
            @Override
            public void onResponse(Call<SanPham> call, Response<SanPham> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(SanPhamActivity.this, "Sửa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("editedProduct", sanPham);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void onFailure(Call<SanPham> call, Throwable throwable) {
                Toast.makeText(SanPhamActivity.this, "Sửa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                Log.e("ERROR EDIT PRODUCT: ", throwable.getMessage());
            }
        });
    }




}