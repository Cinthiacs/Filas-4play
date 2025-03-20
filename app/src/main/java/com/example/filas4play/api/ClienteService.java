package com.example.filas4play.api;
import com.example.filas4play.model.Cliente;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface  ClienteService {
    @POST("cliente")
    Call<Cliente> salvarCliente(@Body Cliente cliente);

    @GET("cliente")
    Call<List<Cliente>> listarClientes();
}