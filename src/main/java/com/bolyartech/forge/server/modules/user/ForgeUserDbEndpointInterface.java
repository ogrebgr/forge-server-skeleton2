package com.bolyartech.forge.server.modules.user;

import com.bolyartech.forge.server.modules.user.data.User;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;

import java.sql.Connection;
import java.sql.SQLException;


public interface ForgeUserDbEndpointInterface {
    ForgeResponse handle(RequestContext ctx,
                         Session session,
                         Connection dbc,
                         User user) throws ResponseException, SQLException;

}
