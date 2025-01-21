package com.fic.bunnyshopmobiletry5.catalogoAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.fic.bunnyshopmobiletry5.MainActivity;
import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.api.apiService;
import com.fic.bunnyshopmobiletry5.api.enviroment;
import com.fic.bunnyshopmobiletry5.ui.demeza.DemezaFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private List<Map<String, Object>> productos;
    private Context context;

    // Constructor del adaptador
    public CarritoAdapter(Context context, List<Map<String, Object>> productos) {
        this.context = context;
        this.productos = productos;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout de cada item del catálogo
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_compra, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        // Obtener el producto para la posición actual
        Map<String, Object> producto = productos.get(position);

        // Configurar los datos del producto
        holder.tvNombre.setText((String) producto.get("nombre"));

        // Validar el precio (puede ser un número o nulo)
        //Object precio = producto.get("precio");
        /*if (precio instanceof Number) {
            holder.tvPrecio.setText(String.format("$%.2f", ((Number) precio).doubleValue()));
        } else {
            holder.tvPrecio.setText("$0.00"); // Valor predeterminado si no se puede obtener el precio
        }*/

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
                int position = holder.getAdapterPosition(); // Obtén la posición del producto
                if (position != RecyclerView.NO_POSITION) {
                    // Obtén el producto a eliminar
                    Map<String, Object> producto = productos.get(position);

                    Log.d("PRODUCTO", "DATA" + producto);

                    Object idProducto = producto.get("id_articulo");
                    if (idProducto != null) {
                        String idArticulo = idProducto.toString();
                        String idUser = getUserIdFromPreferences();

                        // Llama a la API para eliminar el producto
                        apiService apiService = RetrofitInstance.getApiService();
                        Call<ResponseBody> call = apiService.deleteArticuloCarrito(idArticulo, idUser);

                        Log.d("PARAMETROS", "ART: " + idArticulo + "USER" + idUser);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                        try {
                                            String responseBody = response.body().string();
                                            Log.d("RESPUESTA_DELETE", "RESPUESTA:" + responseBody);
                                    // Eliminar el producto de la lista local
                                    productos.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, productos.size());
                                    Log.d("API_SUCCESS", "Producto eliminado correctamente.");
                                } catch (Exception e) {
                                            throw new RuntimeException(e);
                                }
                            } else {
                                Log.e("API_ERROR", "Error en la respuesta: " + response.message());
                            }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("API_ERROR", "Error en la solicitud: ", t);
                                // Opcional: Muestra un mensaje al usuario
                                //Toast.makeText(context, "Error al eliminar el producto. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.e("CarritoAdapter", "ID del producto es nulo.");
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public Double getTotal(){

        Double total = 0.00;

        for (int i = 0; i < getItemCount(); i++) {
            Map<String, Object> producto = productos.get(i);

            Object precio = producto.get("precio");

            total = total + (Double.parseDouble(precio.toString()));
        }

        return total;
    }

    static class CarritoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio;
        ImageView tvImagen;
        Button btnVerMas;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvTituloCompra);
            //tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvImagen = itemView.findViewById(R.id.tvImagenCompra);
            btnVerMas = itemView.findViewById(R.id.tvBotonBorrarProducto); // ID del botón "Ver Más"
        }
    }

    private String getUserIdFromPreferences() {
        // Obtener las SharedPreferences
        SharedPreferences sharedPref = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        // Obtener los datos del usuario como String
        String userData = sharedPref.getString("user", null);
        Log.d("User_Data", "Datos completos: " + userData);
        // Procesar el JSON para obtener el ID del usuario
        if (userData != null) {
            try {
                JSONObject userJson = new JSONObject(userData);
                return userJson.getString("id_user");
            } catch (JSONException e) {
                Log.e("JSON_ERROR", "Error al procesar el JSON: " + e);
            }
        } else {
            Log.d("Shared_Preferences", "No se encontraron datos del usuario.");
        }

        return null;
    }


}
