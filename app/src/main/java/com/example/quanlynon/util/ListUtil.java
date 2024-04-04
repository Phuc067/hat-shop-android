package com.example.quanlynon.util;

import com.example.quanlynon.model.KieuDang;
import com.example.quanlynon.model.SanPham;

import java.util.List;
import java.util.Objects;

public class ListUtil {

    public static int getPositionInList(String obj, List<String> objects)
    {
        int selectedPosition = -1;
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).equals(obj)){
                selectedPosition = i;
                break;
            }
        }
        return  selectedPosition;
    }

    public static int getPositionInList(KieuDang obj, List<KieuDang> objects)
    {
        int selectedPosition = -1;
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getMaKieuDang().equals(obj.getMaKieuDang())){
                selectedPosition = i;
                break;
            }
        }
        return  selectedPosition;
    }

    public static int getPositionInList(SanPham obj, List<SanPham> objects)
    {
        int selectedPosition = -1;
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getMaSanPham().equals(obj.getMaSanPham())){
                selectedPosition = i;
                break;
            }
        }
        return  selectedPosition;
    }

}
