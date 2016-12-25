package com.bolyartech.forge.server.modules.user.endpoints;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.modules.user.UserResponseCodes;
import com.bolyartech.forge.server.modules.user.data.User;
import com.bolyartech.forge.server.modules.user.data.UserLoginType;
import com.bolyartech.forge.server.modules.user.data.screen_name.ScreenName;
import com.bolyartech.forge.server.modules.user.data.screen_name.ScreenNameDbh;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.BasicResponseCodes;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;
import com.bolyartech.forge.server.session.TestSession;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


public class ScreenNameEpTest {
    @Test
    public void test() throws ResponseException, SQLException {
        DbPool dbPool = mock(DbPool.class);
        User user = new User(11, false, UserLoginType.SCRAM);
        ScreenNameDbh screenNameDbh = mock(ScreenNameDbh.class);

        RequestContext rc = mock(RequestContext.class);
        Session session = new TestSession();
        Connection dbc = mock(Connection.class);

        ScreenNameEp ep = new ScreenNameEp(dbPool, screenNameDbh);
        ForgeResponse resp = ep.handle(rc, session, dbc, user);
        assertTrue("Unexpected code", resp.getResultCode() == BasicResponseCodes.Errors.MISSING_PARAMETERS.getCode());

        when(rc.getFromPost(ScreenNameEp.PARAM_SCREEN_NAME)).thenReturn("new screen name");
        when(screenNameDbh.loadByUser(any(), anyLong())).thenReturn(new ScreenName(11, "sn1"));
        resp = ep.handle(rc, session, dbc, user);
        assertTrue("Unexpected code", resp.getResultCode() == UserResponseCodes.Errors.SCREEN_NAME_CHANGE_NOT_SUPPORTED.getCode());


        when(rc.getFromPost(ScreenNameEp.PARAM_SCREEN_NAME)).thenReturn("1invalid");
        when(screenNameDbh.loadByUser(any(), anyLong())).thenReturn(null);
        resp = ep.handle(rc, session, dbc, user);
        assertTrue("Unexpected code", resp.getResultCode() == UserResponseCodes.Errors.INVALID_SCREEN_NAME.getCode());

        verify(screenNameDbh, times(0)).createNew(any(), anyLong(), any());

        when(rc.getFromPost(ScreenNameEp.PARAM_SCREEN_NAME)).thenReturn("screen name");
        when(screenNameDbh.loadByUser(any(), anyLong())).thenReturn(null);
        ep.handle(rc, session, dbc, user);
        verify(screenNameDbh, times(1)).createNew(any(), anyLong(), any());
    }
}
