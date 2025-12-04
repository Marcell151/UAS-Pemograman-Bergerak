package com.example.kantinkampus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private Context context;
    private List<Menu> menuList;
    private OnMenuClickListener listener;

    public interface OnMenuClickListener {
        void onAddToCart(Menu menu);
    }

    public MenuAdapter(Context context, List<Menu> menuList, OnMenuClickListener listener) {
        this.context = context;
        this.menuList = menuList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu menu = menuList.get(position);
        holder.tvMenuNama.setText(menu.getNama());

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String hargaFormatted = formatter.format(menu.getHarga());
        holder.tvMenuHarga.setText(hargaFormatted);

        holder.btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddToCart(menu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView tvMenuNama, tvMenuHarga, btnTambah;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuNama = itemView.findViewById(R.id.tvMenuNama);
            tvMenuHarga = itemView.findViewById(R.id.tvMenuHarga);
            btnTambah = itemView.findViewById(R.id.btnTambah);
        }
    }
}