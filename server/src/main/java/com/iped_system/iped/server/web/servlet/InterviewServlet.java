package com.iped_system.iped.server.web.servlet;

import com.iped_system.iped.common.main.TalkValue;
import com.iped_system.iped.common.main.TalksResponse;
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
        for(Talk talk : talkDomain.search(userId, patientId)) {
            TalkValue talkValue = new TalkValue();
            if (talk.getUserId().equals(userId)) {
                talkValue.setMeText(talk.getText());
            } else {
                User user = userDomain.getByUserId(talk.getUserId());
                talkValue.setFaceKey(user.getFaceKey());
                talkValue.setYouText(talk.getText());
                talkValue.setAuthorName(user.getName());
            }
            talkValue.setCreatedAt(talk.getCreatedAt());
            talks.add(talkValue);
        }

        req.setAttribute("talks", talks);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/web/interview.jsp");
        dispatcher.forward(req, resp);
    }
}
