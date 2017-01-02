package com.bolyartech.forge.server.modules.admin;

import com.bolyartech.forge.server.HttpMethod;
import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.module.ForgeModule;
import com.bolyartech.forge.server.modules.admin.data.*;
import com.bolyartech.forge.server.modules.admin.endpoints.*;
import com.bolyartech.forge.server.modules.user.data.UserDbh;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbh;
import com.bolyartech.forge.server.route.PostRoute;
import com.bolyartech.forge.server.route.RouteImpl;
import com.bolyartech.forge.server.route.Route;

import java.util.ArrayList;
import java.util.List;


public class AdminModule implements ForgeModule {
    private static final String MODULE_SYSTEM_NAME = "admin";
    private static final int MODULE_VERSION_CODE = 1;
    private static final String MODULE_VERSION_NAME = "1.0.0";

    private final DbPool mDbPool;
    private final AdminUserDbh mAdminUserDbh;
    private final ScramDbh mUserScramDbh;
    private final ScramDbh mAdminScramDbh;
    private final UserDbh mUserDbh;
    private final AdminUserScramDbh mAdminUserScramDbh;
    private final UserExportedViewDbh mUserExportedViewDbh;


    public AdminModule(DbPool dbPool, AdminUserDbh adminUserDbh, ScramDbh userScramDbh,
                       ScramDbh adminScramDbh, UserDbh userDbh, AdminUserScramDbh adminUserScramDbh,
                       UserExportedViewDbh userExportedViewDbh) {
        mDbPool = dbPool;
        mAdminUserDbh = adminUserDbh;
        mUserScramDbh = userScramDbh;
        mAdminScramDbh = adminScramDbh;
        mUserDbh = userDbh;
        mAdminUserScramDbh = adminUserScramDbh;
        mUserExportedViewDbh = userExportedViewDbh;
    }


    @Override
    public List<Route> createRoutes() {
        List<Route> ret = new ArrayList<>();

        ret.add(new PostRoute("/api/admin/login",
                new LoginEp(mDbPool, mAdminUserDbh, mUserScramDbh)));
        ret.add(new PostRoute("/api/admin/logout",
                new LogoutEp()));
        ret.add(new PostRoute("/api/admin/users",
                new UserListEp(mDbPool, mUserExportedViewDbh)));
        ret.add(new PostRoute("/api/admin/user_find",
                new FindUserEp(mDbPool, mUserExportedViewDbh)));
        ret.add(new PostRoute("/api/admin/user_disable",
                new DisableUserEp(mDbPool, mUserDbh)));
        ret.add(new PostRoute("/api/admin/user_chpwd",
                new ChangePasswordEp(mDbPool, mUserScramDbh)));
        ret.add(new PostRoute("/api/admin/admin_chpwd",
                new ChangeAdminPasswordEp(mDbPool, mAdminScramDbh)));
        ret.add(new PostRoute("/api/admin/chpwd_own",
                new ChangeAdminPasswordEp(mDbPool, mAdminScramDbh)));
        ret.add(new PostRoute("/api/admin/admin_create",
                new CreateAdminUserEp(mDbPool, mAdminUserDbh, mAdminScramDbh, mAdminUserScramDbh)));
        ret.add(new PostRoute("/api/admin/admin_disable",
                new DisableAdminUserEp(mDbPool, mAdminUserDbh)));

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
