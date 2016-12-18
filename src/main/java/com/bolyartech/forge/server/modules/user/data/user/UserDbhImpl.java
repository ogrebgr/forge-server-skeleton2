package com.bolyartech.forge.server.modules.user.data.user;

import org.slf4j.LoggerFactory;

import java.sql.*;


public class UserDbhImpl implements UserDbh {
    private final org.slf4j.Logger sLogger = LoggerFactory.getLogger(this.getClass());


    @Override
    public User loadById(Connection dbc, long id) throws SQLException {
        String sql = "SELECT is_disabled, login_type FROM users WHERE id = ?";

        try (PreparedStatement psLoad = dbc.prepareStatement(sql)) {
            psLoad.setLong(1, id);

            try (ResultSet rs = psLoad.executeQuery()) {
                if (rs.next()) {
                    return new User(id,
                            rs.getInt(1) == 1,
                            UserLoginType.fromLong(rs.getLong(2)));
                } else {
                    return null;
                }
            }
        }
    }


    @Override
    public User createNew(Connection dbc, boolean isDisabled, UserLoginType lt) throws SQLException {

        String sql = "INSERT INTO users (is_disabled, login_type) VALUES (?, ?)";
        try (PreparedStatement psInsert = dbc.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            psInsert.setInt(1, isDisabled ? 1 : 0);
            psInsert.setLong(2, lt.getCode());
            psInsert.executeUpdate();

            ResultSet rs = psInsert.getGeneratedKeys();
            rs.next();

            return new User(rs.getInt(1), isDisabled, lt);
        }
    }


    @Override
    public User changeDisabled(Connection dbc, User user, boolean disabled) throws SQLException {
        if (user.getId() == 0) {
            throw new IllegalArgumentException("User is new, id = 0");
        }

        String sql = "UPDATE users SET is_disabled = ? WHERE id = ?";
        try (PreparedStatement psUpdate = dbc.prepareStatement(sql)) {
            psUpdate.setInt(1, user.isDisabled() ? 1 : 0);
            psUpdate.setLong(2, user.getId());
            psUpdate.executeUpdate();
            return new User(user.getId(), disabled, user.getLoginType());
        }
    }


    @Override
    public User changeLoginType(Connection dbc, User user, UserLoginType lt) throws SQLException {
        if (user.getId() == 0) {
            throw new IllegalArgumentException("User is new, id = 0");
        }

        String sql = "UPDATE users SET login_type = ? WHERE id = ?";
        try (PreparedStatement psUpdate = dbc.prepareStatement(sql)) {
            psUpdate.setLong(1, user.getLoginType().getCode());
            psUpdate.setLong(2, user.getId());
            int updated = psUpdate.executeUpdate();
            if (updated == 1) {
                return new User(user.getId(), user.isDisabled(), lt);
            } else {
                throw new SQLException("Unexpected number of updated records = " + updated);
            }
        }
    }


    @Override
    public boolean exists(Connection dbc, long id) throws SQLException {
        String sql = "SELECT id FROM users WHERE id = ?";

        try (PreparedStatement psLoad = dbc.prepareStatement(sql)) {
            psLoad.setLong(1, id);

            try (ResultSet rs = psLoad.executeQuery()) {
                return rs.next();
            }
        }
    }
}
