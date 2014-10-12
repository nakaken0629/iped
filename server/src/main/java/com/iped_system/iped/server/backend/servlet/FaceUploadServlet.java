package com.iped_system.iped.server.backend.servlet;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.iped_system.iped.server.domain.UserDomain;
import com.iped_system.iped.server.domain.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/10/12.
 */
public class FaceUploadServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        String userId = req.getParameter("userId");
        List<BlobKey> blobKeys = blobs.get("face");

        if (blobKeys != null || blobKeys.size() > 0) {
            UserDomain domain = UserDomain.getInstance();
            User user = domain.getByUserId(userId);
            user.setFaceKey(blobKeys.get(0).getKeyString());
            domain.update(user);

            resp.getWriter().print(user.getFaceKey());
        }
    }
}
