package Agency;

import java.time.LocalDate;

/**
 * Created by matusmacko on 21.3.17.
 */
public class AgentBuilder {
    private Long id;
    private String name;
    private LocalDate born;
    private LocalDate recruitmentDate;

    public AgentBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public AgentBuilder name(String name) {
        this.name = name;
        return this;
    }

    public AgentBuilder born(LocalDate born) {
        this.born = born;
        return this;
    }

    public AgentBuilder recruitmentDate(LocalDate recruitmentDate) {
        this.recruitmentDate = recruitmentDate;
        return this;
    }

    public Agent build() {
        Agent agent = new Agent();
        agent.setId(id);
        agent.setName(name);
        agent.setBorn(born);
        agent.setRecruitmentDate(recruitmentDate);
        return agent;
    }
}
