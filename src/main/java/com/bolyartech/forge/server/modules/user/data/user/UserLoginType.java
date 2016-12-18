package com.bolyartech.forge.server.modules.user.data.user;

public enum UserLoginType {
    SCRAM(0),
    GOOGLE(1),
    FACEBOOK(2);

    private final long mCode;


    UserLoginType(long code) {
        mCode = code;
    }


    public long getCode() {
        return mCode;
    }


    public static UserLoginType fromLong(long l) {
        if (l == 0) {
            return UserLoginType.SCRAM;
        }

        if (l == 1) {
            return UserLoginType.GOOGLE;
        }

        if (l == 2) {
            return UserLoginType.FACEBOOK;
        }

        throw new IllegalArgumentException("No such UserLoginType: " + l);
    }
}
