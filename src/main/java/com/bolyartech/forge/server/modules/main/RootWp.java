package com.bolyartech.forge.server.modules.main;

import com.bolyartech.forge.server.handler.WebPage;
import com.bolyartech.forge.server.misc.TemplateEngine;
import com.bolyartech.forge.server.misc.TemplateEngineFactory;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;


public class RootWp extends WebPage {
    public RootWp(TemplateEngineFactory templateEngineFactory) {
        super(templateEngineFactory);
    }


    public RootWp(TemplateEngineFactory templateEngineFactory, boolean enableGzipSupport) {
        super(templateEngineFactory, enableGzipSupport);
    }


    @Override
    public String produceHtml(RequestContext ctx, Session session, TemplateEngine tple) {
        tple.assign("chudesni", "chudesni be, ej");

        return tple.render("root.vm");
    }
}