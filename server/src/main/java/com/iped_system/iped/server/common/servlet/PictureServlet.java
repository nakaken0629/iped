package com.iped_system.iped.server.common.servlet;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.iped_system.iped.server.api.filter.AuthInfo;
import com.iped_system.iped.server.common.filter.BaseAuthFilter;
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
public class PictureServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(PictureServlet.class.getName());
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] info = req.getPathInfo().split("/");
        String patientId = info[1];
        long pictureId = Long.parseLong(info[2]);
        AuthInfo authInfo = (AuthInfo) req.getAttribute(BaseAuthFilter.AUTH_INFO_KEY);
        logger.fine("patient's Id = " + patientId + ", authInfo's patientId = " + authInfo.getPatientId());
        if(!authInfo.getPatientId().equals(patientId)) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        PictureDomain domain = PictureDomain.getInstance();
        Picture picture = domain.get(pictureId);
        if (!picture.getOwner().equals(patientId)) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        BlobKey blobKey = new BlobKey(picture.getBlobKey());
        this.blobstoreService.serve(blobKey, resp);
    }
}
