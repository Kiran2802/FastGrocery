package apps.developer.fastgrocery.api;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import apps.developer.fastgrocery.model.RestResponse;
import apps.developer.fastgrocery.model.area.AreaResponseData;
import apps.developer.fastgrocery.model.category.CategoryResponseData;
import apps.developer.fastgrocery.model.category.SubcategoryResponseData;
import apps.developer.fastgrocery.model.home.HomeResponseData;
import apps.developer.fastgrocery.model.login_model.LoginResponseData;
import apps.developer.fastgrocery.model.notification.NotificationResponseData;
import apps.developer.fastgrocery.model.notification.ReadNotification;
import apps.developer.fastgrocery.model.orderHistroy.OrderHistoryResponseData;
import apps.developer.fastgrocery.model.orderHistroy.OrderProductResponseData;
import apps.developer.fastgrocery.model.product.ItemDataResponseData;
import apps.developer.fastgrocery.model.register.RegisterResponse;
import apps.developer.fastgrocery.model.timeslot.TimeslotResponseData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface BaseApiService {



    @POST(RetrofitClient.Append_URL + "login.php")
    Call<LoginResponseData> getLogin(@Body JsonObject body);

    @POST(RetrofitClient.Append_URL + "register.php")
    Call<RegisterResponse> getRegister(@Body JsonObject object);

    @POST(RetrofitClient.Append_URL + "area.php")
    Call<AreaResponseData> getArea(@Body JsonObject jsonObject);

    @POST(RetrofitClient.Append_URL + "pinmatch.php")
    Call<RestResponse> getPinmatch(@Body JsonObject object);

    @POST(RetrofitClient.Append_URL + "forgot.php")
    Call<RestResponse> getForgot(@Body JsonObject object);

    @POST(RetrofitClient.Append_URL + "home.php")
    Call<HomeResponseData> getHome(@Body JsonObject object);

    @POST(RetrofitClient.Append_URL + "cat.php")
    Call<CategoryResponseData> getCat(@Body JsonObject object);

    @POST(RetrofitClient.Append_URL + "subcategory.php")
    Call<SubcategoryResponseData> getSubcategory(@Body JsonObject object);

    @POST(RetrofitClient.Append_URL + "product.php")
    Call<ItemDataResponseData> getGetProduct(@Body JsonObject object);

    @POST(RetrofitClient.Append_URL + "timeslot.php")
    Call<TimeslotResponseData> getTimeslot(@Body JsonObject jsonObject);

    @POST(RetrofitClient.Append_URL + "getcharge.php")
    Call<LoginResponseData> getdcharges(@Body JsonObject jsonObject);

    @POST(RetrofitClient.Append_URL + "history.php")
    Call<OrderHistoryResponseData> getHistroy(@Body JsonObject jsonObject);

    @POST(RetrofitClient.Append_URL + "plist.php")
    Call<OrderProductResponseData> getOHistroy(@Body JsonObject object);

    @POST(RetrofitClient.Append_URL + "order.php")
    Call<RestResponse> getOrder(@Body JsonObject object);

    @POST(RetrofitClient.Append_URL + "profile.php")
    Call<LoginResponseData> getUpdateProfile(@Body JsonObject object);

    @POST(RetrofitClient.Append_URL + "noti.php")
    Call<NotificationResponseData> getNotification(@Body JsonObject jsonObject);

    @POST(RetrofitClient.Append_URL + "n_read.php")
    Call<ReadNotification> getReadNotification(@Body JsonObject jsonObject);

    @POST(RetrofitClient.Append_URL + "feed.php")
    Call<JsonObject> SenfFeedback(@Body JsonObject object);

    @POST(RetrofitClient.Append_URL + "ocancle.php")
    Call<RestResponse> getOCancel(@Body JsonObject object);

    @POST(RetrofitClient.Append_URL + "search.php")
    Call<ItemDataResponseData> getSearch(@Body JsonObject object);
}
