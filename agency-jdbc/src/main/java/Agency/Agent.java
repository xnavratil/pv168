package Agency;

import java.time.LocalDate;


public class Agent {
    private Long id;
    private String name;
    private LocalDate born;
    private LocalDate recruitmentDate;

    public Agent() {}

    public Agent(Long id, String name, LocalDate born, LocalDate recruitmentDate) {
        this.id = id;
        this.name = name;
        this.born = born;
        this.recruitmentDate = recruitmentDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBorn() {
        return born;
    }

    public LocalDate getRecruitmentDate() {
        return recruitmentDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBorn(LocalDate born) {
        this.born = born;
    }

    public void setRecruitmentDate(LocalDate recruitmentDate) {
        this.recruitmentDate = recruitmentDate;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", born=" + born +
                ", recruitmentDate=" + recruitmentDate +
                '}';
    }
}