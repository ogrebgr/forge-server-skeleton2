package com.bolyartech.forge.server.modules.user.endpoints;

import com.bolyartech.forge.server.modules.user.data.screen_name.ScreenName;
import com.bolyartech.forge.server.modules.user.data.screen_name.ScreenNameDbh;
import com.bolyartech.forge.server.session.Session;
import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.modules.user.data.scram.Scram;
import com.bolyartech.forge.server.modules.user.data.scram.ScramDbh;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.BasicResponseCodes;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.TestSession;
import com.bolyartech.scram_sasl.client.ScramClientFunctionality;
import com.bolyartech.scram_sasl.client.ScramClientFunctionalityImpl;
import com.bolyartech.scram_sasl.common.ScramException;
import com.bolyartech.scram_sasl.common.ScramUtils;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class LoginHandlerTest {
    @Test
    public void test() throws SQLException, ScramException, InvalidKeyException, NoSuchAlgorithmException, ResponseException {
        RequestContext req = mock(RequestContext.class);
        Connection dbc = mock(Connection.class);

        String username = "testuser";
        String password = "testpwd";

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[24];
        random.nextBytes(salt);

        ScramUtils.NewPasswordStringData data = ScramUtils.byteArrayToStringData(
                ScramUtils.newPassword(password,
                        salt,
                        Scram.DEFAULT_ITERATIONS,
                        LoginEp.HMAC,
                        LoginEp.DIGEST)
        );

        ScramClientFunctionality clientScram = new ScramClientFunctionalityImpl(LoginEp.DIGEST,
                LoginEp.HMAC);
        String clientFirst = clientScram.prepareFirstMessage(username);

        DbPool dbPool = mock(DbPool.class);

        ScramDbh scramDbh = mock(ScramDbh.class);
        when(scramDbh.loadByUsername(dbc, username)).thenReturn(new Scram(111,
                username,
                data.salt,
                data.serverKey,
                data.storedKey,
                data.iterations));

        when(req.getFromPost(LoginEp.PARAM_STEP)).thenReturn("1");
        when(req.getFromPost(LoginEp.PARAM_DATA)).thenReturn(clientFirst);

        Session session = new TestSession();

        LoginEp ep = new LoginEp(dbPool, scramDbh, mock(ScreenNameDbh.class));

        ForgeResponse forgeResp = ep.handle(req, session, dbc);
        assertTrue("Not OK", forgeResp.getResultCode() == BasicResponseCodes.Oks.OK.getCode());

        String clientFinal = clientScram.prepareFinalMessage(password, forgeResp.getPayload());
        when(req.getFromPost(LoginEp.PARAM_STEP)).thenReturn("2");
        when(req.getFromPost(LoginEp.PARAM_DATA)).thenReturn(clientFinal);


        ForgeResponse forgeResp2 = ep.handle(req, session, dbc);
        assertTrue("Not OK", forgeResp2.getResultCode() == BasicResponseCodes.Oks.OK.getCode());

        assertTrue("invalid final server message", clientScram.checkServerFinalMessage(forgeResp2.getPayload()));
    }
}
