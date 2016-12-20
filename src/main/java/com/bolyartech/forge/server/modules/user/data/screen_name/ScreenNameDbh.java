package com.bolyartech.forge.server.modules.user.data.screen_name;

import java.sql.Connection;
import java.sql.SQLException;


public interface ScreenNameDbh {
    ScreenName createNew(Connection dbc, long user, String screenName) throws SQLException;

    ScreenName loadByUser(Connection dbc, long user) throws SQLException;

    ScreenName change(Connection dbc, ScreenName old, String newName) throws SQLException;

    boolean exists(Connection dbc, String screenName) throws SQLException;
}
