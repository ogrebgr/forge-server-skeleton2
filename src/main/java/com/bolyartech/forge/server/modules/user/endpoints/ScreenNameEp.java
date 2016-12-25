package com.bolyartech.forge.server.modules.user.endpoints;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.modules.user.ForgeUserDbEndpoint;
import com.bolyartech.forge.server.modules.user.UserResponseCodes;
import com.bolyartech.forge.server.modules.user.data.User;
import com.bolyartech.forge.server.modules.user.data.screen_name.ScreenName;
import com.bolyartech.forge.server.modules.user.data.screen_name.ScreenNameDbh;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.response.forge.MissingParametersResponse;
import com.bolyartech.forge.server.response.forge.OkResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;
import com.google.common.base.Strings;

import java.sql.Connection;
import java.sql.SQLException;


public class ScreenNameEp extends ForgeUserDbEndpoint {
    static final String PARAM_SCREEN_NAME = "screen_name";


    private final ScreenNameDbh mScreenNameDbh;


    public ScreenNameEp(DbPool dbPool, ScreenNameDbh screenNameDbh) {
        super(dbPool);
        mScreenNameDbh = screenNameDbh;
    }


    @Override
    public ForgeResponse handle(RequestContext ctx,
                                Session session,
                                Connection dbc, User user) throws ResponseException, SQLException {

        String screenName = ctx.getFromPost(PARAM_SCREEN_NAME);
        if (Strings.isNullOrEmpty(screenName)) {
            return new MissingParametersResponse();
        }

        ScreenName existing = mScreenNameDbh.loadByUser(dbc, user.getId());
        if (existing != null) {
            return new ForgeResponse("Password too short",
                    UserResponseCodes.Errors.SCREEN_NAME_CHANGE_NOT_SUPPORTED.getCode());
        }

        if (!ScreenName.isValid(screenName)) {
            return new ForgeResponse("Invalid screen name", UserResponseCodes.Errors.INVALID_SCREEN_NAME.getCode());
        }

        ScreenName sn = mScreenNameDbh.createNew(dbc, user.getId(), screenName);
        if (sn != null) {
            return new OkResponse();
        } else {
            return new ForgeResponse("Scree name already taken", UserResponseCodes.Errors.SCREEN_NAME_EXISTS.getCode());
        }
    }
}
