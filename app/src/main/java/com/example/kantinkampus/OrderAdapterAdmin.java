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

        // Set order info
        holder.tvOrderId.setText("#" + order.getId());
        holder.tvOrderDate.setText(order.getCreatedAt());
        holder.tvCustomerName.setText(order.getUserName());
        holder.tvStandName.setText("🏪 " + order.getStandName());
        holder.tvOrderTotal.setText(formatter.format(order.getTotal()));
        holder.tvPaymentMethod.setText(order.getPaymentMethod());
        holder.tvOrderEmoji.setText(order.getStatusEmoji());

        // Set order items count (you can enhance this to show actual items)
        holder.tvOrderItems.setText("Lihat detail pesanan");

        // Set status with color
        holder.tvOrderStatus.setText(order.getStatusEmoji() + " " + order.getStatusText());

        int statusColor;
        switch (order.getStatus()) {
            case "pending":
                statusColor = context.getResources().getColor(R.color.warning);
                break;
            case "processing":
                statusColor = context.getResources().getColor(R.color.info);
                break;
            case "ready":
                statusColor = context.getResources().getColor(R.color.success);
                break;
            case "completed":
                statusColor = context.getResources().getColor(R.color.secondary);
                break;
            case "cancelled":
                statusColor = context.getResources().getColor(R.color.danger);
                break;
            default:
                statusColor = context.getResources().getColor(R.color.text_gray);
        }
        holder.cardStatus.setCardBackgroundColor(statusColor);

        // Set click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOrderClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvCustomerName, tvStandName, tvOrderItems,
                tvPaymentMethod, tvOrderTotal, tvOrderStatus, tvOrderEmoji;
        CardView cardStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvStandName = itemView.findViewById(R.id.tvStandName);
            tvOrderItems = itemView.findViewById(R.id.tvOrderItems);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderEmoji = itemView.findViewById(R.id.tvOrderEmoji);
            cardStatus = itemView.findViewById(R.id.cardStatus);
        }
    }
}