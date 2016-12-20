package com.bolyartech.forge.server.modules.user.data.user;

import com.bolyartech.forge.server.config.ForgeConfigurationException;
import com.bolyartech.forge.server.db.*;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbh;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbhImpl;
import com.bolyartech.forge.server.modules.user.data.user_scram.UserScram;
import com.bolyartech.forge.server.modules.user.data.user_scram.UserScramDbh;
import com.bolyartech.forge.server.modules.user.data.user_scram.UserScramDbhImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;


@SuppressWarnings("ConstantConditions")
public class ScramUserDbhImplTest {
    private DbPool mDbPool;

    @Before
    public void setup() throws SQLException, ForgeConfigurationException {
        if (mDbPool == null) {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("db.conf").getFile());

            DbConfigurationLoader loader = new DbConfigurationLoaderImpl();
            DbConfiguration dbConf = loader.load(this.getClass().getClassLoader());

            mDbPool = DbUtils.createComboPooledDataSource(dbConf);
        }

        Connection dbc = mDbPool.getConnection();
        DbTools.deleteAllScrams(dbc);
        DbTools.deleteAllUsers(dbc);
        dbc.close();
    }


    @Test
    public void testCreateNew() throws SQLException {
        ScramDbh scramDbh = new ScramDbhImpl();
        UserDbh userDbh = new UserDbhImpl();

        UserScramDbh dbh = new UserScramDbhImpl();

        Connection dbc = mDbPool.getConnection();
        UserScram scrNew = dbh.createNew(dbc, userDbh, scramDbh, "username", "salt", "server_key", "stored_key", 11);
        dbc.close();

        assertTrue("user not set", scrNew.getUser() != null);
        assertTrue("scram not set", scrNew.getScram() != null);
    }
}
