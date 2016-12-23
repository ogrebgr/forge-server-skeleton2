package com.bolyartech.forge.server.modules.user.data;

import java.sql.Connection;
import java.sql.SQLException;


public interface UserDbh {
    User loadById(Connection dbc, long id) throws SQLException;

    User createNew(Connection dbc, boolean isDisabled, UserLoginType lt) throws SQLException;

    User changeDisabled(Connection dbc, User user, boolean disabled) throws SQLException;

    User changeLoginType(Connection dbc, User user, UserLoginType lt) throws SQLException;

    boolean exists(Connection dbc, long id) throws SQLException;
}
