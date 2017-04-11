package cz.fi.muni.web;

import Agency.Agent;
import Agency.AgentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.time.LocalDate;


@WebServlet(AgentsServlet.URL_MAPPING + "/*")
public class AgentsServlet extends HttpServlet {

    private static final String LIST_JSP = "/list.jsp";
    public static final String URL_MAPPING = "/agents";

    private final static Logger log = LoggerFactory.getLogger(AgentsServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("GET ...");
        showAgentsList(request, response);
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String name = request.getParameter("name");
                LocalDate born;
                LocalDate recruitmentDate;

                try {
                    born = LocalDate.parse(request.getParameter("born"), formatter);
                    recruitmentDate = LocalDate.parse(request.getParameter("recruitmentDate"), formatter);
                } catch (java.time.format.DateTimeParseException ex) {
                    request.setAttribute("chyba", "Date is not in valid format!");
                    log.debug("form data invalid");
                    showAgentsList(request, response);
                    return;
                }

                //form data validity check
                if (name == null || name.length() == 0 || born == null || recruitmentDate == null) {
                    request.setAttribute("chyba", "All fields have to be filled out!");
                    log.debug("form data invalid");
                    showAgentsList(request, response);
                    return;
                }
                //form data processing - storing to database
                Agent agent = new Agent(null, name, born, recruitmentDate);
                getAgentManager().createAgent(agent);
                //redirect-after-POST protects from multiple submission
                log.debug("redirecting after POST");
                response.sendRedirect(request.getContextPath()+URL_MAPPING);
                return;

            case "/delete":
                Long id = Long.valueOf(request.getParameter("id"));
                getAgentManager().removeAgent(getAgentManager().findAgentById(id));
                log.debug("redirecting after POST");
                response.sendRedirect(request.getContextPath()+URL_MAPPING);
                return;

            case "/update":
                //TODO
                return;
            default:
                log.error("Unknown action " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }

    /**
     * Gets BookManager from ServletContext, where it was stored by {@link StartListener}.
     *
     * @return BookManager instance
     */
    private AgentManager getAgentManager() {
        return (AgentManager) getServletContext().getAttribute("agentManager");
    }

    /**
     * Stores the list of books to request attribute "books" and forwards to the JSP to display it.
     */
    private void showAgentsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("showing table of agents");
        request.setAttribute("agents", getAgentManager().getAllAgents());
        request.getRequestDispatcher(LIST_JSP).forward(request, response);
    }

}