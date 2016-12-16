package com.bolyartech.forge.server.modules.main;

import com.bolyartech.forge.server.Session;
import com.bolyartech.forge.server.misc.GzipUtils;
import com.bolyartech.forge.server.response.FileUploadResponse;
import com.bolyartech.forge.server.response.Response;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.ResponseProducer;
import com.bolyartech.forge.server.route.RequestContext;


public class FileUploadRp implements ResponseProducer {
    private final boolean mEnableGzip;


    public FileUploadRp(boolean enableGzip) {
        mEnableGzip = enableGzip;
    }


    @Override
    public Response produce(RequestContext ctx, Session session) throws ResponseException {

        boolean actualEnableGzip = mEnableGzip && GzipUtils.supportsGzip(ctx);
        return new FileUploadResponse("/home/user/somefile.zip", actualEnableGzip);
    }
}
