package com.bolyartech.forge.server.modules.admin.data;

import com.google.gson.annotations.SerializedName;


public class SessionInfoAdmin {
    @SerializedName("user_id")
    public final long user_id;
    @SerializedName("super_admin")
    public final boolean superAdmin;


    public SessionInfoAdmin(long userId, boolean superAdmin) {
        this.user_id = userId;
        this.superAdmin = superAdmin;
    }
}
