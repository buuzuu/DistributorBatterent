package com.example.distributor_batterent.Services;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StatusService {

    @POST("isAllowedToUse")
    Call<String> allowBattery(@Query("allow") String input);

}
