package com.bolyartech.forge.server.modules.main;


import com.bolyartech.forge.server.HttpMethod;
import com.bolyartech.forge.server.Session;
import com.bolyartech.forge.server.endpoint.EndpointImpl;
import com.bolyartech.forge.server.endpoint.RequestContext;
import com.bolyartech.forge.server.endpoint.ResponseProducer;
import com.bolyartech.forge.server.misc.TemplateEngine;
import com.bolyartech.forge.server.response.HtmlResponse;
import com.bolyartech.forge.server.response.Response;
import com.bolyartech.forge.server.response.AbstractStringResponse;


public class RootEp extends EndpointImpl {
    public RootEp(TemplateEngine templateEngine) {
        super(HttpMethod.GET, "/presni", new RootRp(templateEngine));
    }


    private static class RootRp implements ResponseProducer {
        private final TemplateEngine mTemplateEngine;


        public RootRp(TemplateEngine templateEngine) {
            mTemplateEngine = templateEngine;
        }


        @Override
        public Response produce(RequestContext ctx, Session session) {
            mTemplateEngine.assign("chudesni", "chudesni be");

            return new HtmlResponse(mTemplateEngine.render("root.vm"));
        }
    }
}
