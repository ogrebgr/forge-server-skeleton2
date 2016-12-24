package com.bolyartech.forge.server.modules.user.endpoints;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.modules.user.SessionVars;
import com.bolyartech.forge.server.modules.user.data.RokResponseAutoregistration;
import com.bolyartech.forge.server.modules.user.data.User;
import com.bolyartech.forge.server.modules.user.data.UserDbh;
import com.bolyartech.forge.server.modules.user.data.UserLoginType;
import com.bolyartech.forge.server.modules.user.data.scram.Scram;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbh;
import com.bolyartech.forge.server.modules.user.data.user_scram.UserScram;
import com.bolyartech.forge.server.modules.user.data.user_scram.UserScramDbh;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.BasicResponseCodes;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class UserAutoregistrationEpTest {
    @Test
    public void test() throws SQLException, ResponseException {
        DbPool dbPool = mock(DbPool.class);
        UserScramDbh userScramDbh = mock(UserScramDbh.class);

        User user = new User(111, false, UserLoginType.SCRAM);
        Scram scram = new Scram(111, "testuser", "aaa", "aaaa", "aaa", 1);
        UserScram userScram = new UserScram(user, scram);

        when(userScramDbh.createNew(any(), any(), any(), any(), any())).thenReturn(userScram);

        UserDbh userDbh = mock(UserDbh.class);
        ScramDbh scramDbh = mock(ScramDbh.class);

        AutoregistrationEp ep = new AutoregistrationEp(
                dbPool,
                userDbh,
                scramDbh,
                userScramDbh
        );

        RequestContext req = mock(RequestContext.class);

        Session session = mock(Session.class);
        Connection dbc = mock(Connection.class);

        ForgeResponse forgeResp = ep.handle(req, session, dbc);
        assertTrue("Not OK", forgeResp.getResultCode() == BasicResponseCodes.Oks.OK.getCode());

        Gson gson = new Gson();
        RokResponseAutoregistration obj = gson.fromJson(forgeResp.getPayload(), RokResponseAutoregistration.class);
        assertTrue("password not set", !Strings.isNullOrEmpty(obj.password));
        assertTrue("username not set", !Strings.isNullOrEmpty(obj.username));

        verify(session, times(1)).setVar(SessionVars.VAR_USER, user);
    }

}
