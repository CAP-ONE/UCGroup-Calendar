package fr.ucgroup.calendar.http;


import fr.ucgroup.calendar.models.APIResponse;
import fr.ucgroup.calendar.models.Weather;
import io.reactivex.Single;

public class UCGroupAPIService {

    private static final String BASE_URL = "https://ucgroup-android.herokuapp.com/api/";

    public static UCGroupAPI getAPI() {
        return RetrofitBuilder.build(BASE_URL).create(UCGroupAPI.class);
    }

    public static Single<APIResponse> authenticate(String username, String password) {
        return getAPI().authenticate(username,password);
    }

    public static Single<APIResponse> register(String username, String password, String firstname, String lastname) {
        return getAPI().register(username,password,firstname,lastname);
    }

    public static Single<Weather> weather(String token) {
        return getAPI().weather(token);
    }

}
