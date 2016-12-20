package com.bolyartech.forge.server.modules.main;

import com.bolyartech.forge.server.handler.Handler;
import com.bolyartech.forge.server.misc.GzipUtils;
import com.bolyartech.forge.server.response.FileUploadResponse;
import com.bolyartech.forge.server.response.Response;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;


public class FileUploadHandler implements Handler {
    private final boolean mEnableGzip;


    public FileUploadHandler(boolean enableGzip) {
        mEnableGzip = enableGzip;
    }


    @Override
    public Response handle(RequestContext ctx, Session session) throws ResponseException {

        boolean actualEnableGzip = mEnableGzip && GzipUtils.supportsGzip(ctx);
        return new FileUploadResponse("/home/user/somefile.zip", actualEnableGzip);
    }
}
