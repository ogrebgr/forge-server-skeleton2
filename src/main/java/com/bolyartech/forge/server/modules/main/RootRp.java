package com.bolyartech.forge.server.modules.main;

import com.bolyartech.forge.server.HttpMethod;
import com.bolyartech.forge.server.Session;
import com.bolyartech.forge.server.misc.DownloadUtils;
import com.bolyartech.forge.server.misc.TemplateEngine;
import com.bolyartech.forge.server.misc.TemplateEngineFactory;
import com.bolyartech.forge.server.response.WebPage;
import com.bolyartech.forge.server.route.RequestContext;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;


public class RootRp extends WebPage {
    public RootRp(TemplateEngineFactory templateEngineFactory) {
        super(templateEngineFactory);
    }


    public RootRp(TemplateEngineFactory templateEngineFactory, boolean enableGzipSupport) {
        super(templateEngineFactory, enableGzipSupport);
    }


    @Override
    public String produceHtml(RequestContext ctx, Session session, TemplateEngine tple) {
        tple.assign("chudesni", "chudesni be, ej");

        return tple.render("root.vm");
    }
}