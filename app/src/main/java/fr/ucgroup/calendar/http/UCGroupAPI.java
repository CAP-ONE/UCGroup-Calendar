package fr.ucgroup.calendar.http;

import fr.ucgroup.calendar.models.APIResponse;
import fr.ucgroup.calendar.models.Weather;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface UCGroupAPI {

    @GET("register")
    Single<APIResponse> register(@Query("username") String username, @Query("password") String password, @Query("firstname") String firstname, @Query("lastname") String lastname);

    @FormUrlEncoded
    @POST("authenticate")
    Single<APIResponse> authenticate(@Field("username") String username, @Field("password") String password);


    @GET("weather")
    Single<Weather> weather(@Header("x-access-token") String token);

}
