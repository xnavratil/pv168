package Agency;

import java.util.List;

/**
 * Created by Navratil Peter (xnavrat8@mail.muni.cz) on ${DATE}.
 */

public interface AssignManager {
    void createAssignment(Assign newAssignment);
    void updateAssignment(Assign updatedAssignment);
    void removeAssignment(Assign removedAssignment);
    Assign findAssignmentById(long assignmentId);
    List<Assign> getAllAssignments();
}
