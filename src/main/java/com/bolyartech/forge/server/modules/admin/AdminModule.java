package com.bolyartech.forge.server.modules.admin;

import com.bolyartech.forge.server.HttpMethod;
import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.module.ForgeModule;
import com.bolyartech.forge.server.modules.admin.data.AdminScramDbhImpl;
import com.bolyartech.forge.server.modules.admin.data.AdminUserDbh;
import com.bolyartech.forge.server.modules.admin.data.AdminUserScramDbh;
import com.bolyartech.forge.server.modules.admin.data.AdminUserScramDbhImpl;
import com.bolyartech.forge.server.modules.admin.endpoints.LoginEp;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbh;
import com.bolyartech.forge.server.route.Route;
import com.bolyartech.forge.server.route.RouteImpl;

import java.util.ArrayList;
import java.util.List;


public class AdminModule implements ForgeModule {
    private static final String MODULE_SYSTEM_NAME = "admin";
    private static final int MODULE_VERSION_CODE = 1;
    private static final String MODULE_VERSION_NAME = "1.0.0";

    private final DbPool mDbPool;
    private final AdminUserDbh mAdminUserDbh;
    private final ScramDbh mScramDbh;
    private final AdminUserScramDbh mAdminUserScramDbh;


    public AdminModule(DbPool dbPool, AdminUserDbh adminUserDbh, ScramDbh scramDbh, AdminUserScramDbhImpl adminUserScramDbh ) {
        mDbPool = dbPool;
        mAdminUserDbh = adminUserDbh;
        mScramDbh = scramDbh;
        mAdminUserScramDbh = adminUserScramDbh;
    }


    @Override
    public List<Route> createRoutes() {
        List<Route> ret = new ArrayList<>();

        ret.add(new RouteImpl(HttpMethod.POST, "/api/admin/login",
                new LoginEp(mDbPool, mAdminUserDbh, mScramDbh)));

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
