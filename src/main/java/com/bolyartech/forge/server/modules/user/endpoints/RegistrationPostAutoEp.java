package com.bolyartech.forge.server.modules.user.endpoints;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.misc.Params;
import com.bolyartech.forge.server.modules.user.ForgeUserDbEndpoint;
import com.bolyartech.forge.server.modules.user.UserResponseCodes;
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
import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;


public class RegistrationPostAutoEp extends ForgeUserDbEndpoint {
    static final String PARAM_NEW_USERNAME = "new_username";
    static final String PARAM_NEW_PASSWORD = "new_password";
    static final String PARAM_SCREEN_NAME = "screen_name";

    private final Gson mGson;

    private final UserDbh mUserDbh;
    private final ScramDbh mScramDbh;
    private final UserScramDbh mUserScramDbh;
    private final ScreenNameDbh mScreenNameDbh;


    public RegistrationPostAutoEp(DbPool dbPool,
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
    public ForgeResponse handle(RequestContext ctx,
                                Session session,
                                Connection dbc,
                                User user) throws ResponseException, SQLException {

        String newUsername = ctx.getFromPost(PARAM_NEW_USERNAME);
        String newPassword = ctx.getFromPost(PARAM_NEW_PASSWORD);
        String screenName = ctx.getFromPost(PARAM_SCREEN_NAME);

        if (!Params.areAllPresent(newUsername, newPassword)) {
            return new MissingParametersResponse();
        }

        ScreenName existingScreenName = mScreenNameDbh.loadByUser(dbc, user.getId());
        if (existingScreenName == null) {
            if (Strings.isNullOrEmpty(screenName)) {
                return new MissingParametersResponse("missing screen name");
            } else if (!ScreenName.isValid(screenName)) {
                return new ForgeResponse("Invalid screen name", UserResponseCodes.Errors.INVALID_SCREEN_NAME.getCode());
            }
        }


        if (!User.isValidUsername(newUsername)) {
            return new ForgeResponse("Invalid username", UserResponseCodes.Errors.INVALID_USERNAME.getCode());
        }

        if (!User.isValidPasswordLength(newPassword)) {
            return new ForgeResponse("Password too short", UserResponseCodes.Errors.INVALID_PASSWORD.getCode());
        }


        ScramUtils.NewPasswordStringData data = UserScramUtils.createPasswordData(newPassword);

        boolean rez;
        if (existingScreenName == null) {
            rez = mUserScramDbh.replaceExisting(dbc, mScramDbh, mScreenNameDbh,
                    user.getId(), newUsername, data, screenName);
        } else {
            mUserScramDbh.replaceExistingNamed(dbc, mScramDbh,
                    user.getId(), newUsername, data);
            rez = true;
        }

        if (rez) {
            return new OkResponse();
        } else {
            return new ForgeResponse("Scree name already taken", UserResponseCodes.Errors.SCREEN_NAME_EXISTS.getCode());
        }
    }


}
