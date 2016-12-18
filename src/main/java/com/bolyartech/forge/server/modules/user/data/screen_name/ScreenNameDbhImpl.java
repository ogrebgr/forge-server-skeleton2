package com.bolyartech.forge.server.modules.user.data.screen_name;

import java.sql.*;


public class ScreenNameDbhImpl implements ScreenNameDbh {
    @Override
    public ScreenName createNew(Connection dbc, long user, String screenName) throws SQLException {
        try {
            String sqlLock = "LOCK TABLES user_screen_names WRITE, users READ";
            Statement stLock = dbc.createStatement();
            stLock.execute(sqlLock);

            if (!exists(dbc, screenName)) {
                ScreenName ret = new ScreenName(user, screenName);

                String sql = "INSERT INTO user_screen_names (user, screen_name, screen_name_lc) values (?,?,?)";
                try (PreparedStatement psInsert = dbc.prepareStatement(sql)) {
                    psInsert.setLong(1, user);
                    psInsert.setString(2, screenName);
                    psInsert.setString(3, screenName.toLowerCase());
                    psInsert.executeUpdate();
                }
                return ret;
            } else {
                return null;
            }
        } finally {
            String sqlLock = "UNLOCK TABLES";
            Statement stLock = dbc.createStatement();
            stLock.execute(sqlLock);
        }
    }


    @Override
    public ScreenName loadByUser(Connection dbc, long user) throws SQLException {
        if (user <= 0) {
            throw new IllegalStateException("user <= 0");
        }

        String sql = "SELECT screen_name FROM user_screen_names WHERE user = ?";

        try (PreparedStatement psLoad = dbc.prepareStatement(sql)) {
            psLoad.setLong(1, user);

            try (ResultSet rs = psLoad.executeQuery()) {
                if (rs.next()) {
                    return new ScreenName(user, rs.getString(1));
                } else {
                    return null;
                }
            }
        }
    }


    @Override
    public ScreenName change(Connection dbc, ScreenName old, String newName) throws SQLException {
        ScreenName ret = null;
        try {
            String sqlLock = "LOCK TABLES user_screen_names WRITE";
            Statement stLock = dbc.createStatement();
            stLock.execute(sqlLock);

            if (!exists(dbc, newName)) {
                ret = new ScreenName(old.getUser(), newName);

                String sql = "UPDATE user_screen_names " +
                        "SET screen_name = ?, screen_name_lc = ? " +
                        "WHERE user = ?";
                try (PreparedStatement psUpdate = dbc.prepareStatement(sql)) {
                    psUpdate.setString(1, newName);
                    psUpdate.setString(2, newName.toLowerCase());
                    psUpdate.setLong(3, old.getUser());
                    psUpdate.executeUpdate();
                }
            }
        } finally {
            String sqlLock = "UNLOCK TABLES";
            Statement stLock = dbc.createStatement();
            stLock.execute(sqlLock);
        }

        return ret;
    }


    @Override
    public boolean exists(Connection dbc, String screenName) throws SQLException {
        String sql = "SELECT user FROM user_screen_names WHERE screen_name = ?";
        try (PreparedStatement psLoad = dbc.prepareStatement(sql)) {
            psLoad.setString(1, screenName);

            try (ResultSet rs = psLoad.executeQuery()) {
                return rs.next();
            }
        }
    }
}
