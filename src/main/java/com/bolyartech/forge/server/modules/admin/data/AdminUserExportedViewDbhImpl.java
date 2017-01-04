package com.bolyartech.forge.server.modules.admin.data;

import com.bolyartech.forge.server.db.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AdminUserExportedViewDbhImpl implements AdminUserExportedViewDbh {

    @Override
    public List<AdminUserExportedView> list(Connection dbc, long idGreaterThan, int pageSize) throws SQLException {
        DbUtils.ensureOperationalDbc(dbc);
        if (pageSize < 0) {
            throw new IllegalArgumentException("Invalid limit: " + pageSize);
        }

        List<AdminUserExportedView> ret = new ArrayList<>();


        String sql = "SELECT admin_users.id, admin_users.is_disabled, admin_users.is_super_admin, " +
                "admin_users.name, admin_user_scram.username " +
                "FROM admin_user_scram, admin_users " +
                "WHERE admin_users.id > ? AND admin_user_scram.user = admin_users.id LIMIT ?";

        try (PreparedStatement st = dbc.prepareStatement(sql)) {
            st.setLong(1, idGreaterThan);
            st.setLong(2, pageSize);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                AdminUserExportedView tmp = new AdminUserExportedView(rs.getLong(1),
                        rs.getString(5),
                        rs.getInt(2) == 1,
                        rs.getInt(3) == 1,
                        rs.getString(4)
                        );
                ret.add(tmp);
            }
        }

        return ret;
    }


    @Override
    public List<AdminUserExportedView> findByPattern(Connection dbc, String pattern) throws SQLException {
        if (pattern.length() < 3) {
            throw new IllegalArgumentException("pattern must be at least 3 characters long");
        }

        String sql = "SELECT users.id, " +
                "admin_users.is_disabled, " +
                "admin_users.is_super_admin, " +
                "admin_users.name, " +
                "admin_user_scram.username, " +
                "FROM admin_user_scram, admin_users " +
                "WHERE (admin_user_scram.username LIKE ? OR admin_users.name LIKE ?) " +
                "AND admin_user_scram.user = admin_users.id";

        List<AdminUserExportedView> ret = new ArrayList<>();

        try (PreparedStatement st = dbc.prepareStatement(sql)) {
            String sqlPattern = pattern + "%";
            st.setString(1, sqlPattern);
            st.setString(2, sqlPattern);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                AdminUserExportedView tmp = new AdminUserExportedView(rs.getLong(1),
                        rs.getString(5),
                        rs.getInt(2) == 1,
                        rs.getInt(3) == 1,
                        rs.getString(4));
                ret.add(tmp);
            }
        }

        return ret;
    }
}
