package com.bolyartech.forge.server.modules.admin.data;

import java.sql.Connection;
import java.sql.SQLException;


public interface AdminUserDbh {
    AdminUser loadById(Connection dbc, long id) throws SQLException;

    AdminUser createNew(Connection dbc, boolean isSuperAdmin, String name) throws SQLException;

    boolean changeName(Connection dbc, AdminUser user, String newName) throws SQLException;

    boolean changeDisabled(Connection dbc, long userId, boolean isDisabled) throws SQLException;

    AdminUser changeSuperAdmin(Connection dbc, AdminUser user, boolean isSuperAdmin) throws SQLException;
}
