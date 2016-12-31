package com.bolyartech.forge.server.modules.admin.data;

import com.bolyartech.forge.server.modules.user.data.User;

import java.util.Objects;


public final class AdminUser {
    private static final int MIN_PASSWORD_LENGTH = 7;

    private final long mId;
    private final boolean mIsDisabled;
    private final boolean mIsSuperAdmin;
    private final String mName;


    public AdminUser(long id, boolean isDisabled, boolean isSuperAdmin, String name) {
        mId = id;
        mIsDisabled = isDisabled;
        mIsSuperAdmin = isSuperAdmin;
        mName = name;
    }


    public static boolean isValidName(String screenName) {
        return screenName != null && screenName.matches("^[\\p{L}][\\p{L}\\p{N} ]{1,253}[\\p{L}\\p{N}]$");
    }


    public long getId() {
        return mId;
    }


    public boolean isDisabled() {
        return mIsDisabled;
    }


    public boolean isSuperAdmin() {
        return mIsSuperAdmin;
    }


    public String getName() {
        return mName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof AdminUser) {
            AdminUser other = (AdminUser) obj;
            return other.getId() == mId && other.isDisabled() == mIsDisabled &&
                    other.isSuperAdmin() == mIsSuperAdmin && other.getName().equals(mName);
        } else {
            return false;
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(mId, mIsDisabled, mIsSuperAdmin, mName);
    }


    public static boolean isValidPasswordLength(String password) {
        if (password == null) {
            throw new IllegalArgumentException("password is null");
        }

        return password.length() >= MIN_PASSWORD_LENGTH;
    }
}
