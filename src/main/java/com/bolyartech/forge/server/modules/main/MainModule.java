package com.bolyartech.forge.server.modules.main;

import com.bolyartech.forge.server.endpoint.Endpoint;
import com.bolyartech.forge.server.module.ForgeModule;

import java.util.ArrayList;
import java.util.List;


public final class MainModule implements ForgeModule {
    private static final String MODULE_SYSTEM_NAME = "main";
    private static final int MODULE_VERSION_CODE = 1;
    private static final String MODULE_VERSION_NAME = "1.0.0";



    @Override
    public List<Endpoint> getEndpoints() {
        List<Endpoint> ret = new ArrayList<>();

        ret.add(new RootEp());

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


