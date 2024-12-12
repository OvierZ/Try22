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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fic.bunnyshopmobiletry5.R;

import java.util.List;
import java.util.Map;

public class CatalogoAdapter extends RecyclerView.Adapter<CatalogoAdapter.CatalogoViewHolder> {

    private List<Map<String, Object>> productos;
//    private List<Map<String, Object>> producto;
    private Context context;

    public CatalogoAdapter(Context context, List<Map<String, Object>> productos) {
        this.context = context;
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

        // Configurar los datos del producto
        holder.tvNombre.setText((String) producto.get("nombre"));
        holder.tvPrecio.setText(String.format("$%.2f", producto.get("precio")));

        // Usar Glide para cargar la imagen desde la URL
        String urlImagen = (String) producto.get("imagen");
        Glide.with(context)
                .load(urlImagen)
                .placeholder(R.drawable.macaco_preocupado) // Imagen de carga
                .error(R.drawable.bunny) // Imagen en caso de error
                .into(holder.tvImagen);

        // Configurar el evento del botón "Ver Más"
        holder.btnVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Adaptador ~ Adaptador", producto.toString());

                Object idProducto = producto.get("id_articulo");

                if(idProducto != null) {

                    Bundle bundle = new Bundle();
                    bundle.putString("id_producto", idProducto.toString()); // Pasa el ID como String

                    // Obtén la vista desde el ViewHolder
                    View view = holder.itemView;

                    // Navega al fragmento con el ID del producto
                    Navigation.findNavController(view).navigate(R.id.nav_jdd, bundle);

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class CatalogoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio;
        ImageView tvImagen;
        Button btnVerMas;

        public CatalogoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvImagen = itemView.findViewById(R.id.tvImagen);
            btnVerMas = itemView.findViewById(R.id.button5); // ID del botón "Ver Más"
        }
    }
}
