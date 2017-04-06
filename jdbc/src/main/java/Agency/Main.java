package Agency;

import AgencyImpl.MissionManagerImpl;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.List;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static DataSource createMemoryDatabase() {
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(EmbeddedDriver.class.getName());
        bds.setUrl("jdbc:derby:memory:database;user=admin;password=secret;create=true");
        new ResourceDatabasePopulator(
                new ClassPathResource("createTables.sql"),
                new ClassPathResource("testData.sql"))
                .execute(bds);
        return bds;
    }

    public static void main(String[] args) {
        log.info("starting");

        DataSource dataSource = createMemoryDatabase();
        MissionManager missionManager = new MissionManagerImpl(dataSource);

        List<Mission> allMissions = missionManager.getAllMissions();
        System.out.println("allMissions = " + allMissions);
    }
}