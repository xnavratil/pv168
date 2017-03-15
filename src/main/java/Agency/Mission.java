package Agency;

import java.time.LocalDate;

/**
 * Created by Navratil Peter (xnavrat8@mail.muni.cz) on ${DATE}.
 */

public class Mission {
    private Long id;
    private String codename;
    private String info;
    private LocalDate issueDate;

/*
    public Mission(String codename, String info, LocalDate issueDate) {
        this.codename = codename;
        this.info = info;
        this.issueDate = issueDate;
    }
*/

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
