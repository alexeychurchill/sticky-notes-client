package io.github.alexeychurchill.stickynotes.api;

import java.util.List;

import io.github.alexeychurchill.stickynotes.model.LoginResult;
import io.github.alexeychurchill.stickynotes.model.NoteEntry;
import io.github.alexeychurchill.stickynotes.model.NoteFull;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import io.github.alexeychurchill.stickynotes.model.SharedNoteFull;
import io.github.alexeychurchill.stickynotes.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Sticky notes user API
 */


public interface StickyNotesApi {
    // Users
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
    // Notes
    @FormUrlEncoded
    @POST("/note/create")
    Call<ServiceResponse<Object>> noteCreate(@Header("X-AccessToken") String token, @Field("title") String title);
    @GET("/note/{id}")
    Call<ServiceResponse<NoteFull>> noteGet(@Header("X-AccessToken") String token, @Path("id") int id);
    @FormUrlEncoded
    @POST("/note/{id}/update")
    Call<ServiceResponse<Object>> noteUpdate(@Header("X-AccessToken") String token, @Path("id") int id, @Field("text") String text);
    //...edit metadata method...
    @GET("/note/list/{page}")
    Call<ServiceResponse<List<NoteEntry>>> noteGetList(@Header("X-AccessToken") String token, @Path("page") int page);
//    void noteDelete();
    // Shared notes
//    void sharedShare();
//    void sharedUnshare();
    @GET("/shared/list/{page}")
    Call<ServiceResponse<List<NoteEntry>>> sharedList(@Header("X-AccessToken") String token, @Path("page") int page);
//    void sharedToList();
    @GET("/shared/{id}")
    Call<ServiceResponse<SharedNoteFull>> sharedGet(@Header("X-AccessToken") String token, @Path("id") int id);
    @FormUrlEncoded
    @POST("/shared/{id}/update")
    Call<ServiceResponse<Object>> sharedUpdate(@Header("X-AccessToken") String token, @Path("id") int id, @Field("text") String text);
    // Friends API
//    void friendRequest();
//    void friendAcceptRequest();
//    void friendDeleteRequest();
//    void friendGetMyRequests();
//    void friendGetRequests();
    @GET("/friend/list/{page}")
    Call<ServiceResponse<List<User>>> friendGetList(@Header("X-AccessToken") String token, @Path("page") int page);
//    void friendUnfriend();
}
