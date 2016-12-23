package com.bolyartech.forge.server.modules.user.data.scram;

import com.google.common.base.Strings;

import java.sql.*;


public class ScramDbhImpl implements ScramDbh {

    @Override
    public Scram loadByUser(Connection dbc, long user) throws SQLException {
        if (user <= 0) {
            throw new IllegalStateException("user <= 0");
        }

        String sql = "SELECT username, salt, server_key, stored_key, iterations FROM user_scram WHERE user = ?";
        try (PreparedStatement psLoad = dbc.prepareStatement(sql)) {
            psLoad.setLong(1, user);

            try (ResultSet rs = psLoad.executeQuery()) {
                if (rs.next()) {
                    return new Scram(user,
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getInt(5)
                    );
                } else {
                    return null;
                }
            }

        }
    }


    @Override
    public Scram loadByUsername(Connection dbc, String username) throws SQLException {
        if (Strings.isNullOrEmpty(username)) {
            throw new IllegalStateException("username empty");
        }

        String sql = "SELECT user, salt, server_key, stored_key, iterations FROM user_scram WHERE username = ?";
        try (PreparedStatement psLoad = dbc.prepareStatement(sql)) {
            psLoad.setString(1, username);

            try (ResultSet rs = psLoad.executeQuery()) {
                if (rs.next()) {
                    return new Scram(rs.getLong(1),
                            username,
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getInt(5)
                    );
                } else {
                    return null;
                }
            }

        }
    }


    @Override
    public boolean usernameExists(Connection dbc, String username) throws SQLException {
        if (Strings.isNullOrEmpty(username)) {
            throw new IllegalStateException("username empty");
        }

        String sql = "SELECT user FROM user_scram WHERE username_lc = ?";
        try (PreparedStatement psLoad = dbc.prepareStatement(sql)) {
            psLoad.setString(1, username.toLowerCase());

            try (ResultSet rs = psLoad.executeQuery()) {
                return rs.next();
            }
        }
    }


    @Override
    public Scram createNew(Connection dbc, long user, String username, String salt, String serverKey, String storedKey,
                           int iterations) throws SQLException {

        try {
            String sqlLock = "LOCK TABLES user_scram WRITE";
            Statement stLock = dbc.createStatement();
            stLock.execute(sqlLock);

            if (!usernameExists(dbc, username)) {
                Scram ret = new Scram(user, username, salt, serverKey, storedKey, iterations);

                String sql = "INSERT INTO user_scram " +
                        "(user, username, salt, server_key, stored_key, iterations, username_lc) " +
                        "VALUES (?,?,?,?,?,?,?)";

                try (PreparedStatement psInsert = dbc.prepareStatement(sql)) {
                    psInsert.setLong(1, user);
                    psInsert.setString(2, username);
                    psInsert.setString(3, salt);
                    psInsert.setString(4, serverKey);
                    psInsert.setString(5, storedKey);
                    psInsert.setInt(6, iterations);
                    psInsert.setString(7, username.toLowerCase());
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
    public Scram replace(Connection dbc, long userId, String username, String salt, String serverKey, String storedKey, int iterations) throws SQLException {

        Scram ret = new Scram(userId, username, salt, serverKey,
                storedKey, iterations);

        String sql = "UPDATE user_scram SET " +
                "username = ?, " +
                "salt = ?," +
                "server_key = ?," +
                "stored_key = ?," +
                "iterations = ? " +
                "WHERE user = user";
        try (PreparedStatement psUpdate = dbc.prepareStatement(sql)) {
            psUpdate.setString(1, username);
            psUpdate.setString(2, salt);
            psUpdate.setString(3, serverKey);
            psUpdate.setString(4, storedKey);
            psUpdate.setInt(5, iterations);
            psUpdate.executeUpdate();
        }

        return ret;
    }
}
