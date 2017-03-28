package Agency;

import java.time.LocalDate;

/**
 * Created by pnavratil on 3/24/17.
 */
public class AssignmentBuilder {
    private Long id;
    private Mission mission;
    private Agent assignedAgent;
    private LocalDate start;
    private LocalDate expectedEnd;

    public AssignmentBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public AssignmentBuilder mission(Mission mission) {
        this.mission = mission;
        return this;
    }

    public AssignmentBuilder agent(Agent agent) {
        this.assignedAgent = agent;
        return this;
    }

    public AssignmentBuilder start(LocalDate start) {
        this.start = start;
        return this;
    }

    public AssignmentBuilder expectedEnd(LocalDate expectedEnd) {
        this.expectedEnd = expectedEnd;
        return this;
    }

    /**
     * Creates new instance of {@link Assignment} with configured properties.
     *
     * @return new instance of {@link Assignment} with configured properties.
     */
    public Assignment build() {
        Assignment assignment = new Assignment();
        assignment.setId(id);
        assignment.setMission(mission);
        assignment.setAssignedAgent(assignedAgent);
        assignment.setStart(start);
        assignment.setExpectedEnd(expectedEnd);
        return assignment;
    }

}
