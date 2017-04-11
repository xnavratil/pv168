package Agency;

import AgencyImpl.AgentManagerImpl;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.awt.print.Book;
import java.util.List;

public class Main {

    //final static Logger log = LoggerFactory.getLogger(Main.class);

    public static DataSource createMemoryDatabase() {
        BasicDataSource bds = new BasicDataSource();
        //set JDBC driver and URL
        bds.setDriverClassName(EmbeddedDriver.class.getName());
        bds.setUrl("jdbc:derby:memory:database;user=admin;password=secret;create=true");
        //populate db with tables and data
        new ResourceDatabasePopulator(
                new ClassPathResource("createTables.sql"),
                new ClassPathResource("testData.sql"))
                .execute(bds);
        return bds;
    }

    public static void main(String[] args) {
        //log.info("starting");

        DataSource dataSource = createMemoryDatabase();
        AgentManager agentManager = new AgentManagerImpl(dataSource);

        List<Agent> allAgents = agentManager.getAllAgents();
        System.out.println("allAgents = " + allAgents);
    }
}
