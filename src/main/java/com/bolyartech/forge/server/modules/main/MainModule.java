package com.bolyartech.forge.server.modules.main;

import com.bolyartech.forge.server.HttpMethod;
import com.bolyartech.forge.server.handler.StaticFileHandler;
import com.bolyartech.forge.server.misc.MimeTypeResolver;
import com.bolyartech.forge.server.misc.MimeTypeResolverImpl;
import com.bolyartech.forge.server.misc.TemplateEngineFactory;
import com.bolyartech.forge.server.module.HttpModule;
import com.bolyartech.forge.server.route.Route;
import com.bolyartech.forge.server.route.RouteImpl;
import com.bolyartech.forge.server.tple.velocity.VelocityTemplateEngineFactory;

import java.util.ArrayList;
import java.util.List;


public final class MainModule implements HttpModule {
    private static final String MODULE_SYSTEM_NAME = "main";
    private static final int MODULE_VERSION_CODE = 1;
    private static final String MODULE_VERSION_NAME = "1.0.0";

    private final TemplateEngineFactory mTpleFactory;


    public MainModule() {
        mTpleFactory = new VelocityTemplateEngineFactory("templates/modules/main/");
    }


    public List<Route> createRoutes() {
        List<Route> ret = new ArrayList<>();

        NotFoundResponse notFoundResponse = new NotFoundResponse();
        MimeTypeResolver mimeTypeResolver = new MimeTypeResolverImpl();

        ret.add(new RouteImpl(HttpMethod.GET, "/presni", new RootWp(mTpleFactory, true)));
        ret.add(new RouteImpl(HttpMethod.POST, "/presni", new RootWp(mTpleFactory, true)));
        ret.add(new RouteImpl(HttpMethod.GET, "/css", new StaticFileHandler("/static", notFoundResponse, mimeTypeResolver, true)));
        ret.add(new RouteImpl(HttpMethod.GET, "/upload", new FileUploadHandler(true)));

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


