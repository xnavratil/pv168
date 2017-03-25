package Agency;

import java.util.List;

/**
 * Created by Navratil Peter (xnavrat8@mail.muni.cz) on ${DATE}.
 */

public interface AssignManager {
    void createAssignment(Assignment newAssignment);
    void updateAssignment(Assignment updatedAssignment);
    void removeAssignment(Assignment removedAssignment);
    Assignment findAssignmentById(long assignmentId);
    List<Assignment> getAllAssignments();
}
