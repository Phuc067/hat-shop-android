package com.example.quanlynon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.quanlynon.R;

public class BaiTap3 extends AppCompatActivity {
    EditText edtHoTen, edtCCCD;

    RadioButton[] radioButtons = new RadioButton[3];

    CheckBox[] checkBoxes = new CheckBox[3];

    Button btnDangKy, btnNhapLai, btnThoat;

    TextView tvKQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap3);
        setControl();
        setEvent();
    }
    private void setEvent(){
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg="\nThông tin";
                msg += "\nHọ tên: " + edtHoTen.getText().toString();
                msg +="\nCCCD: " + edtCCCD.getText().toString();
                for (RadioButton radioButton : radioButtons) {
                    if (radioButton.isChecked()) {
                        msg += "\nBằng cấp: " + radioButton.getText();
                        break;
                    }
                }

                msg += "\nSở thích: ";
                for (CheckBox checkBox: checkBoxes){
                    if(checkBox.isChecked()){
                        msg += checkBox.getText() + ", ";
                    }
                }

                msg = msg.substring(0,msg.length() -2);
                tvKQ.append( "\n" +msg );
                tvKQ.setBackgroundColor(Color.GREEN);
            }
        });

        btnNhapLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtHoTen.setText("");
                edtCCCD.setText("");
                radioButtons[0].setChecked(true);
                for (CheckBox checkBox: checkBoxes)
                {
                    checkBox.setChecked(false);
                }
                tvKQ.setText("");
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BaiTap3.this);
                builder.setMessage("Bạn có chắc chắn muốn thoát không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setControl(){
        edtHoTen = findViewById(R.id.edtHoTen);
        edtCCCD = findViewById(R.id.edtCCCD);

        radioButtons[0] = findViewById(R.id.rbDaiHoc);
        radioButtons[1] = findViewById(R.id.rbCaoDang);
        radioButtons[2] = findViewById(R.id.rbTrungCap);

        checkBoxes[0] = findViewById(R.id.cbChoiGame);
        checkBoxes[1] = findViewById(R.id.cbDocSach);
        checkBoxes[2] = findViewById(R.id.cbDocBao);

        btnDangKy = findViewById(R.id.btnDangKy);
        btnNhapLai = findViewById(R.id.btnNhapLai);
        btnThoat = findViewById(R.id.btnThoat);

        tvKQ = findViewById(R.id.tvKQ);
    }
}