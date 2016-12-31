package com.bolyartech.forge.server.modules.admin.endpoints;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.misc.Params;
import com.bolyartech.forge.server.modules.admin.AdminDbEndpoint;
import com.bolyartech.forge.server.modules.admin.data.AdminUser;
import com.bolyartech.forge.server.modules.user.data.User;
import com.bolyartech.forge.server.modules.user.data.UserDbh;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.BasicResponseCodes;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.response.forge.MissingParametersResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;

import java.sql.Connection;
import java.sql.SQLException;


public class DisableUserEp extends AdminDbEndpoint {
    static final int ERROR_USER_NOT_FOUND = -100;

    static final String PARAM_USER = "user";
    static final String PARAM_DISABLE = "disable";

    private final UserDbh mUserDbh;


    public DisableUserEp(DbPool dbPool, UserDbh userDbh) {
        super(dbPool);
        mUserDbh = userDbh;
    }


    @Override
    public ForgeResponse handle(RequestContext ctx, Session session, Connection dbc, AdminUser user)
            throws ResponseException, SQLException {

        String userIdRaw = ctx.getFromPost(PARAM_USER);
        String disableRaw = ctx.getFromPost(PARAM_DISABLE);

        if (Params.areAllPresent(userIdRaw, disableRaw)) {
            try {
                boolean disable = disableRaw.equals("1");

                long userId = Long.parseLong(userIdRaw);

                if (mUserDbh.changeDisabled(dbc, userId, disable)) {
                    return new ForgeResponse(BasicResponseCodes.Oks.OK,
                            "{disabled: " + (disable ? "true" : "false") + "}");
                } else {
                    return new ForgeResponse(ERROR_USER_NOT_FOUND, "ERROR_USER_NOT_FOUND");
                }
            } catch (NumberFormatException e) {
                return new ForgeResponse(BasicResponseCodes.Errors.INVALID_PARAMETER_VALUE, "Invalid id");
            }
        } else {
            return MissingParametersResponse.getInstance();
        }
    }
}
