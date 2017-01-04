package com.bolyartech.forge.server.modules.admin.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public interface UserExportedViewDbh {
    List<UserExportedView> list(Connection dbc, long idGreaterThan, int limit) throws SQLException;

    List<UserExportedView> findByPattern(Connection dbc, String pattern) throws SQLException;
}
