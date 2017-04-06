package web;

import Agency.Main;
import AgencyImpl.AgentManagerImpl;
import AgencyImpl.MissionManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by pnavratil on 4/4/17.
 */

@WebListener
public class StartListener implements ServletContextListener {
    final static Logger log = LoggerFactory.getLogger(StartListener.class);

    @Override
    public void contextInitialized(ServletContextEvent ev) {
        log.info("webová aplikace inicializována");
        ServletContext servletContext = ev.getServletContext();
        DataSource dataSource = Main.createMemoryDatabase();
        servletContext.setAttribute("agentManager", new AgentManagerImpl(dataSource));
        servletContext.setAttribute("missionManager", new MissionManagerImpl(dataSource));
        log.info("vytvořeny manažery a uloženy do atributů servletContextu");
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        log.info("aplikace končí");
    }
}