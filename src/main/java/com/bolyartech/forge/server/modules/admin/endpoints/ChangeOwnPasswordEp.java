package com.bolyartech.forge.server.modules.admin.endpoints;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.misc.Params;
import com.bolyartech.forge.server.modules.admin.AdminDbEndpoint;
import com.bolyartech.forge.server.modules.admin.data.AdminUser;
import com.bolyartech.forge.server.modules.user.UserResponseCodes;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbh;
import com.bolyartech.forge.server.modules.user.data.scram.UserScramUtils;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.response.forge.MissingParametersResponse;
import com.bolyartech.forge.server.response.forge.OkResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;
import com.bolyartech.scram_sasl.common.ScramUtils;

import java.sql.Connection;
import java.sql.SQLException;


public class ChangeOwnPasswordEp extends AdminDbEndpoint {
    static final int ERROR_USER_NOT_FOUND = -100;
    static final String PARAM_PASSWORD = "new_password";

    private final ScramDbh mAdminScramDbh;


    public ChangeOwnPasswordEp(DbPool dbPool, ScramDbh adminScramDbh) {
        super(dbPool);
        mAdminScramDbh = adminScramDbh;
    }


    @Override
    public ForgeResponse handle(RequestContext ctx, Session session, Connection dbc, AdminUser user)
            throws ResponseException, SQLException {

        String newPassword = ctx.getFromPost(PARAM_PASSWORD);

        if (Params.areAllPresent(newPassword)) {
            if (!AdminUser.isValidPasswordLength(newPassword)) {
                return new ForgeResponse(UserResponseCodes.Errors.PASSWORD_TOO_SHORT.getCode(), "Invalid screen name");
            }

            ScramUtils.NewPasswordStringData data = UserScramUtils.createPasswordData(newPassword);

            if (mAdminScramDbh.changePassword(dbc, user.getId(), data)) {
                return OkResponse.getInstance();
            } else {
                return new ForgeResponse(ERROR_USER_NOT_FOUND);
            }
        } else {
            return MissingParametersResponse.getInstance();
        }
    }
}
