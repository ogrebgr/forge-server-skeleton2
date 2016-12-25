package com.bolyartech.forge.server.modules.user;

import com.bolyartech.forge.server.handler.ForgeSecureEndpoint;
import com.bolyartech.forge.server.modules.user.data.User;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;


abstract public class ForgeUserEndpoint extends ForgeSecureEndpoint implements ForgeUserEndpointInterface {
    @Override
    public ForgeResponse handleSecure(RequestContext ctx, Session session) throws ResponseException {
        User user = session.getVar(SessionVars.VAR_USER);
        if (user != null) {
            return handle(ctx, session, user);
        } else {
            return new ForgeResponse("Not logged in", UserResponseCodes.Errors.NOT_LOGGED_IN);
        }
    }
}
