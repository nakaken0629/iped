package com.iped_system.iped.server.common.servlet;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/10/12.
 */
public class FaceServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FaceServlet.class.getName());
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.fine("pathInfo = " + req.getPathInfo());
        String faceKey = req.getPathInfo().split("/")[1];
        logger.fine("faceKey = " + faceKey);
        BlobKey blobKey = new BlobKey(faceKey);
        this.blobstoreService.serve(blobKey, resp);
    }
}
