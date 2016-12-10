package com.bolyartech.forge.server.modules.main;


import com.bolyartech.forge.server.HttpMethod;
import com.bolyartech.forge.server.Session;
import com.bolyartech.forge.server.endpoint.*;
import com.bolyartech.forge.server.response.RedirectResponseImpl;
import com.bolyartech.forge.server.response.Response;
import com.bolyartech.forge.server.response.StringResponseImpl;


public class RootEp extends EndpointImpl {

    public RootEp() {
        super(HttpMethod.GET, "/presni", new RootRp());
    }


    private static class RootRp implements ResponseProducer {
        @Override
        public Response produce(RequestContext ctx, Session session) {
//            return new StringResponseImpl("presni be");
            return new RedirectResponseImpl("/chudesni/be");
        }
    }
}
