package com.iped_system.iped.server.web.servlet;

import com.iped_system.iped.common.main.TalkValue;
import com.iped_system.iped.server.api.filter.AuthInfo;
import com.iped_system.iped.server.domain.TalkDomain;
import com.iped_system.iped.server.domain.UserDomain;
import com.iped_system.iped.server.domain.model.Talk;
import com.iped_system.iped.server.domain.model.User;
import com.iped_system.iped.server.web.filter.AuthFilter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/11/08.
 */
public class InterviewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthInfo authInfo = (AuthInfo) req.getAttribute(AuthFilter.AUTH_INFO_KEY);
        String userId = authInfo.getUserId();
        String patientId = authInfo.getPatientId();
        TalkDomain talkDomain = TalkDomain.getInstance();
        UserDomain userDomain = UserDomain.getInstance();
        ArrayList<TalkValue> talks = new ArrayList<TalkValue>();
        for(Talk talk : talkDomain.search(patientId)) {
            TalkValue talkValue = new TalkValue();
            if (talk.getUserId().equals(userId)) {
                talkValue.setMeText(talk.getText());
                talkValue.setMePictogramKey(talk.getPictogramKey());
            } else {
                User user = userDomain.getUser(talk.getUserId());
                talkValue.setFaceId(user.getFaceId());
                talkValue.setYouText(talk.getText());
                talkValue.setYouPictogramKey(talk.getPictogramKey());
                talkValue.setAuthorName(user.getName());
            }
            talkValue.setCreatedAt(talk.getCreatedAt());
            talks.add(talkValue);
        }

        req.setAttribute("talks", talks);
        req.setAttribute("tokenId", req.getSession().getAttribute(AuthFilter.TOKEN_KEY));
        req.setAttribute("patientId", req.getSession().getAttribute(AuthFilter.PATIENT_ID_KEY));
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/web/interview.jsp");
        dispatcher.forward(req, resp);
    }
}
