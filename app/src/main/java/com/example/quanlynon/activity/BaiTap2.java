package com.example.quanlynon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlynon.R;

public class BaiTap2 extends AppCompatActivity {

    EditText edtSo1, edtSo2, edtKetQua;
    TextView tvKQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap2);
        setControl();
    }
    public void setControl(){
        edtSo1= findViewById(R.id.edtSo1);
        edtSo2 = findViewById(R.id.edtSo2);
        edtKetQua = findViewById(R.id.edtKetQua);
        tvKQ = findViewById(R.id.tvKQ);
    }

    public void phepTinh(View v) {
        int so1 = Integer.parseInt(edtSo1.getText().toString());
        int so2 = Integer.parseInt(edtSo2.getText().toString());
        int kq= Integer.parseInt(edtKetQua.getText().toString());
        String msg ="";

        if (v.getId() == R.id.btnCong) {
            Toast.makeText(this, "Phép cộng", Toast.LENGTH_SHORT).show();
            if(so1 + so2 == kq)
            {
                msg = so1 +" + " + so2 + " = "+ kq+": Đúng";
            }
            else
                msg = so1 +" + " + so2 + " = "+ kq+": Sai";
        }
        if (v.getId() == R.id.btnTru) {
            Toast.makeText(this, "Phép trừ", Toast.LENGTH_SHORT).show();
            if(so1 - so2 == kq)
            {
                msg = so1 +" - " + so2 + " = "+ kq+": Đúng";
            }
            else
                msg = so1 +" - " + so2 + " = "+ kq+": Sai";
        }
        if (v.getId() == R.id.btnNhan) {
            Toast.makeText(this, "Phép nhân", Toast.LENGTH_SHORT).show();
            if(so1 * so2 == kq)
            {
                msg = so1 +" * " + so2 + " = "+ kq+": Đúng";
            }
            else
                msg = so1 +" * " + so2 + " = "+ kq+": Sai";
        }
        if (v.getId() == R.id.btnChia) {
            Toast.makeText(this, "Phép chia", Toast.LENGTH_SHORT).show();
            if(so1 / so2 == kq)
            {
                msg = so1 +" / " + so2 + " = "+ kq+": Đúng";
            }
            else
                msg = so1 +" / " + so2 + " = "+ kq+": Sai";
        }

        tvKQ.append(msg + "\n");
        tvKQ.setBackgroundColor(Color.GREEN);
    }
}