package com.bolyartech.forge.server.modules.user.endpoints;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.handler.ForgeDbSecureEndpoint;
import com.bolyartech.forge.server.misc.Params;
import com.bolyartech.forge.server.modules.user.SessionVars;
import com.bolyartech.forge.server.modules.user.UserResponseCodes;
import com.bolyartech.forge.server.modules.user.data.RokLogin;
import com.bolyartech.forge.server.modules.user.data.SessionInfo;
import com.bolyartech.forge.server.modules.user.data.User;
import com.bolyartech.forge.server.modules.user.data.UserDbh;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbh;
import com.bolyartech.forge.server.modules.user.data.scram.UserScramUtils;
import com.bolyartech.forge.server.modules.user.data.screen_name.ScreenName;
import com.bolyartech.forge.server.modules.user.data.screen_name.ScreenNameDbh;
import com.bolyartech.forge.server.modules.user.data.user_scram.UserScramDbh;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.response.forge.MissingParametersResponse;
import com.bolyartech.forge.server.response.forge.OkResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;
import com.bolyartech.scram_sasl.common.ScramUtils;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;


public class RegistrationEp extends ForgeDbSecureEndpoint {
    static final String PARAM_USERNAME = "username";
    static final String PARAM_PASSWORD = "password";
    static final String PARAM_SCREEN_NAME = "screen_name";


    private final Gson mGson;

    private final UserDbh mUserDbh;
    private final ScramDbh mScramDbh;
    private final UserScramDbh mUserScramDbh;
    private final ScreenNameDbh mScreenNameDbh;


    public RegistrationEp(DbPool dbPool,
                          UserDbh userDbh,
                          ScramDbh scramDbh,
                          UserScramDbh userScramDbh,
                          ScreenNameDbh screenNameDbh) {

        super(dbPool);
        mGson = new Gson();
        mUserDbh = userDbh;
        mScramDbh = scramDbh;
        mUserScramDbh = userScramDbh;
        mScreenNameDbh = screenNameDbh;
    }


    @Override
    public ForgeResponse handle(RequestContext ctx, Session session, Connection dbc) throws ResponseException,
            SQLException {

        String username = ctx.getFromPost(PARAM_USERNAME);
        String password = ctx.getFromPost(PARAM_PASSWORD);
        String screenName = ctx.getFromPost(PARAM_SCREEN_NAME);


        if (!Params.areAllPresent(username, password, screenName)) {
            return new MissingParametersResponse();
        }

        if (!User.isValidUsername(username)) {
            return new ForgeResponse("Invalid username", UserResponseCodes.Errors.INVALID_USERNAME);
        }

        if (!User.isValidPasswordLength(password)) {
            return new ForgeResponse("Password too short", UserResponseCodes.Errors.INVALID_PASSWORD);
        }

        if (!ScreenName.isValid(screenName)) {
            return new ForgeResponse("Invalid screen name", UserResponseCodes.Errors.INVALID_SCREEN_NAME);
        }


        ScramUtils.NewPasswordStringData data = UserScramUtils.createPasswordData(password);


        UserScramDbh.NewNamedResult rez = mUserScramDbh.createNewNamed(dbc, mUserDbh, mScramDbh, mScreenNameDbh,
                username, data, screenName);

        if (rez.isOk) {
            SessionInfo si = new SessionInfo(rez.mUserScram.getUser().getId(), null);

            session.setVar(SessionVars.VAR_USER, rez.mUserScram.getUser());

            return new OkResponse(
                    mGson.toJson(new RokLogin(
                            session.getMaxInactiveInterval(),
                            si
                    )));
        } else if (rez.usernameExist) {
            return new ForgeResponse("Invalid Login", UserResponseCodes.Errors.USERNAME_EXISTS);
        } else {
            return new ForgeResponse("Invalid Login", UserResponseCodes.Errors.SCREEN_NAME_EXISTS);
        }
    }
}
