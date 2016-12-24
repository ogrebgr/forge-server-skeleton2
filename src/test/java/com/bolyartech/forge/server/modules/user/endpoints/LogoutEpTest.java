package com.bolyartech.forge.server.modules.user.endpoints;

import com.bolyartech.forge.server.modules.user.SessionVars;
import com.bolyartech.forge.server.modules.user.data.User;
import com.bolyartech.forge.server.modules.user.data.UserLoginType;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;
import com.bolyartech.forge.server.session.TestSession;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


public class LogoutEpTest {
    @Test
    public void test() throws ResponseException {
        LogoutEp ep = new LogoutEp();

        RequestContext rc = mock(RequestContext.class);
        Session session = new TestSession();

        session.setVar(SessionVars.VAR_USER, new User(11, false, UserLoginType.FACEBOOK));
        ep.handleForge(rc, session);

        assertTrue("user session var not cleared", session.getVar(SessionVars.VAR_USER) == null);
    }
}
