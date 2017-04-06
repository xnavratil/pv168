package web;

import Agency.Mission;
import Exceptions.MissionException;
import Agency.MissionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet(MissionServlet.URL_MAPPING + "/*")
public class MissionServlet extends HttpServlet {
    private static final String LIST_JSP = "/list.jsp";
    public static final String URL_MAPPING = "/agency";

    private final static Logger log = LoggerFactory.getLogger(MissionServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("GET ...");
        showBooksList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //support non-ASCII characters in form
        request.setCharacterEncoding("utf-8");
        //action specified by pathInfo
        String action = request.getPathInfo();
        log.debug("POST ... {}",action);
        switch (action) {
            case "/add":
                //getting POST parameters from form
                String codename = request.getParameter("codename");
                String info = request.getParameter("info");
                LocalDate date = LocalDate.parse(request.getParameter("issueDate"));
                //form data validity check
                if (codename == null || codename.length() == 0
                        || info == null || info.length() == 0 || date == null) {
                    request.setAttribute("chyba", "Je nutné vyplnit všechny hodnoty !");
                    log.debug("form data invalid");
                    showBooksList(request, response);
                    return;
                }
               /* try {*/
                    Mission mission = new Mission(null, codename, info, date);
                    getMissionManager().createMission(mission);
                    //redirect-after-POST protects from multiple submission
                    log.debug("redirecting after POST");
                    response.sendRedirect(request.getContextPath()+URL_MAPPING);
                    return;
                /*} catch (MissionException e) {
                    log.error("Cannot add book", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }*/
            case "/delete":
                /*try {*/
                    Long id = Long.valueOf(request.getParameter("id"));
                    getMissionManager().removeMission(getMissionManager().findMissionById(id));
                    log.debug("redirecting after POST");
                    response.sendRedirect(request.getContextPath()+URL_MAPPING);
                    return;
                /*} catch (MissionException e) {
                    log.error("Cannot delete book", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }*/
            case "/update":
                //TODO
                return;
            default:
                log.error("Unknown action " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }

    /**
     * Gets MissionManager from ServletContext, where it was stored by {@link StartListener}.
     *
     * @return MissionManager instance
     */
    private MissionManager getMissionManager() {
        return (MissionManager) getServletContext().getAttribute("missionManager");
    }

    /**
     * Stores the list of books to request attribute "mission" and forwards to the JSP to display it.
     */
    private void showBooksList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*try {*/
            log.debug("showing table of mission");
            request.setAttribute("mission", getMissionManager().getAllMissions());
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
        /*} catch (MissionException e) {
            log.error("Cannot show books", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }*/
    }

}