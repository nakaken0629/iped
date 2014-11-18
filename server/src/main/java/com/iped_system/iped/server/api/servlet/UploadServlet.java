package com.iped_system.iped.server.api.servlet;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.iped_system.iped.server.domain.PictureDomain;
import com.iped_system.iped.server.domain.model.Picture;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/08/23.
 */
public class UploadServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(UploadServlet.class.getName());

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.fine(req.getRequestURI());
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        BlobKey blobKey = blobs.get("myFile").get(0);
        PictureDomain domain = PictureDomain.getInstance();
        Picture picture = new Picture();
        picture.setBlobKey(blobKey.getKeyString());
        String patientId = req.getPathInfo().split("/")[1];
        picture.setOwner(patientId);
        domain.insert(picture);
        resp.getWriter().write(Long.toString(picture.getId()));
        resp.flushBuffer();
    }
}
