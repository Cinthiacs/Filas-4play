package com.example.filas4play.api;
import com.example.filas4play.model.Endereco;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ViaCepService {
    @GET("ws/{cep}/json/")
    Call<Endereco> getEndereco(@Path("cep") String cep);
}
