package com.iped_system.iped.server.common.servlet;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.iped_system.iped.server.domain.PictureDomain;
import com.iped_system.iped.server.domain.model.Picture;

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
        long faceId = Long.parseLong(req.getPathInfo().split("/")[1]);
        PictureDomain domain = PictureDomain.getInstance();
        Picture picture = domain.get(faceId);
        BlobKey blobKey = new BlobKey(picture.getBlobKey());
        this.blobstoreService.serve(blobKey, resp);
    }
}
