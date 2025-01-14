package com.fic.bunnyshopmobiletry5.catalogoAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.fic.bunnyshopmobiletry5.R;


import java.util.List;
import java.util.Map;


public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {


    private final List<Map<String, Object>> itemList;


    public WishListAdapter(List<Map<String, Object>> itemList) {
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout para cada elemento del RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false);
        return new ViewHolder(view); // Ahora usamos la variable view correctamente
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> item = itemList.get(position);


        holder.textViewName.setText((String) item.get("name"));
        holder.textViewPrice.setText("$" + item.get("price"));
        Glide.with(holder.imageViewProduct.getContext())
                .load((String) item.get("image"))
                .into(holder.imageViewProduct);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice;
        ImageView imageViewProduct;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
        }
    }
}

