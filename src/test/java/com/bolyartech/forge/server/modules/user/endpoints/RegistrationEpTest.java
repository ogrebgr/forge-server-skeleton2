package com.bolyartech.forge.server.modules.user.endpoints;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.modules.user.SessionVars;
import com.bolyartech.forge.server.modules.user.UserResponseCodes;
import com.bolyartech.forge.server.modules.user.data.User;
import com.bolyartech.forge.server.modules.user.data.UserDbh;
import com.bolyartech.forge.server.modules.user.data.UserLoginType;
import com.bolyartech.forge.server.modules.user.data.scram.Scram;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbh;
import com.bolyartech.forge.server.modules.user.data.screen_name.ScreenNameDbh;
import com.bolyartech.forge.server.modules.user.data.user_scram.UserScram;
import com.bolyartech.forge.server.modules.user.data.user_scram.UserScramDbh;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.BasicResponseCodes;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.response.forge.OkResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;
import com.bolyartech.forge.server.session.TestSession;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class RegistrationEpTest {
    @Test
    public void testMissingParameters() throws ResponseException, SQLException {
        RegistrationEp ep = createRegistrationEp();

        RequestContext rc = mock(RequestContext.class);
        Session session = new TestSession();
        Connection dbc = mock(Connection.class);

        when(rc.getFromPost(RegistrationEp.PARAM_USERNAME)).thenReturn(null);
        when(rc.getFromPost(RegistrationEp.PARAM_PASSWORD)).thenReturn("some_password");
        when(rc.getFromPost(RegistrationEp.PARAM_SCREEN_NAME)).thenReturn("some screen name");
        ForgeResponse resp = ep.handle(rc, session, dbc);
        assertTrue("Unexpected code", resp.getResultCode() == BasicResponseCodes.Errors.MISSING_PARAMETERS);

        when(rc.getFromPost(RegistrationEp.PARAM_USERNAME)).thenReturn("username");
        when(rc.getFromPost(RegistrationEp.PARAM_PASSWORD)).thenReturn(null);
        when(rc.getFromPost(RegistrationEp.PARAM_SCREEN_NAME)).thenReturn("some screen name");
        resp = ep.handle(rc, session, dbc);
        assertTrue("Unexpected code", resp.getResultCode() == BasicResponseCodes.Errors.MISSING_PARAMETERS);

        when(rc.getFromPost(RegistrationEp.PARAM_USERNAME)).thenReturn("username");
        when(rc.getFromPost(RegistrationEp.PARAM_PASSWORD)).thenReturn("some_password");
        when(rc.getFromPost(RegistrationEp.PARAM_SCREEN_NAME)).thenReturn(null);
        resp = ep.handle(rc, session, dbc);
        assertTrue("Unexpected code", resp.getResultCode() == BasicResponseCodes.Errors.MISSING_PARAMETERS);
    }


    @Test
    public void testInvalidParameters() throws ResponseException, SQLException {
        RegistrationEp ep = createRegistrationEp();

        RequestContext rc = mock(RequestContext.class);
        Session session = new TestSession();
        Connection dbc = mock(Connection.class);

        when(rc.getFromPost(RegistrationEp.PARAM_USERNAME)).thenReturn("12username");
        when(rc.getFromPost(RegistrationEp.PARAM_PASSWORD)).thenReturn("some_password");
        when(rc.getFromPost(RegistrationEp.PARAM_SCREEN_NAME)).thenReturn("some screen name");
        ForgeResponse resp = ep.handle(rc, session, dbc);
        assertTrue("Unexpected code", resp.getResultCode() == UserResponseCodes.Errors.INVALID_USERNAME);

        when(rc.getFromPost(RegistrationEp.PARAM_USERNAME)).thenReturn("username");
        when(rc.getFromPost(RegistrationEp.PARAM_PASSWORD)).thenReturn("somwod");
        when(rc.getFromPost(RegistrationEp.PARAM_SCREEN_NAME)).thenReturn("some screen name");
        resp = ep.handle(rc, session, dbc);
        assertTrue("Unexpected code", resp.getResultCode() == UserResponseCodes.Errors.INVALID_PASSWORD);

        when(rc.getFromPost(RegistrationEp.PARAM_USERNAME)).thenReturn("username");
        when(rc.getFromPost(RegistrationEp.PARAM_PASSWORD)).thenReturn("some_password");
        when(rc.getFromPost(RegistrationEp.PARAM_SCREEN_NAME)).thenReturn("so");
        resp = ep.handle(rc, session, dbc);
        assertTrue("Unexpected code", resp.getResultCode() == UserResponseCodes.Errors.INVALID_SCREEN_NAME);
    }


    @Test
    public void testUsernameExists() throws SQLException, ResponseException {
        DbPool dbPool = mock(DbPool.class);
        UserDbh userDbh = mock(UserDbh.class);
        ScramDbh scramDbh = mock(ScramDbh.class);
        UserScramDbh userScramDbh = mock(UserScramDbh.class);
        ScreenNameDbh screenNameDbh = mock(ScreenNameDbh.class);

        RegistrationEp ep = new RegistrationEp(dbPool, userDbh, scramDbh, userScramDbh, screenNameDbh);

        RequestContext rc = mock(RequestContext.class);
        Session session = new TestSession();
        Connection dbc = mock(Connection.class);

        when(scramDbh.usernameExists(any(), any())).thenReturn(true);
        when(rc.getFromPost(RegistrationEp.PARAM_USERNAME)).thenReturn("username");
        when(rc.getFromPost(RegistrationEp.PARAM_PASSWORD)).thenReturn("some_password");
        when(rc.getFromPost(RegistrationEp.PARAM_SCREEN_NAME)).thenReturn("some screen name");

        UserScramDbh.NewNamedResult newNamedResult = new UserScramDbh.NewNamedResult(false, null, true);
        when(userScramDbh.createNewNamed(any(), any(), any(), any(), any(), any(), any())).thenReturn(newNamedResult);

        ForgeResponse resp = ep.handle(rc, session, dbc);
        assertTrue("Unexpected code", resp.getResultCode() == UserResponseCodes.Errors.USERNAME_EXISTS);
    }


    @Test
    public void testScreenNameExists() throws SQLException, ResponseException {
        DbPool dbPool = mock(DbPool.class);
        UserDbh userDbh = mock(UserDbh.class);
        ScramDbh scramDbh = mock(ScramDbh.class);
        UserScramDbh userScramDbh = mock(UserScramDbh.class);
        ScreenNameDbh screenNameDbh = mock(ScreenNameDbh.class);

        RegistrationEp ep = new RegistrationEp(dbPool, userDbh, scramDbh, userScramDbh, screenNameDbh);

        RequestContext rc = mock(RequestContext.class);
        Session session = new TestSession();
        Connection dbc = mock(Connection.class);

        when(scramDbh.usernameExists(any(), any())).thenReturn(false);
        when(rc.getFromPost(RegistrationEp.PARAM_USERNAME)).thenReturn("username");
        when(rc.getFromPost(RegistrationEp.PARAM_PASSWORD)).thenReturn("some_password");
        when(rc.getFromPost(RegistrationEp.PARAM_SCREEN_NAME)).thenReturn("some screen name");
        when(screenNameDbh.exists(any(), any())).thenReturn(true);

        UserScramDbh.NewNamedResult newNamedResult = new UserScramDbh.NewNamedResult(false, null, false);
        when(userScramDbh.createNewNamed(any(), any(), any(), any(), any(), any(), any())).thenReturn(newNamedResult);

        ForgeResponse resp = ep.handle(rc, session, dbc);
        assertTrue("Unexpected code", resp.getResultCode() == UserResponseCodes.Errors.SCREEN_NAME_EXISTS);
    }


    @Test
    public void testOk() throws SQLException, ResponseException {
        DbPool dbPool = mock(DbPool.class);
        UserDbh userDbh = mock(UserDbh.class);
        ScramDbh scramDbh = mock(ScramDbh.class);
        UserScramDbh userScramDbh = mock(UserScramDbh.class);
        ScreenNameDbh screenNameDbh = mock(ScreenNameDbh.class);

        RegistrationEp ep = new RegistrationEp(dbPool, userDbh, scramDbh, userScramDbh, screenNameDbh);

        RequestContext rc = mock(RequestContext.class);
        Session session = new TestSession();
        Connection dbc = mock(Connection.class);

        when(scramDbh.usernameExists(any(), any())).thenReturn(false);
        when(rc.getFromPost(RegistrationEp.PARAM_USERNAME)).thenReturn("username");
        when(rc.getFromPost(RegistrationEp.PARAM_PASSWORD)).thenReturn("some_password");
        when(rc.getFromPost(RegistrationEp.PARAM_SCREEN_NAME)).thenReturn("some screen name");
        when(screenNameDbh.exists(any(), any())).thenReturn(true);

        UserScram us = new UserScram(new User(11, false, UserLoginType.SCRAM), new Scram(11, "aaa", "aaa", "aaa",
                "aaa", 11));

        UserScramDbh.NewNamedResult newNamedResult = new UserScramDbh.NewNamedResult(true, us, false);
        when(userScramDbh.createNewNamed(any(), any(), any(), any(), any(), any(), any())).thenReturn(newNamedResult);

        ForgeResponse resp = ep.handle(rc, session, dbc);
        assertTrue("Unexpected response", resp instanceof OkResponse);

        User user = session.getVar(SessionVars.VAR_USER);
        assertTrue("User not set", user != null);
        assertTrue("Unexpected user", user.getId() == 11);
    }


    private RegistrationEp createRegistrationEp() {
        DbPool dbPool = mock(DbPool.class);
        UserDbh userDbh = mock(UserDbh.class);
        ScramDbh scramDbh = mock(ScramDbh.class);
        UserScramDbh userScramDbh = mock(UserScramDbh.class);
        ScreenNameDbh screenNameDbh = mock(ScreenNameDbh.class);

        return new RegistrationEp(dbPool, userDbh, scramDbh, userScramDbh, screenNameDbh);
    }
}
