package com.example.kantinkampus;

import android.content.Context;
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

public class OrderAdapterCustomer extends RecyclerView.Adapter<OrderAdapterCustomer.OrderViewHolder> {
    private Context context;
    private List<Order> orders;
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderAdapterCustomer(Context context, List<Order> orders, OnOrderClickListener listener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_customer, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        holder.tvOrderId.setText("Order #" + order.getId());
        holder.tvOrderTotal.setText(formatter.format(order.getTotal()));

        // Tampilkan Nama Stand (Penting di Multi-Seller!)
        // DBHelper query getOrdersByUser sudah join table_stand, pastikan Order.java punya field standName?
        // Jika belum ada di model Order, kita pakai text placeholder atau ambil dari DB lagi (tapi berat).
        // Solusi cepat: Kita tampilkan ID Stand atau teks generik jika model belum support nama stand.
        holder.tvStandName.setText("Stand ID: " + order.getStandId());

        // Tanggal
        if (holder.tvOrderDate != null) {
            holder.tvOrderDate.setText("📅 " + (order.getCreatedAt() != null ? order.getCreatedAt() : "Baru saja"));
        }

        // Status Styling
        String status = order.getStatus();
        String statusText = status;
        int colorRes = R.color.gray;
        String emoji = "📦";

        switch (status) {
            case "pending_payment":
                statusText = "Belum Bayar";
                colorRes = R.color.warning;
                emoji = "💳";
                break;
            case "pending_verification":
                statusText = "Verifikasi";
                colorRes = R.color.info;
                emoji = "⏳";
                break;
            case "cooking":
                statusText = "Dimasak";
                colorRes = R.color.primary;
                emoji = "👨‍🍳";
                break;
            case "ready":
                statusText = "Siap Ambil!";
                colorRes = R.color.success;
                emoji = "🥡";
                break;
            case "completed":
                statusText = "Selesai";
                colorRes = R.color.secondary;
                emoji = "✅";
                break;
            case "cancelled":
                statusText = "Batal";
                colorRes = R.color.danger;
                emoji = "❌";
                break;
        }

        holder.tvOrderStatus.setText(statusText);
        holder.cardStatus.setCardBackgroundColor(context.getResources().getColor(colorRes));
        if(holder.tvOrderEmoji != null) holder.tvOrderEmoji.setText(emoji);

        // Click Listener
        holder.itemView.setOnClickListener(v -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvStandName, tvOrderTotal, tvOrderStatus, tvOrderEmoji;
        CardView cardStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvStandName = itemView.findViewById(R.id.tvStandName); // Pastikan ID ini ada di XML
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderEmoji = itemView.findViewById(R.id.tvOrderEmoji);
            cardStatus = itemView.findViewById(R.id.cardStatus);
        }
    }
}