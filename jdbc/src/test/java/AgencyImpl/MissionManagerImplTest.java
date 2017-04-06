package AgencyImpl;

import Agency.Mission;
import Agency.MissionBuilder;
import org.junit.Test;
import javax.sql.DataSource;
import org.junit.rules.ExpectedException;
import org.junit.Before;
import java.sql.SQLException;
import org.apache.derby.jdbc.EmbeddedDataSource;
import Agency.MissionManager;
import Exceptions.ValidationException;
import java.time.LocalDate;
import java.util.function.Consumer;
import Utils.DBUtils;
import org.junit.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by pnavratil on 3/8/17.
 */
public class MissionManagerImplTest {
    private DataSource dataSource;
    private MissionManagerImpl manager;

    @Rule
    // attribute annotated with @Rule annotation must be public :-(
    public ExpectedException expectedException = ExpectedException.none();

    //--------------------------------------------------------------------------
    // Test initialization
    //--------------------------------------------------------------------------

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        // we will use in memory database
        ds.setDatabaseName("memory:missionmanager-test");
        // database is created automatically if it does not exist yet
        ds.setCreateDatabase("create");
        return ds;
    }

    @Before
    public void setUp() throws SQLException {
        dataSource = prepareDataSource();
        DBUtils.executeSqlScript(dataSource, MissionManager.class.getResource("/createTables.sql"));
        manager = new MissionManagerImpl();
        manager.setDataSource(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(dataSource, MissionManager.class.getResource("/dropTables.sql"));
    }

    static MissionBuilder ruthlessMissionBuilder() {
        return new MissionBuilder()
                .id(null)
                .codename("Ruthless")
                .info("Admiralty plan to capture an Enigma machine.")
                .issueDate(LocalDate.of(1940, 9, 2));
    }

    static MissionBuilder ultraMissionBuilder() {
        return new MissionBuilder()
                .id(null)
                .codename("ULTRA")
                .info("Breaking of ENIGMA.")
                .issueDate(LocalDate.of(1941, 6, 1));
    }

    @Test
    public void createMission() {
        Mission firstMission = ruthlessMissionBuilder().build();
        manager.createMission(firstMission);

        Long missionId = firstMission.getId();
        assertThat(missionId).isNotNull();

        assertThat(manager.findMissionById(missionId))
                .isNotSameAs(firstMission)
                .isEqualToComparingFieldByField(firstMission);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullMission() {
        manager.createMission(null);
    }

    @Test
    public void createMissionWithNullCodename() {
        Mission mission = ruthlessMissionBuilder().codename(null).build();
        expectedException.expect(ValidationException.class);
        manager.createMission(mission);
    }

    @Test
    public void createMissionWithNullInfo() {
        Mission mission = ruthlessMissionBuilder().info(null).build();
        expectedException.expect(ValidationException.class);
        manager.createMission(mission);
    }

    @Test
    public void createMissionWithNullIssueDate() {
        Mission mission = ruthlessMissionBuilder().issueDate(null).build();
        expectedException.expect(ValidationException.class);
        manager.createMission(mission);
    }

    private void testUpdateMission(Consumer<Mission> updateOperation) {
        Mission sourceMission = ruthlessMissionBuilder().build();
        Mission anotherMission = ultraMissionBuilder().build();
        manager.createMission(sourceMission);
        manager.createMission(anotherMission);

        updateOperation.accept(sourceMission);

        manager.updateMission(sourceMission);
        assertThat(manager.findMissionById(sourceMission.getId()))
                .isEqualToComparingFieldByField(sourceMission);

        assertThat(manager.findMissionById(anotherMission.getId()))
                .isEqualToComparingFieldByField(anotherMission);
    }

    @Test
    public void updateMissionCodename() {
        testUpdateMission((mission) -> mission.setCodename("RUTHLESS"));
    }

    @Test
    public void updateMissionInfo() {
        testUpdateMission((mission) -> mission.setInfo("Breaking enigma"));
    }

    @Test
    public void updateMissionIssueDate() {
        testUpdateMission((mission) -> mission.setIssueDate(LocalDate.of(1941, 6, 6)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullMission() {
        manager.updateMission(null);
    }
    
    @Test
    public void removeMission() {
        Mission m1 = ruthlessMissionBuilder().build();
        Mission m2 = ultraMissionBuilder().build();
        manager.createMission(m1);
        manager.createMission(m2);

        assertThat(manager.findMissionById(m1.getId())).isNotNull();
        assertThat(manager.findMissionById(m2.getId())).isNotNull();

        manager.removeMission(m1);

        assertThat(manager.findMissionById(m1.getId())).isNull();
        assertThat(manager.findMissionById(m2.getId())).isNotNull();

    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteNullMission() {
        manager.removeMission(null);
    }

}