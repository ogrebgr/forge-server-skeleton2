package com.bolyartech.forge.server;

import com.bolyartech.forge.server.module.ForgeModule;
import com.bolyartech.forge.server.modules.main.MainModule;

import java.util.ArrayList;
import java.util.List;


public class SkeletonMainServlet extends MainServlet {
    @Override
    protected List<ForgeModule> getModules() {
        List<ForgeModule> ret = new ArrayList<>();
        ret.add(new MainModule());

//        ServletContext context = getServletContext();
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        URL resourceUrl = classLoader.getResource("velocity.properties");
//
//        String relativeWebPath = "/";
//        String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
//        int i = 1;
//        i++;

        return ret;
    }
}
