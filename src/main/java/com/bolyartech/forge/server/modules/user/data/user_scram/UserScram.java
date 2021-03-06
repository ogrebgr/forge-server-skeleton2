package com.bolyartech.forge.server.modules.user.data.user_scram;

import com.bolyartech.forge.server.modules.user.data.User;
import com.bolyartech.forge.server.modules.user.data.scram.Scram;


public class UserScram {
    private final User mUser;
    private final Scram mScram;


    public UserScram(User user, Scram scram) {
        mUser = user;
        mScram = scram;
    }


    public User getUser() {
        return mUser;
    }


    public Scram getScram() {
        return mScram;
    }

}
