package com.iped_system.iped.server.web.servlet;

import com.iped_system.iped.common.main.RemarkValue;
import com.iped_system.iped.server.api.filter.AuthInfo;
import com.iped_system.iped.server.domain.RemarkDomain;
import com.iped_system.iped.server.domain.UserDomain;
import com.iped_system.iped.server.domain.model.Remark;
import com.iped_system.iped.server.domain.model.User;
import com.iped_system.iped.server.web.filter.AuthFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/11/07.
 */
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthInfo authInfo = (AuthInfo) req.getAttribute(AuthFilter.AUTH_INFO_KEY);
        String patientId = authInfo.getPatientId();

        RemarkDomain remarkDomain = RemarkDomain.getInstance();
        UserDomain userDomain = UserDomain.getInstance();
        ArrayList<RemarkValue> remarkValues = new ArrayList<RemarkValue>();
        for(Remark remark : remarkDomain.search(patientId)) {
            User user = userDomain.getByUserId(remark.getUserId());
            RemarkValue value = new RemarkValue();
            value.setId(remark.getId());
            value.setFaceKey(user.getFaceKey());
            value.setAuthorName(user.getName());
            value.setCreatedAt(remark.getCreatedAt());
            value.setText(remark.getText());
            value.setPictures(remark.getPictures());
            remarkValues.add(value);
        }

        req.setAttribute("remarks", remarkValues);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/web/meeting.jsp");
        dispatcher.forward(req, resp);
    }
}
