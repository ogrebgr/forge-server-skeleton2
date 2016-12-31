package com.bolyartech.forge.server.modules.admin;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.handler.ForgeDbSecureEndpoint;
import com.bolyartech.forge.server.modules.admin.data.AdminUser;
import com.bolyartech.forge.server.modules.user.UserResponseCodes;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;

import java.sql.Connection;
import java.sql.SQLException;


abstract public class AdminDbEndpoint extends ForgeDbSecureEndpoint implements AdminDbEndpointInterface {

    public AdminDbEndpoint(DbPool dbPool) {
        super(dbPool);
    }


    @Override
    public ForgeResponse handle(RequestContext ctx, Session session, Connection dbc)
            throws ResponseException, SQLException {
        AdminUser user = session.getVar(SessionVars.VAR_USER);
        if (user != null) {
            return handle(ctx, session, dbc, user);
        } else {
            return new ForgeResponse(UserResponseCodes.Errors.NOT_LOGGED_IN, "Not logged in");
        }
    }
}
