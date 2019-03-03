package com.example.distributor_batterent;

import com.example.distributor_batterent.Model.NotificationData;
import com.example.distributor_batterent.Model.ResponseClass;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FCMService {

    @Headers({
            "Content-Type: application/json",
            "Authorization: key=AAAA1rI4gP8:APA91bHRDasvmDMH0Mll7mv4kE5jIMNtjTfsJlu3WBoamBw_WhFaAgblbXTvjX-g_3bDPcwvK0wd0tBXSUAtAiQW_iXvmLQp0vON-Fg3LprY2kHcL4ncrHhVm6a_gkQxEhqpwZH7Yhr_"
    })
    @POST("send")
    Call<ResponseClass> sendNotification(@Body NotificationData data);

}
