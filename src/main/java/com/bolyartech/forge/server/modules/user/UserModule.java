package com.bolyartech.forge.server.modules.user;

import com.bolyartech.forge.server.HttpMethod;
import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.module.ForgeModule;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbh;
import com.bolyartech.forge.server.modules.user.data.user.UserDbh;
import com.bolyartech.forge.server.modules.user.data.user_scram.UserScramDbh;
import com.bolyartech.forge.server.modules.user.endpoints.AutoregistrationEp;
import com.bolyartech.forge.server.modules.user.endpoints.LoginEp;
import com.bolyartech.forge.server.route.Route;
import com.bolyartech.forge.server.route.RouteImpl;

import java.util.ArrayList;
import java.util.List;


public final class UserModule implements ForgeModule {
    private static final String MODULE_SYSTEM_NAME = "user";
    private static final int MODULE_VERSION_CODE = 1;
    private static final String MODULE_VERSION_NAME = "1.0.0";

    private final DbPool mDbPool;
    private final UserScramDbh mUserScramDbh;
    private final UserDbh mUserDbh;
    private final ScramDbh mScramDbh;


    public UserModule(
            DbPool dbPool,
            UserScramDbh userScramDbh,
            UserDbh userDbh,
            ScramDbh scramDbh) {

        mDbPool = dbPool;
        mUserScramDbh = userScramDbh;
        mUserDbh = userDbh;
        mScramDbh = scramDbh;
    }


    @Override
    public List<Route> createRoutes() {
        List<Route> ret = new ArrayList<>();

        ret.add(new RouteImpl(HttpMethod.POST, "/api/user/autoregister",
                new AutoregistrationEp(mDbPool, mUserScramDbh, mUserDbh, mScramDbh)));
        ret.add(new RouteImpl(HttpMethod.POST, "/api/user/login",
                new LoginEp(mDbPool, mScramDbh)));

        return ret;
    }


    @Override
    public String getSystemName() {
        return MODULE_SYSTEM_NAME;
    }


    @Override
    public String getShortDescription() {
        return "";
    }


    @Override
    public int getVersionCode() {
        return MODULE_VERSION_CODE;
    }


    @Override
    public String getVersionName() {
        return MODULE_VERSION_NAME;
    }
}
