package com.gooddelivery.delivery.api;

/**
 * Created by santhosh@appoets.com on 03-10-17.
 */

import com.gooddelivery.delivery.model.Message;
import com.gooddelivery.delivery.model.Notice;
import com.gooddelivery.delivery.model.Order;
import com.gooddelivery.delivery.model.Profile;
import com.gooddelivery.delivery.model.Shift;
import com.gooddelivery.delivery.model.Token;
import com.gooddelivery.delivery.model.Otp;
import com.gooddelivery.delivery.model.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {


    @GET("api/transporter/profile")
    Call<Profile> getProfile(@Header("Authorization") String authorization, @QueryMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/transporter/profile")
    Call<Profile> updateProfile(@Header("Authorization") String authorization, @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/transporter/profile/location")
    Call<Profile> updateLocation(@Header("Authorization") String authorization, @FieldMap HashMap<String, Double> params);

    @Multipart
    @POST("api/transporter/profile")
    Call<Profile> updateProfileWithImage(@Header("Authorization") String authorization, @PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part filename);

    @FormUrlEncoded
    @POST("api/transporter/login")
    Call<Otp> getOtp(@Field("phone") String mobile);

    @GET("api/transporter/logout")
    Call<Message> logout(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("api/transporter/verify/otp")
    Call<Token> postLogin(@FieldMap HashMap<String, String> params);

    @GET("api/transporter/shift/")
    Call<List<Shift>> getShift(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("api/transporter/shift")
    Call<List<Shift>> shiftStart(@Header("Authorization") String authorization, @FieldMap HashMap<String, String> params);

    @DELETE("api/transporter/shift/{id}")
    Call<List<Shift>> shiftEnd(@Header("Authorization") String authorization, @Path("id") int shiftId);

    @FormUrlEncoded
    @POST("api/transporter/shift/timing")
    Call<List<Shift>> shiftBreakStart(@Header("Authorization") String authorization, @FieldMap HashMap<String, String> params);

    @DELETE("api/transporter/shift/timing/{id}")
    Call<List<Shift>> shiftBreakEnd(@Header("Authorization") String authorization, @Path("id") int breakId);

    @GET("api/transporter/vehicles")
    Call<List<Vehicle>> getVehicles(@Header("Authorization") String authorization);

    @GET("api/transporter/order")
    Call<List<Order>> getOrder(@Header("Authorization") String authorization);

    @GET("api/transporter/history")
    Call<List<Order>> getCompletedOrder(@Header("Authorization") String authorization, @Query("type") String type);

    @FormUrlEncoded
    @PATCH("api/transporter/order/{id}")
    Call<Order> updateStatus(@Path("id") int id, @FieldMap HashMap<String, String> params, @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("api/transporter/rating")
    Call<Message> rateUser(@Header("Authorization") String authorization, @FieldMap HashMap<String, String> params);

    @GET("api/transporter/notice")
    Call<List<Notice>> getNoticeBoard(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("api/transporter/request/order")
    Call<Order> acceptRequest(@Header("Authorization") String authorization, @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/transporter/request/order")
    Call<Message> rejectRequest(@Header("Authorization") String authorization, @FieldMap HashMap<String, String> params);
}
