package com.example.quanlynon.constant;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static final String BASE_URL = "http://192.168.1.10:8080/api/";
    public static List<String> gioiTinhOptions = new ArrayList<>();
    static {
        gioiTinhOptions.add("Nam");
        gioiTinhOptions.add("Nữ");
        gioiTinhOptions.add("Unisex");
    }

}
