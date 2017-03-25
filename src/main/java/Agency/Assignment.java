package Agency;

import java.time.LocalDate;

/**
 * Created by Navratil Peter (xnavrat8@mail.muni.cz) on ${DATE}.
 */

public class Assignment {
    private Long id;
    private Mission mission;
    private Agent assignedAgents;
    private LocalDate start;
    private LocalDate expectedEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Agent getAssignedAgents() {
        return assignedAgents;
    }

    public void setAssignedAgents(Agent assignedAgents) {
        this.assignedAgents = assignedAgents;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getExpectedEnd() {
        return expectedEnd;
    }

    public void setExpectedEnd(LocalDate expectedEnd) {
        this.expectedEnd = expectedEnd;
    }
}
