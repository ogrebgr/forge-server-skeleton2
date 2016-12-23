package com.bolyartech.forge.server.modules.user;

import com.bolyartech.forge.server.modules.user.data.User;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;


public interface ForgeUserEndpointInterface {
    ForgeResponse handle(RequestContext ctx,
                         Session session,
                         User user) throws ResponseException;

}
