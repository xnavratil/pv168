package AgencyImpl;

import Agency.Assignment;
import Agency.AssignmentManager;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import Exceptions.IllegalEntityException;
import Exceptions.ValidationException;
import Exceptions.ServiceFailureException;
import Utils.DBUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Navratil Peter (xnavrat8@mail.muni.cz) on ${DATE}.
 */

public class AssignmentManagerImpl implements AssignmentManager {
    private static final Logger logger = Logger.getLogger(
            AssignmentManagerImpl.class.getName());
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    private static void validate(Assignment assignment) {
        if (assignment == null) {
            throw new IllegalArgumentException("agent is null");
        }
        if (assignment.getAssignedAgent() == null) {
            throw new ValidationException("assignment agent is null");
        }
        if (assignment.getMission() == null) {
            throw new ValidationException("mission is null");
        }
        if (assignment.getStart() == null) {
            throw new ValidationException("start is null");
        }
        if (assignment.getExpectedEnd() == null) {
            throw new ValidationException("expected end is null");
        }
    }

    public void createAssignment(Assignment newAssignment) {
        checkDataSource();
        validate(newAssignment);
        if (newAssignment.getId() != null) {
            throw new IllegalArgumentException("assignment id is already set");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "INSERT INTO ASSIGNMENT (MISSION_ID,AGENT_ID,START, EXPECTED_END) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setLong(1, newAssignment.getMission().getId());
            st.setLong(2, newAssignment.getAssignedAgent().getId());
            st.setDate(3, java.sql.Date.valueOf(newAssignment.getStart()));
            st.setDate(4, java.sql.Date.valueOf(newAssignment.getExpectedEnd()));

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, newAssignment, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            newAssignment.setId(id);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when inserting grave into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    public void updateAssignment(Assignment updatedAssignment) {
        checkDataSource();
        validate(updatedAssignment);
        if (updatedAssignment.getId() == null) {
            throw new IllegalEntityException("grave id is null");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in
            // method DBUtils.closeQuietly(...)
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE ASSIGNMENT SET MISSION_ID = ?, AGENT_ID = ?, START = ?, EXPECTED_END = ? WHERE ID = ?");
            st.setLong(1, updatedAssignment.getMission().getId());
            st.setLong(2, updatedAssignment.getAssignedAgent().getId());
            st.setDate(3, java.sql.Date.valueOf(updatedAssignment.getStart()));
            st.setDate(4, java.sql.Date.valueOf(updatedAssignment.getExpectedEnd()));
            st.setLong(5, updatedAssignment.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, updatedAssignment, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when updating updatedAssignment in the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    public void removeAssignment(Assignment removedAssignment) {
        checkDataSource();
        if (removedAssignment == null) {
            throw new IllegalArgumentException("grave is null");
        }
        if (removedAssignment.getId() == null) {
            throw new IllegalEntityException("grave id is null");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "DELETE FROM ASSIGNMENT WHERE ID = ?");
            st.setLong(1, removedAssignment.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, removedAssignment, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when deleting assignment from the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    public Assignment findAssignmentById(Long assignmentId) {
        checkDataSource();

        if (assignmentId == null) {
            throw new IllegalArgumentException("id is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT ID, MISSION_ID, AGENT_ID, START, EXPECTED_END FROM ASSIGNMENT WHERE ID = ?");
            st.setLong(1, assignmentId);
            return executeQueryForSingleAssignment(st);
        } catch (SQLException ex) {
            String msg = "Error when getting assignment with id = " + assignmentId + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }

    }

    static Assignment executeQueryForSingleAssignment(PreparedStatement st) throws SQLException, ServiceFailureException {
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Assignment result = rowToAssignment(rs);
            if (rs.next()) {
                throw new ServiceFailureException(
                        "Internal integrity error: more assignments with the same id found!");
            }
            return result;
        } else {
            return null;
        }
    }

    private static Assignment rowToAssignment(ResultSet rs) throws SQLException {
        Assignment result = new Assignment();
        result.setId(rs.getLong("ID"));
        result.setAssignedAgent(rs.getLong("AGENT_ID"));
        result.setMission(rs.getLong("MISSION_ID"));
        result.setStart(toLocalDate(rs.getDate("START")));
        result.setExpectedEnd(toLocalDate(rs.getDate("EXPECTED_END")));
        return result;
    }

    private static LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toLocalDate();
    }


    public List<Assignment> getAllAssignments() {
        throw new UnsupportedOperationException();
    }
}
