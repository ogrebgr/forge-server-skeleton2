package com.bolyartech.forge.server.modules.user.data;

import com.google.gson.annotations.SerializedName;


public class RokLogin {
    @SerializedName("session_ttl")
    public final int sessionTtl;
    @SerializedName("session_info")
    public final SessionInfo sessionInfo;


    public RokLogin(int sessionTtl, SessionInfo sessionInfo) {
        this.sessionTtl = sessionTtl;
        this.sessionInfo = sessionInfo;
    }
}
