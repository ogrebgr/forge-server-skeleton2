package com.bolyartech.forge.server.modules.admin.data;

import com.bolyartech.forge.server.modules.user.data.scram.ScramDbh;
import com.bolyartech.scram_sasl.common.ScramUtils;

import java.sql.Connection;
import java.sql.SQLException;


public interface AdminUserScramDbh {
    public AdminUserScram createNew(Connection dbc,
                                    AdminUserDbh adminUserDbh,
                                    ScramDbh scramDbh,
                                    boolean isSuperAdmin,
                                    String name,
                                    String username,
                                    ScramUtils.NewPasswordStringData data) throws SQLException;
}
