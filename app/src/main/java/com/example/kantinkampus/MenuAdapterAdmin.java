package com.example.kantinkampus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MenuAdapterAdmin extends RecyclerView.Adapter<MenuAdapterAdmin.MenuViewHolder> {
    private Context context;
    private List<Menu> menuList;
    private OnMenuActionListener listener;

    public interface OnMenuActionListener {
        void onToggleStatus(Menu menu);
        void onEditMenu(Menu menu);
        void onDeleteMenu(Menu menu);
    }

    public MenuAdapterAdmin(Context context, List<Menu> menuList, OnMenuActionListener listener) {
        this.context = context;
        this.menuList = menuList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item_admin, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu menu = menuList.get(position);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        holder.tvMenuNama.setText(menu.getNama());
        holder.tvMenuDeskripsi.setText(menu.getDeskripsi());
        holder.tvMenuKategori.setText(menu.getKategori());
        holder.tvMenuHarga.setText(formatter.format(menu.getHarga()));

        // --- LOGIKA GAMBAR DENGAN RESIZE ---
        if (menu.getImage() != null && !menu.getImage().isEmpty()) {
            // Langsung baca file path
            Bitmap bitmap = BitmapFactory.decodeFile(menu.getImage());

            if (bitmap != null) {
                holder.ivMenuImage.setVisibility(View.VISIBLE);
                holder.tvMenuIcon.setVisibility(View.GONE);
                holder.ivMenuImage.setImageBitmap(bitmap);
            } else {
                // Fallback: File mungkin terhapus atau format lama (URI)
                holder.ivMenuImage.setVisibility(View.GONE);
                holder.tvMenuIcon.setVisibility(View.VISIBLE);
            }
        } else {
            holder.ivMenuImage.setVisibility(View.GONE);
            holder.tvMenuIcon.setVisibility(View.VISIBLE);
        }

        if (menu.isAvailable()) {
            holder.tvMenuStatus.setText("Tersedia");
            holder.cardMenuStatus.setCardBackgroundColor(context.getResources().getColor(R.color.success));
        } else {
            holder.tvMenuStatus.setText("Habis");
            holder.cardMenuStatus.setCardBackgroundColor(context.getResources().getColor(R.color.danger));
        }

        holder.btnToggleStatus.setOnClickListener(v -> listener.onToggleStatus(menu));
        holder.btnEditMenu.setOnClickListener(v -> listener.onEditMenu(menu));
        holder.btnDeleteMenu.setOnClickListener(v -> listener.onDeleteMenu(menu));
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView tvMenuNama, tvMenuDeskripsi, tvMenuKategori, tvMenuHarga, tvMenuStatus, tvMenuIcon;
        ImageView ivMenuImage;
        CardView btnToggleStatus, btnEditMenu, btnDeleteMenu, cardMenuStatus;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuNama = itemView.findViewById(R.id.tvMenuNama);
            tvMenuDeskripsi = itemView.findViewById(R.id.tvMenuDeskripsi);
            tvMenuKategori = itemView.findViewById(R.id.tvMenuKategori);
            tvMenuHarga = itemView.findViewById(R.id.tvMenuHarga);
            tvMenuStatus = itemView.findViewById(R.id.tvMenuStatus);
            tvMenuIcon = itemView.findViewById(R.id.tvMenuIcon);
            ivMenuImage = itemView.findViewById(R.id.ivMenuImage);
            btnToggleStatus = itemView.findViewById(R.id.btnToggleStatus);
            btnEditMenu = itemView.findViewById(R.id.btnEditMenu);
            btnDeleteMenu = itemView.findViewById(R.id.btnDeleteMenu);
            cardMenuStatus = itemView.findViewById(R.id.cardMenuStatus);
        }
    }
}