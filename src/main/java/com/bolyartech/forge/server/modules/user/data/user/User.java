package com.bolyartech.forge.server.modules.user.data.user;

import java.util.Objects;


public final class User {
    private final long mId;
    private final boolean mIsDisabled;
    private final UserLoginType mLoginType;


    public User(long id, boolean isDisabled, UserLoginType loginType) {
        mId = id;
        mIsDisabled = isDisabled;
        mLoginType = loginType;
    }


    public long getId() {
        return mId;
    }


    public boolean isDisabled() {
        return mIsDisabled;
    }


    public UserLoginType getLoginType() {
        return mLoginType;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof User) {
            User other = (User) obj;
            return other.getId() == mId && other.isDisabled() == mIsDisabled && other.getLoginType() == mLoginType;
        } else {
            return false;
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(mId, mIsDisabled, mLoginType);
    }
}
