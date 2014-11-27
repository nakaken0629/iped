package com.iped_system.iped.server.web.servlet;

import com.iped_system.iped.common.RoleType;
import com.iped_system.iped.common.main.RemarkValue;
import com.iped_system.iped.server.api.filter.AuthInfo;
import com.iped_system.iped.server.domain.RemarkDomain;
import com.iped_system.iped.server.domain.UserDomain;
import com.iped_system.iped.server.domain.model.Remark;
import com.iped_system.iped.server.domain.model.User;
import com.iped_system.iped.server.web.filter.AuthFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/11/07.
 */
public class MeetingServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MeetingServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthInfo authInfo = (AuthInfo) req.getAttribute(AuthFilter.AUTH_INFO_KEY);
        if (authInfo.getRole() == RoleType.PATIENT) {
            resp.sendRedirect("/web/secure/interview");
        }
        String patientId = authInfo.getPatientId();

        RemarkDomain remarkDomain = RemarkDomain.getInstance();
        UserDomain userDomain = UserDomain.getInstance();
        ArrayList<RemarkValue> remarkValues = new ArrayList<RemarkValue>();
        for (Remark remark : remarkDomain.search(patientId)) {
            User user = userDomain.getUser(remark.getUserId());
            RemarkValue value = new RemarkValue();
            value.setId(remark.getId());
            value.setFaceId(user.getFaceId());
            value.setAuthorName(user.getName());
            value.setCreatedAt(remark.getCreatedAt());
            value.setText(remark.getText());
            value.setPictureIdList(remark.getPictureIdList());
            remarkValues.add(value);
        }

        req.setAttribute("remarks", remarkValues);
        req.setAttribute("tokenId", req.getSession().getAttribute(AuthFilter.TOKEN_KEY));
        req.setAttribute("patientId", req.getSession().getAttribute(AuthFilter.PATIENT_ID_KEY));
        req.setAttribute("patients", UserDomain.getInstance().getPatients(authInfo.getPatientIdList()));
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/web/meeting.jsp");
        dispatcher.forward(req, resp);
    }
}
