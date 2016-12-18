package com.bolyartech.forge.server.modules.user.data.user_scram;

import com.bolyartech.forge.server.modules.user.data.scram.Scram;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbh;
import com.bolyartech.forge.server.modules.user.data.user.User;
import com.bolyartech.forge.server.modules.user.data.user.UserDbh;
import com.bolyartech.forge.server.modules.user.data.user.UserLoginType;
import com.bolyartech.scram_sasl.common.ScramUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class UserScramDbhImpl implements UserScramDbh {

    /**
     *
     * @param dbc
     * @param userDbh
     * @param scramDbh
     * @param username
     * @param salt
     * @param serverKey
     * @param storedKey
     * @param iterations
     * @return UserScram on success, and null if username is taken
     * @throws SQLException
     */
    @Override
    public UserScram createNew(Connection dbc,
                               UserDbh userDbh,
                               ScramDbh scramDbh,
                               String username,
                               String salt,
                               String serverKey,
                               String storedKey,
                               int iterations) throws SQLException {

        try {
            String sqlLock = "LOCK TABLES users WRITE, user_scram WRITE";
            Statement stLock = dbc.createStatement();
            stLock.execute(sqlLock);

            if (!scramDbh.usernameExists(dbc, username)) {
                return createNewRaw(dbc, userDbh, scramDbh, username, salt, serverKey, storedKey, iterations);
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
    public UserScram createNew(Connection dbc,
                               UserDbh userDbh,
                               ScramDbh scramDbh,
                               String username,
                               ScramUtils.NewPasswordStringData data) throws SQLException {

        return createNew(dbc, userDbh, scramDbh, username,
                data.salt,
                data.serverKey,
                data.storedKey,
                data.iterations);
    }


    private UserScram createNewRaw(Connection dbc,
                                   UserDbh userDbh,
                                   ScramDbh scramDbh,
                                   String username,
                                   String salt,
                                   String serverKey,
                                   String storedKey,
                                   int iterations) throws SQLException {

        try {
            dbc.setAutoCommit(false);
            User user = userDbh.createNew(dbc, false, UserLoginType.SCRAM);
            Scram scram = scramDbh.createNew(dbc, user.getId(), username, salt, serverKey, storedKey, iterations);
            return new UserScram(user, scram);
        } catch (SQLException e) {
            dbc.rollback();
            return null;
        } finally {
            dbc.commit();
            dbc.setAutoCommit(true);
        }
    }
}
