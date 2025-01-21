package com.fic.bunnyshopmobiletry5.api;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface apiService {
    @GET("cotizaciones/usd")
    Call<ResponseBody> getData();

    @GET("estado")
    Call<ResponseBody> getEstadoApi();

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/crear")
    Call<ResponseBody> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("direccion") String direccion,
            @Field("telefono") String telefono
    );

    @FormUrlEncoded
    @POST("compra/comprar")
    Call<ResponseBody> comprar(
            @Field("id_articulo") String id_articulo,
            @Field("key_user") String key_user,
            @Field("tarjeta") String tarjeta,
            @Field("cvv") String cvv
    );

    @GET("articulo/get")
    Call<ResponseBody> getCatalogo();


    @GET("articulo/get")
    Call<ResponseBody> getArticulo(@Query("id_articulos") String id_articulo);

    @GET("catalog")  // Ruta del endpoint
    Call<List<Map<String, Object>>> getCatalog();

    @GET("wishlist/get")
    Call<List<Map<String, Object>>> getWishlist(@Query("key_user") String userId);  // Carga la lista de la wishlist

    @GET("carrito/get")
    Call<ResponseBody>  getCarrito(@Query("key_user") String key_user);

    @GET("compra/get")
    Call<ResponseBody> getCompras(@Query("key_user") String key_user);

    @GET("wishlist/check")
    Call<ResponseBody> wishlistCheck(
            @Query("key_producto") String productoId,
            @Query("key_user") String userId
    );

    @FormUrlEncoded
    @POST("wishlist/delete")
    Call<ResponseBody> wishlistDelete(
            @Field("id_articulo") String productoId,
            @Field("key_user") String userId
    );

    @FormUrlEncoded
    @POST("wishlist/agg")
    Call<ResponseBody> wishlistAgg(
            @Field("id_articulo") String productoId,
            @Field("key_user") String userId
    );

    @FormUrlEncoded
    @POST("carrito/add")
    Call<ResponseBody> agregarCarrito(
            @Field("id_articulo") String productoId,
            @Field("key_user") String userId
    );

    // Ruta para agregar un artículo a la wishlist
    @FormUrlEncoded
    @POST("wishlist/add")  // Esta ruta debe ser la que agrega el artículo a la wishlist
    Call<Void> addToWishlist(
            @Field("id_articulo") String id_articulo,
            @Field("key_user") String key_user
    );

    @FormUrlEncoded
    @POST("carrito/delete")
    Call<ResponseBody> deleteArticuloCarrito(
            @Field("id_articulo") String idArticulo,
            @Field("key_user") String idUser
    );
}


