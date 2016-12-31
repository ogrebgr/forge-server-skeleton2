package com.bolyartech.forge.server.modules.admin.data;

import com.bolyartech.forge.server.db.DbUtils;

import java.sql.*;


public class AdminUserDbhImpl implements AdminUserDbh {
    @Override
    public AdminUser loadById(Connection dbc, long id) throws SQLException {
        DbUtils.ensureOperationalDbc(dbc);

        String sql = "SELECT is_disabled, is_super_admin, name FROM admin_users WHERE id = ?";

        try (PreparedStatement psLoad = dbc.prepareStatement(sql)) {
            psLoad.setLong(1, id);

            try (ResultSet rs = psLoad.executeQuery()) {
                if (rs.next()) {
                    return new AdminUser(id,
                            rs.getInt(1) == 1,
                            rs.getInt(2) == 1,
                            rs.getString(3));
                } else {
                    return null;
                }
            }
        }
    }


    @Override
    public AdminUser createNew(Connection dbc, boolean isSuperAdmin, String name) throws SQLException {
        if (!AdminUser.isValidName(name)) {
            throw new IllegalArgumentException("Invalid name: " + name);
        }

        String sql = "INSERT INTO admin_users (is_disabled, is_super_admin, name) VALUES (0, ?, ?)";

        try (PreparedStatement psInsert = dbc.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            psInsert.setInt(1, isSuperAdmin ? 1 : 0);
            psInsert.setString(2, name);
            psInsert.executeUpdate();

            ResultSet rs = psInsert.getGeneratedKeys();
            rs.next();

            return new AdminUser(rs.getInt(1), false, isSuperAdmin, name);
        }
    }


    @Override
    public boolean changeName(Connection dbc, AdminUser user, String newName) throws SQLException {
        if (!AdminUser.isValidName(newName)) {
            throw new IllegalArgumentException("Invalid name: " + newName);
        }

        String sql = "UPDATE admin_users SET name = ? WHERE id = ?";
        try (PreparedStatement psUpdate = dbc.prepareStatement(sql)) {
            psUpdate.setString(1, newName);
            psUpdate.setLong(2, user.getId());
            psUpdate.executeUpdate();
            int count = psUpdate.executeUpdate();

            return count == 1;
        }
    }


    @Override
    public boolean changeDisabled(Connection dbc, long userId, boolean isDisabled) throws SQLException {

        String sql = "UPDATE admin_users SET is_disabled = ? WHERE id = ?";
        try (PreparedStatement psUpdate = dbc.prepareStatement(sql)) {
            psUpdate.setInt(1, isDisabled ? 1 : 0);
            psUpdate.setLong(2, userId);
            int count = psUpdate.executeUpdate();

            return count == 1;
        }
    }


    @Override
    public AdminUser changeSuperAdmin(Connection dbc, AdminUser user, boolean isSuperAdmin) throws SQLException {
        String sql = "UPDATE admin_users SET is_super_admin = ? WHERE id = ?";
        try (PreparedStatement psUpdate = dbc.prepareStatement(sql)) {
            psUpdate.setInt(1, isSuperAdmin ? 1 : 0);
            psUpdate.setLong(2, user.getId());
            psUpdate.executeUpdate();
            return new AdminUser(user.getId(), user.isDisabled(), isSuperAdmin, user.getName());
        }
    }
}
