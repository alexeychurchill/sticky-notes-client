package io.github.alexeychurchill.stickynotes.api;

import java.util.List;

import io.github.alexeychurchill.stickynotes.model.Comment;
import io.github.alexeychurchill.stickynotes.model.FriendRequest;
import io.github.alexeychurchill.stickynotes.model.JsonUser;
import io.github.alexeychurchill.stickynotes.model.OldNoteEntry;
import io.github.alexeychurchill.stickynotes.model.NoteFull;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import io.github.alexeychurchill.stickynotes.model.SharedNoteFull;
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

    //    void userGetById(int id);
//    void userUpdateName(String name);
//    void userUpdateLastName(String lastName);
//    void userUpdateNameLastName(String name, String lastName);
    @FormUrlEncoded
    @POST("/user/search")
    Call<ServiceResponse<List<JsonUser>>> userSearch(@Header("X-AccessToken") String token, @Field("query") String query);

    // Notes
    @FormUrlEncoded
    @POST("/note/create")
    Call<ServiceResponse<Object>> noteCreate(@Header("X-AccessToken") String token, @Field("title") String title);
    @GET("/note/{id}")
    Call<ServiceResponse<NoteFull>> noteGet(@Header("X-AccessToken") String token, @Path("id") int id);
    @FormUrlEncoded
    @POST("/note/{id}/update")
    Call<ServiceResponse<Object>> noteUpdate(@Header("X-AccessToken") String token, @Path("id") int id, @Field("text") String text);
    @FormUrlEncoded
    @POST("/note/{id}/update/metadata")
    Call<ServiceResponse<Object>> noteUpdateMetadata(@Header("X-AccessToken") String token,
                                                     @Path("id") int id,
                                                     @Field("title") String title,
                                                     @Field("subject") String subject);
    @GET("/note/list/{page}")
    Call<ServiceResponse<List<OldNoteEntry>>> noteGetList(@Header("X-AccessToken") String token, @Path("page") int page);
    @POST("/note/{id}/delete")
    Call<ServiceResponse<Object>> noteDelete(@Header("X-AccessToken") String token, @Path("id") int id);

    // Shared notes
    @FormUrlEncoded
    @POST("/shared/share")
    Call<ServiceResponse<Object>> sharedShare(@Header("X-AccessToken") String token,
                                              @Field("note_id") int noteId,
                                              @Field("edit_permission") int editAllowed,
                                              @Field("user_id") int userId);
    @POST("/shared/{note_id}/unshare/{user_id}")
    Call<ServiceResponse<Object>> sharedUnshare(@Header("X-AccessToken") String token,
                                                @Path("note_id") int noteId,
                                                @Path("user_id") int userId);
    @GET("/shared/list/{page}")
    Call<ServiceResponse<List<OldNoteEntry>>> sharedList(@Header("X-AccessToken") String token, @Path("page") int page);
    @GET("/shared/{id}/to/{page}")
    Call<ServiceResponse<List<JsonUser>>> sharedToList(@Header("X-AccessToken") String token,
                                                       @Path("id") int noteId,
                                                       @Path("page") int page);
    @GET("/shared/{id}")
    Call<ServiceResponse<SharedNoteFull>> sharedGet(@Header("X-AccessToken") String token, @Path("id") int id);
    @FormUrlEncoded
    @POST("/shared/{id}/update")
    Call<ServiceResponse<Object>> sharedUpdate(@Header("X-AccessToken") String token, @Path("id") int id, @Field("text") String text);

    // Friends API
    @POST("/friend/request/make/{user_id}")
    Call<ServiceResponse<Object>> friendRequest(@Header("X-AccessToken") String token, @Path("user_id") int userId);
    @POST("/friend/request/{id}/accept")
    Call<ServiceResponse<Object>> friendAcceptRequest(@Header("X-AccessToken") String token, @Path("id") int id);
    @POST("/friend/request/{id}/delete")
    Call<ServiceResponse<Object>> friendDeleteRequest(@Header("X-AccessToken") String token, @Path("id") int id);
    @POST("/friend/{id}/unfriend")
    Call<ServiceResponse<Object>> friendUnfriend(@Header("X-AccessToken") String token, @Path("id") int id);
    @GET("/friend/requests/my/{page}")
    Call<ServiceResponse<List<FriendRequest>>> friendGetMyRequests(@Header("X-AccessToken") String token, @Path("page") int page);
    @GET("/friend/list/{page}")
    Call<ServiceResponse<List<JsonUser>>> friendGetList(@Header("X-AccessToken") String token, @Path("page") int page);
    @GET("/friend/requests/{page}")
    Call<ServiceResponse<List<FriendRequest>>> friendGetRequests(@Header("X-AccessToken") String token, @Path("page") int page);

    // Comments API
    @GET("/comment/list/{note_id}/{page}")
    Call<ServiceResponse<List<Comment>>> commentGetList(@Header("X-AccessToken") String token,
                                                        @Path("note_id") int noteId,
                                                        @Path("page") int page);
    @FormUrlEncoded
    @POST("/comment")
    Call<ServiceResponse<Object>> commentAdd(@Header("X-AccessToken") String token,
                                             @Field("note_id") int noteId,
                                             @Field("text") String text);
    @POST("/comment/{id}/delete")
    Call<ServiceResponse<Object>> commentDelete(@Header("X-AccessToken") String token, @Path("id") int id);
}
