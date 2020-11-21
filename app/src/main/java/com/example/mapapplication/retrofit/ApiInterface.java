package com.example.mapapplication.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("driving-car")
    Call<JsonObject> getRoadDuration(@Query("api_key") String authHeader,
                                       @Query("start") String start,
                                       @Query("end") String end);


//    @POST("participate/register/{id}")
//    Call<TokenRequestDto> sendQRToken(@Header("Authorization") String authHeader,
//                                      @Path("id") int id,
//                                      @Query("token") String token);
}
