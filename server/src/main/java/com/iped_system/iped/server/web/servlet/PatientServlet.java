package com.iped_system.iped.server.web.servlet;

import com.iped_system.iped.server.web.filter.AuthFilter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/11/28.
 */
public class PatientServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String patientId = req.getHeader("X-IPED-PATIENT-ID");
        req.getSession().setAttribute(AuthFilter.PATIENT_ID_KEY, patientId);
    }
}
