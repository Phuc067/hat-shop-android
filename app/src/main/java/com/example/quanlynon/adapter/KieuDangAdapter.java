package com.example.quanlynon.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlynon.model.KieuDang;

import java.util.ArrayList;
import java.util.List;

public class KieuDangAdapter extends ArrayAdapter<KieuDang> {
    public KieuDangAdapter(Context context, List<KieuDang> kieuDangList) {
        super(context, 0, kieuDangList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        KieuDang kieuDang = getItem(position);
        if (kieuDang != null) {
            textView.setText(kieuDang.getTenKieuDang());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        KieuDang kieuDang = getItem(position);
        if (kieuDang != null) {
            textView.setText(kieuDang.getTenKieuDang());
        }

        return convertView;
    }
}

