package com.bolyartech.forge.server.modules.admin.data;

import com.google.gson.annotations.SerializedName;


public final class UserExportedView {
    @SerializedName("id")
    public final long mId;
    @SerializedName("username")
    private final String mUsername;
    @SerializedName("screen_name")
    private final String mScreenName;
    @SerializedName("disabled")
    private final boolean mIsDisabled;


    public UserExportedView(long id, String username, String screenName, boolean isDisabled) {
        mId = id;
        mUsername = username;
        mScreenName = screenName;
        mIsDisabled = isDisabled;
    }
}
