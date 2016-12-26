package io.github.alexeychurchill.stickynotes.api;

import io.github.alexeychurchill.stickynotes.model.LoginResult;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Sticky notes user API
 */


public interface StickyNotesApi {
    @FormUrlEncoded
    @POST("/user/register")
    Call<ServiceResponse<Object>> userRegister(@Field("login") String login, @Field("password") String password);
    @FormUrlEncoded
    @POST("/user/login")
    Call<ServiceResponse<LoginResult>> userLogin(@Field("login") String login, @Field("password") String password);
//    void userGetById(int id);
//    void userUpdateName(String name);
//    void userUpdateLastName(String lastName);
//    void userUpdateNameLastName(String name, String lastName);
//    void userSearch(String query);
}
