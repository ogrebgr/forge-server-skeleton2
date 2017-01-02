package com.bolyartech.forge.server.modules.admin;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.module.HttpModule;
import com.bolyartech.forge.server.modules.admin.data.AdminUserDbh;
import com.bolyartech.forge.server.modules.admin.data.AdminUserScramDbh;
import com.bolyartech.forge.server.modules.admin.data.UserExportedViewDbh;
import com.bolyartech.forge.server.modules.admin.endpoints.*;
import com.bolyartech.forge.server.modules.user.data.UserDbh;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbh;
import com.bolyartech.forge.server.route.PostRoute;
import com.bolyartech.forge.server.route.Route;

import java.util.ArrayList;
import java.util.List;


public class AdminModule implements HttpModule {
    private static final String DEFAULT_PATH_PREFIX = "/api/admin/";

    private static final String MODULE_SYSTEM_NAME = "admin";
    private static final int MODULE_VERSION_CODE = 1;
    private static final String MODULE_VERSION_NAME = "1.0.0";

    private final String mPathPrefix;
    private final DbPool mDbPool;
    private final AdminUserDbh mAdminUserDbh;
    private final ScramDbh mUserScramDbh;
    private final ScramDbh mAdminScramDbh;
    private final UserDbh mUserDbh;
    private final AdminUserScramDbh mAdminUserScramDbh;
    private final UserExportedViewDbh mUserExportedViewDbh;


    public AdminModule(String pathPrefix,
                       DbPool dbPool,
                       AdminUserDbh adminUserDbh,
                       ScramDbh userScramDbh,
                       ScramDbh adminScramDbh,
                       UserDbh userDbh,
                       AdminUserScramDbh adminUserScramDbh,
                       UserExportedViewDbh userExportedViewDbh) {

        mPathPrefix = pathPrefix;
        mDbPool = dbPool;
        mAdminUserDbh = adminUserDbh;
        mUserScramDbh = userScramDbh;
        mAdminScramDbh = adminScramDbh;
        mUserDbh = userDbh;
        mAdminUserScramDbh = adminUserScramDbh;
        mUserExportedViewDbh = userExportedViewDbh;
    }


    public AdminModule(
            DbPool dbPool,
            AdminUserDbh adminUserDbh,
            ScramDbh userScramDbh,
            ScramDbh adminScramDbh,
            UserDbh userDbh,
            AdminUserScramDbh adminUserScramDbh,
            UserExportedViewDbh userExportedViewDbh) {

        mPathPrefix = DEFAULT_PATH_PREFIX;
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

        ret.add(new PostRoute(mPathPrefix + "login",
                new LoginEp(mDbPool, mAdminUserDbh, mUserScramDbh)));
        ret.add(new PostRoute(mPathPrefix + "logout",
                new LogoutEp()));
        ret.add(new PostRoute(mPathPrefix + "users",
                new UserListEp(mDbPool, mUserExportedViewDbh)));
        ret.add(new PostRoute(mPathPrefix + "user_find",
                new FindUserEp(mDbPool, mUserExportedViewDbh)));
        ret.add(new PostRoute(mPathPrefix + "user_disable",
                new DisableUserEp(mDbPool, mUserDbh)));
        ret.add(new PostRoute(mPathPrefix + "user_chpwd",
                new ChangePasswordEp(mDbPool, mUserScramDbh)));
        ret.add(new PostRoute(mPathPrefix + "admin_chpwd",
                new ChangeAdminPasswordEp(mDbPool, mAdminScramDbh)));
        ret.add(new PostRoute(mPathPrefix + "chpwd_own",
                new ChangeAdminPasswordEp(mDbPool, mAdminScramDbh)));
        ret.add(new PostRoute(mPathPrefix + "admin_create",
                new CreateAdminUserEp(mDbPool, mAdminUserDbh, mAdminScramDbh, mAdminUserScramDbh)));
        ret.add(new PostRoute(mPathPrefix + "admin_disable",
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
