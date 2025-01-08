package com.fic.bunnyshopmobiletry5.ui.briseyda;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.fic.bunnyshopmobiletry5.R;
import java.util.ArrayList;
import java.util.List;

public class BriseydaFragment extends Fragment {

    private ImageView productImage;
    private TextView productTitle;
    private TextView productDescription;
    private ImageButton changeInfoBtn; // Cambiado a ImageButton

    private int currentIndex = 0;

    // Lista de productos
    private final List<Product> products = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar la vista del fragmento
        View root = inflater.inflate(R.layout.fragment_briseyda, container, false);

        // Inicializar vistas
        productImage = root.findViewById(R.id.productImage1);
        productTitle = root.findViewById(R.id.productTitle1);
        productDescription = root.findViewById(R.id.productDescription1);
        changeInfoBtn = root.findViewById(R.id.changeInfoBtn); // Corregido el tipo

        // Inicializar productos
        initializeProducts();

        // Configurar botón para cambiar información
        changeInfoBtn.setOnClickListener(v -> updateProductInfo());

        return root;
    }

    /**
     * Inicializa la lista de productos
     */
    private void initializeProducts() {
        products.add(new Product(R.drawable._producto, "Producto 1", "Descripción breve del producto 1"));
        products.add(new Product(R.drawable._producto2, "Producto 2", "Descripción breve del producto 2"));

        // Mostrar el primer producto
        if (!products.isEmpty()) {
            setProductInfo(products.get(0));
        }
    }

    /**
     * Actualiza la información al siguiente producto
     */
    private void updateProductInfo() {
        if (products.isEmpty()) return; // Validar que la lista no esté vacía

        // Cambiar al siguiente producto
        currentIndex = (currentIndex + 1) % products.size();
        setProductInfo(products.get(currentIndex));
    }

    /**
     * Establece la información del producto en las vistas
     */
    private void setProductInfo(Product product) {
        productImage.setImageResource(product.getImageRes());
        productTitle.setText(product.getTitle());
        productDescription.setText(product.getDescription());
    }

    /**
     * Clase interna para representar un producto
     */
    private static class Product {
        private final int imageRes;
        private final String title;
        private final String description;

        public Product(int imageRes, String title, String description) {
            this.imageRes = imageRes;
            this.title = title;
            this.description = description;
        }

        public int getImageRes() {
            return imageRes;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }
}
