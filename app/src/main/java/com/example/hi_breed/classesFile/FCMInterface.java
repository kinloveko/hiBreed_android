package com.example.hi_breed.classesFile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface FCMInterface {

 @Headers({"Authorization: key="+CONSTANT.SERVER_KEY,"Content-Type:"+CONSTANT.REMOTE_MSG_CONTENT_TYPE})
    @POST("fcm/send")
    Call<pushNotification> sendNotification(@Body pushNotification notification);
}
