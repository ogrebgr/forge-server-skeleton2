package com.bolyartech.forge.server.modules.user.data;

import com.bolyartech.forge.server.modules.user.data.user_scram.UserScram;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public interface UserDbh {
    User loadById(Connection dbc, long id) throws SQLException;

    User createNew(Connection dbc, boolean isDisabled, UserLoginType lt) throws SQLException;

    boolean changeDisabled(Connection dbc, long id, boolean disabled) throws SQLException;

    User changeLoginType(Connection dbc, User user, UserLoginType lt) throws SQLException;

    boolean exists(Connection dbc, long id) throws SQLException;

    List<UserScram> list(Connection dbc, long idGreaterThan, int limit) throws SQLException;
}
