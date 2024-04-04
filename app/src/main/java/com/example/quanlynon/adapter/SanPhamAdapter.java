package com.example.quanlynon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlynon.R;
import com.example.quanlynon.model.SanPham;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.MyViewHolder> {

    Context context;
    int resource;
    List<SanPham> sanPhamList;

    private OnItemClickListener listener;

    private OnItemLongClickListener onItemLongClickListener;


    public SanPhamAdapter(Context context) {
        this.context = context;
    }
    public SanPhamAdapter(Context context, List<SanPham> sanPhamList) {
        this.context = context;
        this.sanPhamList = sanPhamList;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
    public void setSanPhams(List<SanPham> sanPhamList) {
        this.sanPhamList = sanPhamList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_san_pham, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPham sanPham = sanPhamList.get(position);
        holder.itemTenSanPham.setText(sanPham.getTenSanPham());
        DecimalFormat dft = new DecimalFormat("###,###,###");
        holder.itemGiaSanPham.setText(String.format("Giá: %sđ", dft.format(sanPham.getGia())));
        Glide.with(context)
                .load(sanPham.getHinhAnh())
                .into(holder.itemSanPhamImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (listener != null && clickedPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(clickedPosition);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (listener != null && clickedPosition != RecyclerView.NO_POSITION) {
                    onItemLongClickListener.onItemLongClick(clickedPosition);
                }
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemGiaSanPham, itemTenSanPham;
        ImageView itemSanPhamImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemGiaSanPham = itemView.findViewById(R.id.itemGiaSanPham);
            itemTenSanPham = itemView.findViewById(R.id.itemTenSanPham);
            itemSanPhamImage = itemView.findViewById(R.id.itemSanPhamImage);
        }
    }
}
