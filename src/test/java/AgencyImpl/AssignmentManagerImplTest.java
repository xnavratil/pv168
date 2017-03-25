package AgencyImpl;

import Agency.Assignment;
import Agency.AssignmentBuilder;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by pnavratil on 3/15/17.
 */
public class AssignmentManagerImplTest {
    private AssignManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        manager = new AssignManagerImpl();
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
        manager.createAssignment(assignment);

        assertThat(manager.findAssignmentById(assignment.getId()))
                .isNotNull()
                .isEqualToComparingFieldByField(assignment);
    }

    @Test
    public void createAssignWithNullMission() {
        Assignment assignment = ruthlessAssignmentBuilder().mission(null).build();
        manager.createAssignment(assignment);

        assertThat(manager.findAssignmentById(assignment.getId()))
                .isNotNull()
                .isEqualToComparingFieldByField(assignment);
    }

    @Test
    public void createAssignWithNullStart() {
        Assignment assignment = ruthlessAssignmentBuilder().start(null).build();
        manager.createAssignment(assignment);

        assertThat(manager.findAssignmentById(assignment.getId()))
                .isNotNull()
                .isEqualToComparingFieldByField(assignment);
    }

    @Test
    public void createAssignWithNullExpectedEnd() {
        Assignment assignment = ruthlessAssignmentBuilder().expectedEnd(null).build();
        manager.createAssignment(assignment);

        assertThat(manager.findAssignmentById(assignment.getId()))
                .isNotNull()
                .isEqualToComparingFieldByField(assignment);
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
        testUpdateAssignment((assignment) -> assignment.setAssignedAgents(AgentManagerImplTest.ultraAgentBuilder().build()));
    }

    @Test
    public void updateAssignmentMission() {
        testUpdateAssignment((assignment) -> assignment.setMission(MissionManagerImplTest.ultraMissionBuilder().build()));
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
    private AssignmentBuilder ruthlessAssignmentBuilder() {
        return new AssignmentBuilder()
                .id(null)
                .agent(AgentManagerImplTest.ruthlessAgentBuilder().build())
                .mission(MissionManagerImplTest.ruthlessMissionBuilder().build())
                .start(LocalDate.of(1940, 9, 3))
                .expectedEnd(LocalDate.of(1941, 9, 3));
    }

    private AssignmentBuilder ultraAssignmentBuilder() {
        return new AssignmentBuilder()
                .id(null)
                .agent(AgentManagerImplTest.ultraAgentBuilder().build())
                .mission(MissionManagerImplTest.ultraMissionBuilder().build())
                .start(LocalDate.of(1941, 6, 2))
                .expectedEnd(LocalDate.of(1943, 6, 2));
    }


}