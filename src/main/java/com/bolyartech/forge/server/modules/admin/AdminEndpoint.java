package com.bolyartech.forge.server.modules.admin;

import com.bolyartech.forge.server.handler.ForgeSecureEndpoint;
import com.bolyartech.forge.server.modules.admin.data.AdminUser;
import com.bolyartech.forge.server.modules.user.UserResponseCodes;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;


abstract public class AdminEndpoint extends ForgeSecureEndpoint implements AdminEndpointInterface {
    @Override
    public ForgeResponse handleSecure(RequestContext ctx, Session session) throws ResponseException {
        AdminUser user = session.getVar(SessionVars.VAR_USER);
        if (user != null) {
            return handle(ctx, session, user);
        } else {
            return new ForgeResponse(UserResponseCodes.Errors.NOT_LOGGED_IN, "Not logged in");
        }
    }
}
