package com.bolyartech.forge.server.modules.admin;

import com.bolyartech.forge.server.modules.admin.data.AdminUser;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;


public interface AdminEndpointInterface {
    ForgeResponse handle(RequestContext ctx,
                         Session session,
                         AdminUser user) throws ResponseException;
}
