package com.bolyartech.forge.server.modules.admin.data;

import com.bolyartech.forge.server.db.DbUtils;
import com.bolyartech.forge.server.modules.user.data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserExportedViewDbhImpl implements UserExportedViewDbh {
    @Override
    public List<UserExportedView> list(Connection dbc, long idGreaterThan, int pageSize) throws SQLException {
        DbUtils.ensureOperationalDbc(dbc);
        if (pageSize < 0) {
            throw new IllegalArgumentException("Invalid limit: " + pageSize);
        }

        List<UserExportedView> ret = new ArrayList<>();


        String sql = "SELECT users.id, users.is_disabled, user_scram.username, user_screen_names.screen_name " +
                "FROM user_scram, users LEFT OUTER JOIN user_screen_names " +
                "ON users.id = user_screen_names.user " +
                "WHERE users.id > ? AND user_scram.user = users.id LIMIT ?";

        try (PreparedStatement st = dbc.prepareStatement(sql)) {
            st.setLong(1, idGreaterThan);
            st.setLong(2, pageSize);
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                UserExportedView tmp = new UserExportedView(rs.getLong(1),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(2) == 1);
                ret.add(tmp);
            }
        }

        return ret;
    }


    @Override
    public List<UserExportedView> findByPattern(Connection dbc, String pattern) throws SQLException {
        String sql = "SELECT users.id, " +
                            "users.is_disabled, " +
                            "user_scram.username, " +
                            "user_screen_names.screen_name " +
                "FROM user_scram, users LEFT OUTER JOIN user_screen_names " +
                "ON users.id = user_screen_names.user " +
                "WHERE (user_scram.username LIKE ? OR user_screen_names.screen_name LIKE ?) " +
                "AND user_scram.user = users.id";

        List<UserExportedView> ret = new ArrayList<>();

        try (PreparedStatement st = dbc.prepareStatement(sql)) {
            String sqlPattern = pattern + "%";
            st.setString(1, sqlPattern);
            st.setString(2, sqlPattern);

            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                UserExportedView tmp = new UserExportedView(rs.getLong(1),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(2) == 1);
                ret.add(tmp);
            }
        }

        return ret;
    }
}
