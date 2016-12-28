package com.bolyartech.forge.server.modules.admin.data;

import java.sql.Connection;
import java.sql.SQLException;


public interface AdminUserDbh {
    AdminUser loadById(Connection dbc, long id) throws SQLException;
    AdminUser createNew(Connection dbc, boolean isSuperAdmin, String name) throws SQLException;
    AdminUser changeName(Connection dbc, AdminUser user, String newName) throws SQLException;
    AdminUser changeDisabled(Connection dbc, AdminUser user, boolean isDisabled) throws SQLException;
    AdminUser changeSuperAdmin(Connection dbc, AdminUser user, boolean isSuperAdmin) throws SQLException;
}
