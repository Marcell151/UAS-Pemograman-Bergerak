package com.example.uas_pemrogramanbergerak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class book_adapter extends RecyclerView.Adapter<book_adapter.ViewHolder> {

    private Context context;
    private ArrayList<book_model> listBuku;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }

    public book_adapter(Context context, ArrayList<book_model> list) {
        this.context = context;
        this.listBuku = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_buku, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        book_model buku = listBuku.get(position);
        holder.tvJudul.setText(buku.getJudul());
        holder.tvStatus.setText(buku.isDipinjam() ? "Dipinjam" : "Tersedia");
        holder.tvStatus.setTextColor(buku.isDipinjam() ? 0xFFD32F2F : 0xFF388E3C);

        // 🖼️ Tampilkan cover dari data
        if (buku.getCoverResId() != 0) {
            holder.imgBuku.setImageResource(buku.getCoverResId());
        } else {
            holder.imgBuku.setImageResource(R.drawable.ic_book_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return listBuku.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBuku;
        TextView tvJudul, tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            imgBuku = itemView.findViewById(R.id.imgBuku);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(getAdapterPosition());
            });
        }
    }
}
