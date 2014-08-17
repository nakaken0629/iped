package com.iped_system.iped.server.api.servlet;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/08/17.
 */
public class ServeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlobstoreService service = BlobstoreServiceFactory.getBlobstoreService();
        BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
        service.serve(blobKey, resp);
    }
}
