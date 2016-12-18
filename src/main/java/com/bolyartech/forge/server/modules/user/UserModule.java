package com.bolyartech.forge.server.modules.user;

import com.bolyartech.forge.server.module.ForgeModule;
import com.bolyartech.forge.server.route.Route;

import java.util.List;


public final class UserModule implements ForgeModule {
    private static final String MODULE_SYSTEM_NAME = "user";
    private static final int MODULE_VERSION_CODE = 1;
    private static final String MODULE_VERSION_NAME = "1.0.0";


    @Override
    public List<Route> createRoutes() {
        return null;
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
