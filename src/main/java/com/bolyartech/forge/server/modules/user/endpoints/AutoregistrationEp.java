package com.bolyartech.forge.server.modules.user.endpoints;

import com.bolyartech.forge.server.Session;
import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.handler.ForgeDbEndpoint;
import com.bolyartech.forge.server.modules.user.SessionVars;
import com.bolyartech.forge.server.modules.user.data.RokResponseAutoregistration;
import com.bolyartech.forge.server.modules.user.data.SessionInfo;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbh;
import com.bolyartech.forge.server.modules.user.data.user.UserDbh;
import com.bolyartech.forge.server.modules.user.data.user_scram.UserScram;
import com.bolyartech.forge.server.modules.user.data.user_scram.UserScramDbh;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.response.forge.OkResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.scram_sasl.common.ScramUtils;
import com.google.gson.Gson;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;


public class AutoregistrationEp extends ForgeDbEndpoint {
    static final int SCRAM_ITERATIONS = 4096;
    private final Gson mGson;
    private final UserScramDbh mUserScramDbh;
    private final UserDbh mUserDbh;
    private final ScramDbh mScramDbh;


    public AutoregistrationEp(DbPool dbPool, UserScramDbh userScramDbh, UserDbh userDbh, ScramDbh scramDbh) {
        super(dbPool);
        mUserScramDbh = userScramDbh;
        mUserDbh = userDbh;
        mScramDbh = scramDbh;
        mGson = new Gson();
    }


    @Override
    public ForgeResponse handle(RequestContext ctx, Session session, Connection dbc) throws ResponseException,
            SQLException {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[24];
        random.nextBytes(salt);

        String username;
        String password = UUID.randomUUID().toString();
        UserScram us;

        while(true) {
            username = "g"+ UUID.randomUUID().toString().replace("-", "");

            try {
                ScramUtils.NewPasswordStringData data = ScramUtils.byteArrayToStringData(
                        ScramUtils.newPassword(password, salt, SCRAM_ITERATIONS, "HmacSHA512", "SHA-512")
                );

                us = mUserScramDbh.createNew(dbc, mUserDbh, mScramDbh, username, data);
                if (us != null) {
                    break;
                }
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        }

        SessionInfo si = new SessionInfo(us.getUser().getId(), null);

        session.setVar(SessionVars.VAR_USER, us.getUser());

        return new OkResponse(
                mGson.toJson(new RokResponseAutoregistration(username,
                        password,
                        session.getMaxInactiveInterval(),
                        si
                )));
    }
}