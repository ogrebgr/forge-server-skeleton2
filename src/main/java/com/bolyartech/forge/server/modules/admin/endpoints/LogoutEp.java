package com.bolyartech.forge.server.modules.admin.endpoints;

import com.bolyartech.forge.server.handler.ForgeEndpoint;
import com.bolyartech.forge.server.modules.admin.SessionVars;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.response.forge.OkResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;


public class LogoutEp extends ForgeEndpoint {
    @Override
    public ForgeResponse handleForge(RequestContext ctx, Session session) throws ResponseException {
        session.setVar(SessionVars.VAR_USER, null);
        return new OkResponse();
    }
}
