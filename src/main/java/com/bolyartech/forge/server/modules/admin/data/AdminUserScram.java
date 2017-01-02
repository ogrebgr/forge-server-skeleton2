package com.bolyartech.forge.server.modules.admin.data;

import com.bolyartech.forge.server.modules.user.data.scram.Scram;


public class AdminUserScram {
    private final AdminUser mUser;
    private final Scram mScram;


    public AdminUserScram(AdminUser user, Scram scram) {
        mUser = user;
        mScram = scram;
    }


    public AdminUser getUser() {
        return mUser;
    }


    public Scram getScram() {
        return mScram;
    }

}
