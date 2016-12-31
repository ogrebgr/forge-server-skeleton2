package com.bolyartech.forge.server.modules.admin;

import com.bolyartech.forge.server.modules.admin.data.AdminUser;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;

import java.sql.Connection;
import java.sql.SQLException;


public interface AdminDbEndpointInterface {
    ForgeResponse handle(RequestContext ctx,
                         Session session,
                         Connection dbc,
                         AdminUser user) throws ResponseException, SQLException;

}
