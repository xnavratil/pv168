package Agency;

import java.time.LocalDate;

/**
 * Created by pnavratil on 3/15/17.
 */
public class MissionBuilder {
    private Long id;
    private String codename;
    private String info;
    private LocalDate issueDate;

    public MissionBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public MissionBuilder codename(String codename) {
        this.codename = codename;
        return this;
    }

    public MissionBuilder info(String info) {
        this.info = info;
        return this;
    }

    public MissionBuilder issueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
        return this;
    }

    /**
     * Creates new instance of {@link Mission} with configured properties.
     *
     * @return new instance of {@link Mission} with configured properties.
     */
    public Mission build() {
        Mission mission = new Mission();
        mission.setId(id);
        mission.setCodename(codename);
        mission.setInfo(info);
        mission.setIssueDate(issueDate);
        return mission;
    }

}
