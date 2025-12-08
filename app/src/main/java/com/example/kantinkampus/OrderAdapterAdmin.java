package com.example.kantinkampus;

import android.content.Context;
import android.graphics.Color;
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

public class OrderAdapterAdmin extends RecyclerView.Adapter<OrderAdapterAdmin.OrderViewHolder> {
    private Context context;
    private List<Order> orders;
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderAdapterAdmin(Context context, List<Order> orders, OnOrderClickListener listener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_admin, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        holder.tvOrderId.setText("Order #" + order.getId());
        holder.tvOrderTotal.setText(formatter.format(order.getTotal()));

        // Tampilkan Nama Pembeli (Pastikan query JOIN di DBHelper sudah mengambil kolom 'user_name')
        // Jika belum ada di model Order, ganti dengan User ID sementara
        holder.tvCustomerName.setText("Customer ID: " + order.getUserId());

        // Info Pembayaran
        String payMethod = order.getPaymentMethod() != null ? order.getPaymentMethod().toUpperCase() : "CASH";
        holder.tvPaymentMethod.setText(payMethod);

        // Status Styling
        String status = order.getStatus();
        String statusText = status;
        int colorRes = R.color.gray; // Default

        switch (status) {
            case "pending_payment":
                statusText = "Bayar Tunai";
                colorRes = R.color.warning;
                break;
            case "pending_verification":
                statusText = "Cek Transfer";
                colorRes = R.color.info;
                break;
            case "cooking":
                statusText = "Sedang Dimasak";
                colorRes = R.color.primary;
                break;
            case "ready":
                statusText = "Siap Diambil";
                colorRes = R.color.success;
                break;
            case "completed":
                statusText = "Selesai";
                colorRes = R.color.secondary;
                break;
            case "cancelled":
                statusText = "Dibatalkan";
                colorRes = R.color.danger;
                break;
        }

        holder.tvOrderStatus.setText(statusText);
        holder.cardStatus.setCardBackgroundColor(context.getResources().getColor(colorRes));

        // Click Listener
        holder.itemView.setOnClickListener(v -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvCustomerName, tvPaymentMethod, tvOrderTotal, tvOrderStatus;
        CardView cardStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            // Note: Pastikan ID ini ada di XML order_item_admin.xml
            // Jika XML Anda menggunakan tvStandName, kita pakai itu dulu untuk nama customer
            // atau sesuaikan ID di XML-nya.

            // Menggunakan ID yang ada di XML sebelumnya:
            // tvStandName -> Kita alihfungsikan jadi Customer Name di view admin
            // tvOrderItems -> Kita sembunyikan atau isi "Lihat Detail"

            tvCustomerName = itemView.findViewById(R.id.tvCustomerName); // Pastikan ID ini ada
            if (tvCustomerName == null) tvCustomerName = itemView.findViewById(R.id.tvStandName); // Fallback

            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod); // Pastikan ID ini ada
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            cardStatus = itemView.findViewById(R.id.cardStatus);
        }
    }
}