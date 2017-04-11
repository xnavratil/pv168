package AgencyImpl;

import Agency.Assignment;
import Agency.AssignmentBuilder;
import org.junit.Test;
import javax.sql.DataSource;
import org.junit.rules.ExpectedException;
import org.junit.Before;
import java.sql.SQLException;
import org.apache.derby.jdbc.EmbeddedDataSource;
import Agency.AssignmentManager;
import Exceptions.ValidationException;
import java.time.LocalDate;
import java.util.function.Consumer;
import Utils.DBUtils;
import org.junit.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by pnavratil on 3/15/17.
 */
public class AssignmentManagerImplTest {
    private AssignmentManagerImpl manager;
    private DataSource dataSource;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:assignmentmanager-test");
        ds.setCreateDatabase("create");
        return ds;
    }

    @Before
    public void setUp() throws Exception {
        dataSource = prepareDataSource();
        DBUtils.executeSqlScript(dataSource, AssignmentManager.class.getResource("/createTables.sql"));
        manager = new AssignmentManagerImpl();
        manager.setDataSource(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        // Drop tables after each test
        DBUtils.executeSqlScript(dataSource, AssignmentManager.class.getResource("/dropTables.sql"));
    }

    @Test
    public void createAssign() {
        Assignment firstAssignment = ruthlessAssignmentBuilder().build();
        manager.createAssignment(firstAssignment);

        Long assignId = firstAssignment.getId();
        assertThat(assignId).isNotNull();

        assertThat(manager.findAssignmentById(assignId))
                .isNotSameAs(firstAssignment)
                .isEqualToComparingFieldByField(firstAssignment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullAssign() {
        manager.createAssignment(null);
    }

    @Test
    public void createAssignWithNullAgent() {
        Assignment assignment = ruthlessAssignmentBuilder().agent(null).build();
        expectedException.expect(ValidationException.class);
        manager.createAssignment(assignment);
    }

    @Test
    public void createAssignWithNullMission() {
        Assignment assignment = ruthlessAssignmentBuilder().mission(null).build();
        expectedException.expect(ValidationException.class);
        manager.createAssignment(assignment);
    }

    @Test
    public void createAssignWithNullStart() {
        Assignment assignment = ruthlessAssignmentBuilder().start(null).build();
        expectedException.expect(ValidationException.class);
        manager.createAssignment(assignment);
    }

    @Test
    public void createAssignWithNullExpectedEnd() {
        Assignment assignment = ruthlessAssignmentBuilder().expectedEnd(null).build();
        expectedException.expect(ValidationException.class);
        manager.createAssignment(assignment);
    }

    private void testUpdateAssignment(Consumer<Assignment> updateOperation) {
        Assignment sourceAssignment = ruthlessAssignmentBuilder().build();
        Assignment anotherAssignment = ultraAssignmentBuilder().build();
        manager.createAssignment(sourceAssignment);
        manager.createAssignment(anotherAssignment);

        updateOperation.accept(sourceAssignment);

        manager.updateAssignment(sourceAssignment);
        assertThat(manager.findAssignmentById(sourceAssignment.getId()))
                .isEqualToComparingFieldByField(sourceAssignment);

        assertThat(manager.findAssignmentById(anotherAssignment.getId()))
                .isEqualToComparingFieldByField(anotherAssignment);
    }

    @Test
    public void updateAssignmentAgent() {
        testUpdateAssignment((assignment) -> assignment.setAssignedAgentId(AgentManagerImplTest.ultraAgentBuilder().id(10L).build().getId()));
    }

    @Test
    public void updateAssignmentMission() {
        testUpdateAssignment((assignment) -> assignment.setMissionId(MissionManagerImplTest.ultraMissionBuilder().id(10L).build().getId()));
    }

    @Test
    public void updateAssignmentStart() {
        testUpdateAssignment((assignment) -> assignment.setStart(LocalDate.of(1940, 9, 4)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullAssignment() {
        manager.updateAssignment(null);
    }

    @Test
    public void removeAssignment() throws Exception {
        Assignment m1 = ruthlessAssignmentBuilder().build();
        Assignment m2 = ultraAssignmentBuilder().build();
        manager.createAssignment(m1);
        manager.createAssignment(m2);

        assertThat(manager.findAssignmentById(m1.getId())).isNotNull();
        assertThat(manager.findAssignmentById(m2.getId())).isNotNull();

        manager.removeAssignment(m1);

        assertThat(manager.findAssignmentById(m1.getId())).isNull();
        assertThat(manager.findAssignmentById(m2.getId())).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullAssignment() {
        manager.removeAssignment(null);
    }


    private AssignmentBuilder ruthlessAssignmentBuilder() {
        return new AssignmentBuilder()
                .id(null)
                .agent(AgentManagerImplTest.ruthlessAgentBuilder().id(1L).build().getId())
                .mission(MissionManagerImplTest.ruthlessMissionBuilder().id(1L).build().getId())
                .start(LocalDate.of(1940, 9, 3))
                .expectedEnd(LocalDate.of(1941, 9, 3));
    }

    private AssignmentBuilder ultraAssignmentBuilder() {
        return new AssignmentBuilder()
                .id(null)
                .agent(AgentManagerImplTest.ultraAgentBuilder().id(2L).build().getId())
                .mission(MissionManagerImplTest.ultraMissionBuilder().id(2L).build().getId())
                .start(LocalDate.of(1941, 6, 2))
                .expectedEnd(LocalDate.of(1943, 6, 2));
    }
}