package com.bolyartech.forge.server.modules.main;

import com.bolyartech.forge.server.response.Response;
import com.bolyartech.forge.server.response.ResponseException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class NotFoundResponse implements Response {

    @Override
    public void toServletResponse(HttpServletResponse resp) throws ResponseException {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        PrintWriter pw;
        try {
            pw = resp.getWriter();
            pw.print("Not found");
            pw.flush();
            pw.close();
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }
}
