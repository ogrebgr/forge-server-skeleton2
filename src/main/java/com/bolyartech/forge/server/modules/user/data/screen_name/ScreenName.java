package com.bolyartech.forge.server.modules.user.data.screen_name;

import com.google.common.base.Strings;

import java.util.Objects;


public class ScreenName {
    private final long mUser;
    private final String mScreenName;


    public ScreenName(long user, String screenName) {
        if (user <= 0) {
            throw new IllegalArgumentException("user <= 0");
        }

        if (Strings.isNullOrEmpty(screenName)) {
            throw new IllegalArgumentException("screen name null or empty");
        }

        if (!isValid(screenName)) {
            throw new IllegalArgumentException("invalid screen name");
        }

        mUser = user;
        mScreenName = screenName;
    }


    public static boolean isValid(String screenName) {
        return screenName != null && screenName.matches("^[\\p{L}][\\p{L}\\p{N} ]{1,33}[\\p{L}\\p{N}]$");
    }


    public long getUser() {
        return mUser;
    }


    public String getScreenName() {
        return mScreenName;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ScreenName) {
            ScreenName other = (ScreenName) obj;

            return other.mUser == mUser && other.getScreenName().equals(mScreenName);
        } else {
            return false;
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(mUser, mScreenName);
    }
}
