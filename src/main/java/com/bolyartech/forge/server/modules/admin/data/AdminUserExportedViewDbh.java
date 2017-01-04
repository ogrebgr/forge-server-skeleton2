package com.bolyartech.forge.server.modules.admin.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public interface AdminUserExportedViewDbh {
    List<AdminUserExportedView> list(Connection dbc, long idGreaterThan, int pageSize) throws SQLException;

    List<AdminUserExportedView> findByPattern(Connection dbc, String pattern) throws SQLException;
}
