package com.fic.bunnyshopmobiletry5.ui.pago;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.api.apiService;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagoFragment extends Fragment {

    private PagoViewModel mViewModel;
    private String id_producto;

    private View rootView;

    private EditText etNumeroTarjeta, etFechaExpiracion, etCVV;

    private TextView etNombreTitular;
    private CheckBox cbTerminos;
    private Button btnEnviarPago;
    public static PagoFragment newInstance() {
        return new PagoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_pago, container, false);

        // Referencias a los campos del formulario
         etNumeroTarjeta = rootView.findViewById(R.id.etNumeroTarjeta);
         etFechaExpiracion = rootView.findViewById(R.id.etFechaExpiracion);
         etCVV = rootView.findViewById(R.id.etCVV);
         etNombreTitular = rootView.findViewById(R.id.etNombreTitular);
         cbTerminos = rootView.findViewById(R.id.cbTerminos);
         btnEnviarPago = rootView.findViewById(R.id.btnEnviarPago);

        // Configurar el TextWatcher para el campo de fecha de expiración
        etFechaExpiracion.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false; // Para evitar cambios recursivos

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Evitar cambios recursivos
                if (isUpdating) {
                    return;
                }
                isUpdating = true;

                String input = charSequence.toString();

                // Si el texto tiene exactamente 2 caracteres y no contiene una barra
                if (input.length() == 2 && !input.contains("/")) {
                    etFechaExpiracion.setText(input + "/");
                    etFechaExpiracion.setSelection(etFechaExpiracion.getText().length()); // Mover el cursor al final
                }

                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Configura el listener para el botón de pago
        btnEnviarPago.setOnClickListener(v -> procesarPago());


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Obtener el argumento (ID del producto)
        if (getArguments() != null) {
            String idProducto = getArguments().getString("id_producto", "---");
            Log.d("Producto_desde_pag", "id_producto" + idProducto);

            id_producto = idProducto;
            // Usa el idProducto aquí
        }

        Log.d("SIN ARGUMENTOS", "ALgo Salio mal");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PagoViewModel.class);
        // TODO: Use the ViewModel

    }

    private void procesarPago() {
        // Obtener los datos del formulario
        String numeroTarjeta = etNumeroTarjeta.getText().toString().trim();
        String fechaExpiracion = etFechaExpiracion.getText().toString().trim();
        String cvv = etCVV.getText().toString().trim();
        String nombreTitular = etNombreTitular.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (numeroTarjeta.isEmpty() || fechaExpiracion.isEmpty() || cvv.isEmpty() || nombreTitular.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que el número de tarjeta tenga 16 dígitos
        if (numeroTarjeta.length() != 16 || !numeroTarjeta.matches("\\d+")) {
            Toast.makeText(getActivity(), "El número de tarjeta debe tener 16 dígitos y ser solo numérico.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar la fecha de expiración (formato MM/AA)
        if (!fechaExpiracion.matches("\\d{2}/\\d{2}")) {
            Toast.makeText(getActivity(), "Fecha de expiración no válida. Debe ser en formato MM/AA.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el CVV (3 dígitos)
        if (cvv.length() != 3 || !cvv.matches("\\d{3}")) {
            Toast.makeText(getActivity(), "CVV debe tener 3 dígitos numéricos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar si aceptó los términos y condiciones
        if (!cbTerminos.isChecked()) {
            Toast.makeText(getActivity(), "Debe aceptar los términos y condiciones.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí puedes realizar el procesamiento del pago con la API que elijas
        // Por ejemplo, llamar a una función que se encargue de enviar los datos a tu servidor

        // Mostrar un mensaje de éxito
//        Toast.makeText(getActivity(), "Pago procesado con éxito.", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        // Obtener el JSON como String
        String userData = sharedPref.getString("user", null);

        try {
            // Convertir el String JSON a un JSONObject
            JSONObject userJson = new JSONObject(userData);

            comprar(id_producto, userJson.getString("id_user"), etNumeroTarjeta.getText().toString(), etCVV.getText().toString());
        } catch (Exception e) {
            Log.e("Shared_Preferences", "Error al procesar el JSON", e);
        }




        // Limpiar los campos
//        limpiarCampos();
    }

    private void limpiarCampos() {
        etNumeroTarjeta.setText("");
        etFechaExpiracion.setText("");
        etCVV.setText("");
        etNombreTitular.setText("");
        cbTerminos.setChecked(false);
    }

    private void comprar(String id_articulo, String key_user, String tarjeta, String cvv) {
        apiService apiService = RetrofitInstance.getApiService();  // Obtén la instancia del servicio API

        // Realiza la solicitud sin parámetros
        Call<ResponseBody> call = apiService.comprar(id_articulo, key_user, tarjeta, cvv);  // Usamos ResponseBody para obtener la respuesta cruda

        // Ejecuta la solicitud de forma asíncrona
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Verifica que la respuesta sea exitosa (código 200)
                if (response.isSuccessful()) {
                    try {
                        // Obtener la respuesta como un String
                        String responseString = response.body().string();

                        // Imprimir la respuesta completa en Logcat (opcional)
                        Log.d("API_RESPONSE_COMPRA", "Respuesta COMPRA: " + responseString);

                        Toast.makeText(rootView.getContext(),"COMPRA REALIZADA",Toast.LENGTH_SHORT).show();

                        // Navega al fragmento con el ID del producto
                        Navigation.findNavController(rootView).navigate(R.id.nav_demeza);


                    } catch (IOException e) {
                        Log.e("API_ERROR_COMPRA", "Error al leer el cuerpo de la respuesta: " + e);
                        Toast.makeText(rootView.getContext(),"Ocurrio un error en la respuesta",Toast.LENGTH_SHORT).show();

                    }
                } else {
                    // Si la respuesta no es exitosa, muestra el error en el log
                    Log.e("API_ERROR_COMPRA", "Error en la respuesta: Código " + response.code() + ", Mensaje: " + response.message());
                    try {
                        String errorBody = response.errorBody().string(); // Obtener el cuerpo del error
                        Log.e("API_ERROR_COMPRA", "Cuerpo del error: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(rootView.getContext(),"Compra denegada",Toast.LENGTH_SHORT).show();

                    //textViewResponse.setText("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Si la solicitud falla, muestra el error en el TextView
                Log.e("API_ERROR_COMPRA", "Error en la solicitud: " + t);
                Toast.makeText(rootView.getContext(),"Algo salió mal...",Toast.LENGTH_SHORT).show();

            }
        });
    }

}