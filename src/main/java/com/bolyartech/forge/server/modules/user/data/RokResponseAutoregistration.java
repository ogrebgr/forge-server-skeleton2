package com.bolyartech.forge.server.modules.user.data;

import com.google.gson.annotations.SerializedName;


public class RokResponseAutoregistration {
    public final String username;
    public final String password;
    @SerializedName("session_ttl")
    public final int sessionTtl;
    @SerializedName("session_info")
    public final SessionInfo sessionInfo;


    public RokResponseAutoregistration(String username, String password, int sessionTtl, SessionInfo sessionInfo) {
        this.username = username;
        this.password = password;
        this.sessionTtl = sessionTtl;
        this.sessionInfo = sessionInfo;
    }
}
