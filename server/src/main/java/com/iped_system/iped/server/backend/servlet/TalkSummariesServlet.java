package com.iped_system.iped.server.backend.servlet;

import com.iped_system.iped.server.domain.TalkDomain;
import com.iped_system.iped.server.domain.model.TalkSummary;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 15/06/07.
 */
public class TalkSummariesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doTalkSummaries(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int count = TalkDomain.getInstance().refreshSummaries();
        req.setAttribute("count", count);
        doTalkSummaries(req, resp);
    }

    private void doTalkSummaries(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<TalkSummary> summaries = TalkDomain.getInstance().getSummaries();
        req.setAttribute("summaries", summaries);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/backend/talk-summaries.jsp");
        dispatcher.forward(req, resp);
    }
}
