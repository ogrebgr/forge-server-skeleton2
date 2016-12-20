package com.bolyartech.forge.server.modules.user.data;

import com.google.gson.annotations.SerializedName;


public class SessionInfo {
    @SerializedName("user_id")
    public final long user_id;
    @SerializedName("screen_name")
    public final String screenName;


    public SessionInfo(long userId, String screenName) {
        this.user_id = userId;
        this.screenName = screenName;
    }
}
