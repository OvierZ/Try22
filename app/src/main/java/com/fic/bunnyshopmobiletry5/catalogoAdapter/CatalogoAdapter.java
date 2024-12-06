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

public class CatalogoAdapter extends RecyclerView.Adapter<CatalogoAdapter.CatalogoViewHolder> {

    private List<Map<String, Object>> productos;

    public CatalogoAdapter(List<Map<String, Object>> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public CatalogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_catalogo, parent, false);
        return new CatalogoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogoViewHolder holder, int position) {
        Map<String, Object> producto = productos.get(position);

        // Acceder a los datos del mapa
        holder.tvNombre.setText((String) producto.get("nombre"));
        //holder.tvDescripcion.setText((String) producto.get("descripcion"));
        holder.tvPrecio.setText(String.format("$%.2f", producto.get("precio")));

        // Usa Glide para cargar la imagen desde una URL
        String urlImagen = (String) producto.get("imagen"); // La clave "imagen" debe contener la URL
        Glide.with(holder.itemView.getContext())
                .load(urlImagen)
                .placeholder(R.drawable.macaco_preocupado) // Imagen de carga
                .error(R.drawable.bunny) // Imagen en caso de error
                .into(holder.tvImagen);

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class CatalogoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvPrecio;
        ImageView tvImagen;

        public CatalogoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            //tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvImagen = itemView.findViewById(R.id.tvImagen);
        }
    }
}
