package Agency;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Navratil Peter (xnavrat8@mail.muni.cz) on ${DATE}.
 */

public class Agent {
    private Long id;
    private String name;
    private LocalDate born;
    private LocalDate recruitmentDate;

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
}
