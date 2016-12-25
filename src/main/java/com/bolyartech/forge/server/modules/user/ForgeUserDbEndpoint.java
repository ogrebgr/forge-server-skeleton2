package com.bolyartech.forge.server.modules.user;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.handler.ForgeDbSecureEndpoint;
import com.bolyartech.forge.server.modules.user.data.User;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;

import java.sql.Connection;
import java.sql.SQLException;


abstract public class ForgeUserDbEndpoint extends ForgeDbSecureEndpoint implements ForgeUserDbEndpointInterface {


    public ForgeUserDbEndpoint(DbPool dbPool) {
        super(dbPool);
    }


    @Override
    public ForgeResponse handle(RequestContext ctx, Session session, Connection dbc)
            throws ResponseException, SQLException {
        User user = session.getVar(SessionVars.VAR_USER);
        if (user != null) {
            return handle(ctx, session, dbc, user);
        } else {
            return new ForgeResponse("Not logged in", UserResponseCodes.Errors.NOT_LOGGED_IN);
        }
    }
}
