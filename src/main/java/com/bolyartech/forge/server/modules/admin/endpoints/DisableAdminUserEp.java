package com.bolyartech.forge.server.modules.admin.endpoints;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.misc.Params;
import com.bolyartech.forge.server.modules.admin.AdminDbEndpoint;
import com.bolyartech.forge.server.modules.admin.data.AdminUser;
import com.bolyartech.forge.server.modules.admin.data.AdminUserDbh;
import com.bolyartech.forge.server.modules.user.UserResponseCodes;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.BasicResponseCodes;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.response.forge.MissingParametersResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;

import java.sql.Connection;
import java.sql.SQLException;


public class DisableAdminUserEp extends AdminDbEndpoint {
    static final String PARAM_USER = "user";
    static final String PARAM_DISABLE = "disable";

    static final int ERROR_USER_NOT_FOUND = -100;
    static final int ERROR_CANNOT_DISABLE_YOURSELF = -101;


    private final AdminUserDbh mAdminUserDbh;


    public DisableAdminUserEp(DbPool dbPool, AdminUserDbh adminUserDbh) {
        super(dbPool);
        mAdminUserDbh = adminUserDbh;
    }


    @Override
    public ForgeResponse handle(RequestContext ctx, Session session, Connection dbc, AdminUser user)
            throws ResponseException, SQLException {

        if (user.isSuperAdmin()) {
            String userIdRaw = ctx.getFromPost(PARAM_USER);
            String disableRaw = ctx.getFromPost(PARAM_DISABLE);

            if (Params.areAllPresent(userIdRaw, disableRaw)) {
                try {
                    boolean disable = disableRaw.equals("1");

                    long userId = Long.parseLong(userIdRaw);
                    if (userId == user.getId()) {
                        return new ForgeResponse(ERROR_CANNOT_DISABLE_YOURSELF, "ERROR_CANNOT_DISABLE_YOURSELF");
                    }

                    if (mAdminUserDbh.changeDisabled(dbc, userId, disable)) {
                        return new ForgeResponse(BasicResponseCodes.Oks.OK,
                                "{disabled: " + (disable ? "true" : "false") + "}");
                    } else {
                        return new ForgeResponse(ERROR_USER_NOT_FOUND, "ERROR_USER_NOT_FOUND");
                    }
                } catch (NumberFormatException e) {
                    return new ForgeResponse(BasicResponseCodes.Errors.INVALID_PARAMETER_VALUE.getCode(), "Invalid id");
                }
            } else {
                return MissingParametersResponse.getInstance();
            }
        } else {
            return new ForgeResponse(UserResponseCodes.Errors.NO_ENOUGH_PRIVILEGES, "No enough privileges");
        }
    }
}
