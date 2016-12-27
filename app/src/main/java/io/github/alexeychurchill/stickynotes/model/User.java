package io.github.alexeychurchill.stickynotes.model;

import com.google.gson.annotations.SerializedName;

/**
 * User class
 */

public class User {
    @SerializedName("id")
    private int id;
    @SerializedName("login")
    private String login;
    @SerializedName("name")
    private String name;
    @SerializedName("lastname")
    private String lastName;

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }
}
