package com.bolyartech.forge.server.modules.user.endpoints;

import com.bolyartech.forge.server.handler.ForgeSimpleEndpoint;
import com.bolyartech.forge.server.modules.user.SessionVars;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.response.forge.OkResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;


public class LogoutEp extends ForgeSimpleEndpoint {
    @Override
    public ForgeResponse handleForge(RequestContext ctx, Session session) throws ResponseException {
        session.setVar(SessionVars.VAR_USER, null);
        return new OkResponse();
    }
}
