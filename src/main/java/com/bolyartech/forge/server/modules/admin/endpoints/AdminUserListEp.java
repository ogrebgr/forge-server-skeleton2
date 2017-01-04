package com.bolyartech.forge.server.modules.admin.endpoints;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.modules.admin.AdminDbEndpoint;
import com.bolyartech.forge.server.modules.admin.data.*;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.BasicResponseCodes;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.response.forge.MissingParametersResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class AdminUserListEp extends AdminDbEndpoint {
    private static final int USERS_PAGE_SIZE = 10;

    private final AdminUserExportedViewDbh mAdminUserExportedViewDbh;

    private final Gson mGson;


    public AdminUserListEp(DbPool dbPool, AdminUserExportedViewDbh adminUserExportedViewDbh) {
        super(dbPool);
        mAdminUserExportedViewDbh = adminUserExportedViewDbh;
        mGson = new Gson();
    }


    @Override
    public ForgeResponse handle(RequestContext ctx, Session session, Connection dbc, AdminUser user)
            throws ResponseException, SQLException {

        String idGreaterThanRaw = ctx.getFromPost("id");
        long id = 0;
        if (!Strings.isNullOrEmpty(idGreaterThanRaw)) {
            try {
                id = Long.parseLong(idGreaterThanRaw);
            } catch (NumberFormatException e) {
                return new ForgeResponse(BasicResponseCodes.Errors.INVALID_PARAMETER_VALUE, "Invalid id: " + id);
            }

        }

        List<AdminUserExportedView> users = mAdminUserExportedViewDbh.list(dbc, id, USERS_PAGE_SIZE);

        return new ForgeResponse(BasicResponseCodes.Oks.OK, mGson.toJson(users));
    }
}
