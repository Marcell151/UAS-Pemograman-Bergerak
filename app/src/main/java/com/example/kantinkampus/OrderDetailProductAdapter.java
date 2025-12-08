package com.example.kantinkampus;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailProductAdapter extends RecyclerView.Adapter<OrderDetailProductAdapter.ViewHolder> {
    private Context context;
    private List<OrderItem> items;
    private boolean isOrderCompleted; // Status apakah boleh review
    private boolean isBuyer;          // Hanya buyer yang boleh review

    public OrderDetailProductAdapter(Context context, List<OrderItem> items, boolean isOrderCompleted, boolean isBuyer) {
        this.context = context;
        this.items = items;
        this.isOrderCompleted = isOrderCompleted;
        this.isBuyer = isBuyer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem item = items.get(position);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        holder.tvItemQty.setText(item.getQty() + "x");
        holder.tvItemName.setText(item.getMenuName());
        holder.tvItemPrice.setText("@ " + formatter.format(item.getPrice()));
        holder.tvItemSubtotal.setText(formatter.format(item.getSubtotal()));

        // LOGIKA TOMBOL REVIEW
        // Muncul HANYA JIKA: Order Selesai DAN User adalah Pembeli
        if (isOrderCompleted && isBuyer) {
            holder.btnReviewItem.setVisibility(View.VISIBLE);
            holder.btnReviewItem.setOnClickListener(v -> {
                // Buka halaman Review
                Intent intent = new Intent(context, AddReviewActivity.class);
                intent.putExtra("menu_id", item.getMenuId());
                intent.putExtra("menu_name", item.getMenuName());
                intent.putExtra("order_id", item.getOrderId());
                context.startActivity(intent);
            });
        } else {
            holder.btnReviewItem.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemQty, tvItemName, tvItemPrice, tvItemSubtotal;
        CardView btnReviewItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemQty = itemView.findViewById(R.id.tvItemQty);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvItemSubtotal = itemView.findViewById(R.id.tvItemSubtotal);
            btnReviewItem = itemView.findViewById(R.id.btnReviewItem);
        }
    }
}