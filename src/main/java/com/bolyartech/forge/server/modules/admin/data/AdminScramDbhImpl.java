package com.bolyartech.forge.server.modules.admin.data;

import com.bolyartech.forge.server.modules.user.data.scram.ScramDbhImpl;


public class AdminScramDbhImpl extends ScramDbhImpl {
    private static final String USERS_TABLE_NAME = "admin_user_scram";

    @Override
    protected String getTableName() {
        return USERS_TABLE_NAME;
    }
}
