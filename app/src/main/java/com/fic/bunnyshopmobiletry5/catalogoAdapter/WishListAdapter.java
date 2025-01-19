package com.fic.bunnyshopmobiletry5.catalogoAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fic.bunnyshopmobiletry5.R;

import java.util.List;
import java.util.Map;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    private List<Map<String, Object>> itemList;
    private OnItemClickListener listener;  // Listener para el clic en el botón

    public WishListAdapter(List<Map<String, Object>> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    public void updateData(List<Map<String, Object>> newData) {
        itemList.clear();
        itemList.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> item = itemList.get(position);
        holder.textViewName.setText((String) item.get("name"));
        holder.textViewPrice.setText("$" + item.get("price"));
        Glide.with(holder.imageViewProduct.getContext())
                .load((String) item.get("image"))
                .into(holder.imageViewProduct);

        // Establecer el click listener para el botón de favoritos
        holder.favoriteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToWishlistClick(item);  // Llamar al listener cuando el botón es presionado
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface OnItemClickListener {
        void onAddToWishlistClick(Map<String, Object> item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice;
        ImageView imageViewProduct;
        Button favoriteButton;  // Asumiendo que tienes un botón en el layout

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            favoriteButton = itemView.findViewById(R.id.button3);  // El botón de agregar a favoritos
        }
    }
}




