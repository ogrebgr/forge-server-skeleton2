package com.bolyartech.forge.server;

import com.bolyartech.forge.server.config.ForgeConfigurationException;
import com.bolyartech.forge.server.db.*;
import com.bolyartech.forge.server.module.HttpModule;
import com.bolyartech.forge.server.modules.admin.AdminModule;
import com.bolyartech.forge.server.modules.admin.data.AdminScramDbhImpl;
import com.bolyartech.forge.server.modules.admin.data.AdminUserDbhImpl;
import com.bolyartech.forge.server.modules.admin.data.AdminUserScramDbhImpl;
import com.bolyartech.forge.server.modules.admin.data.UserExportedViewDbhImpl;
import com.bolyartech.forge.server.modules.main.MainModule;
import com.bolyartech.forge.server.modules.user.UserModule;
import com.bolyartech.forge.server.modules.user.data.UserDbhImpl;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbhImpl;
import com.bolyartech.forge.server.modules.user.data.screen_name.ScreenNameDbhImpl;
import com.bolyartech.forge.server.modules.user.data.user_scram.UserScramDbhImpl;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;


public class SkeletonMainServlet extends MainServlet {
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass());

    private DbPool mDbPool;


    @Override
    public void init() throws ServletException {
        mDbPool = createDbPool();

        super.init();
    }


    @Override
    protected List<HttpModule> getModules() {
        List<HttpModule> ret = new ArrayList<>();
        ret.add(new MainModule());
        ret.add(new UserModule(mDbPool,
                new UserScramDbhImpl(),
                new UserDbhImpl(),
                new ScramDbhImpl(),
                new ScreenNameDbhImpl()));
        ret.add(new AdminModule(mDbPool,
                new AdminUserDbhImpl(),
                new ScramDbhImpl(),
                new AdminScramDbhImpl(),
                new UserDbhImpl(),
                new AdminUserScramDbhImpl(),
                new UserExportedViewDbhImpl()
                ));

        return ret;
    }


    private DbPool createDbPool() {
        DbConfigurationLoader dbConfigurationLoader = new DbConfigurationLoaderImpl();
        try {
            DbConfiguration dbConfiguration = dbConfigurationLoader.load(this.getClass().getClassLoader());
            return DbUtils.createComboPooledDataSource(dbConfiguration);
        } catch (ForgeConfigurationException e) {
            mLogger.error("Cannot initialize SkeletonMainServlet", e);
            throw new RuntimeException(e);
        }
    }
}
