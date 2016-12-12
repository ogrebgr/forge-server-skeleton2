package com.bolyartech.forge.server.modules.main;

import com.bolyartech.forge.server.Session;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.response.ResponseProducer;
import com.bolyartech.forge.server.misc.TemplateEngine;
import com.bolyartech.forge.server.misc.TemplateEngineFactory;
import com.bolyartech.forge.server.response.HtmlResponse;
import com.bolyartech.forge.server.response.Response;


public class RootRp implements ResponseProducer {
    private final TemplateEngineFactory mTemplateEngineFactory;


    public RootRp(TemplateEngineFactory templateEngineFactory) {
        mTemplateEngineFactory = templateEngineFactory;
    }


    @Override
    public Response produce(RequestContext ctx, Session session) {
        TemplateEngine tple = mTemplateEngineFactory.createNew();

        tple.assign("chudesni", "chudesni be");

        return new HtmlResponse(tple.render("root.vm"));
    }
}