package com.fic.bunnyshopmobiletry5.catalogoAdapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.enviroment;

import java.util.List;
import java.util.Map;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishListViewHolder> {

    private List<Map<String, Object>> productos;
    private Context context;

    // Constructor del adaptador
    public WishListAdapter(Context context, List<Map<String, Object>> productos) {
        this.context = context;
        this.productos = productos;
    }

    @NonNull
    @Override
    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout de cada item de la wishlist
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wishlist, parent, false);
        return new WishListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListViewHolder holder, int position) {
        // Obtener el producto para la posición actual
        Map<String, Object> producto = productos.get(position);

        // Configurar los datos del producto
        holder.tvNombre.setText((String) producto.get("nombre"));

        // Validar el precio (puede ser un número o nulo)
        Object precio = producto.get("precio");
        if (precio instanceof Number) {
            holder.tvPrecio.setText(String.format("$%.2f", ((Number) precio).doubleValue()));
        } else {
            holder.tvPrecio.setText("$0.00"); // Valor predeterminado si no se puede obtener el precio
        }

        // Configurar la imagen del producto con Glide
        String imagen = (String) producto.get("imagen");
        if (imagen != null && !imagen.isEmpty()) {
            String urlImagen = enviroment.BASE_URL_STORAGE + "productos/" + imagen;
            Glide.with(context)
                    .load(urlImagen)
                    .placeholder(R.drawable.macaco_preocupado) // Imagen de carga
                    .error(R.drawable.bunny) // Imagen en caso de error
                    .into(holder.tvImagen);
        } else {
            holder.tvImagen.setImageResource(R.drawable.bunny); // Imagen por defecto si no se encuentra la URL
        }

        // Configurar el evento del botón "Ver Más"
        holder.btnVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("WishListAdapter", "Producto: " + producto.toString());

                // Obtener el ID del producto
                Object idProducto = producto.get("id_articulo");
                if (idProducto != null) {
                    // Pasar el ID del producto a otro fragmento
                    Bundle bundle = new Bundle();
                    bundle.putString("id_producto", idProducto.toString());

                    // Navegar al fragmento de detalle
                    View view = holder.itemView;
                    Navigation.findNavController(view).navigate(R.id.nav_jdd, bundle);
                } else {
                    Log.e("WishListAdapter", "ID del producto no encontrado.");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    // Método para actualizar los datos de la wishlist
    public void updateData(List<Map<String, Object>> nuevosProductos) {
        this.productos = nuevosProductos;
        notifyDataSetChanged(); // Notificamos que los datos han cambiado
    }

    // ViewHolder para el adaptador
    static class WishListViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio;
        ImageView tvImagen;
        Button btnVerMas;

        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvImagen = itemView.findViewById(R.id.tvImagen);
            btnVerMas = itemView.findViewById(R.id.buttonVerMas); // ID del botón "Ver Más"
        }
    }
}




















