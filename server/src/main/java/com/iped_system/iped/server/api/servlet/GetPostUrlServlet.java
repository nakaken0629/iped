package com.iped_system.iped.server.api.servlet;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.utils.SystemProperty;

import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/08/17.
 */
public class GetPostUrlServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlobstoreService service = BlobstoreServiceFactory.getBlobstoreService();
        String postUrl = service.createUploadUrl("/upload");
        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Development) {
            String hostName = InetAddress.getLocalHost().getHostName();
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            postUrl = postUrl.replace(hostName, hostAddress);
        }
        resp.setContentType("text/plain");
        resp.getWriter().write(postUrl);
    }
}
