package AgencyImpl;

import Agency.Mission;
import Agency.MissionManager;


import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Exceptions.ValidationException;
import Exceptions.IllegalEntityException;
import Exceptions.ServiceFailureException;
import Utils.DBUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Navratil Peter (xnavrat8@mail.muni.cz) on ${DATE}.
 */

public class MissionManagerImpl implements MissionManager {
    private static final Logger logger = Logger.getLogger(
            MissionManagerImpl.class.getName());

    private DataSource dataSource;

    public MissionManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MissionManagerImpl(){};

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    private static void validate(Mission mission) {
        if (mission == null) {
            throw new IllegalArgumentException("agent is null");
        }
        if (mission.getCodename() == null) {
            throw new ValidationException("codename is null");
        }
        if (mission.getInfo() == null) {
            throw new ValidationException("info is null");
        }
        if (mission.getIssueDate() == null) {
            throw new ValidationException("issue date is null");
        }
    }

    public void createMission(Mission newMission) {
        checkDataSource();
        validate(newMission);
        if (newMission.getId() != null) {
            throw new IllegalArgumentException("mission id is already set");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "INSERT INTO MISSION (CODENAME,INFO,ISSUE_DATE) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, newMission.getCodename());
            st.setString(2, newMission.getInfo());
            st.setDate(3, java.sql.Date.valueOf(newMission.getIssueDate()));

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, newMission, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            newMission.setId(id);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when inserting mission into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    public void updateMission(Mission updatedMission) {
        checkDataSource();
        validate(updatedMission);
        if (updatedMission.getId() == null) {
            throw new IllegalEntityException("mission id is null");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();

            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE MISSION SET CODENAME = ?, INFO = ?, ISSUE_DATE = ? WHERE id = ?");
            st.setString(1, updatedMission.getCodename());
            st.setString(2, updatedMission.getInfo());
            st.setDate(3, java.sql.Date.valueOf(updatedMission.getIssueDate()));
            st.setLong(4, updatedMission.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, updatedMission, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when updating mission in the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    public void removeMission(Mission removedMission) {
        checkDataSource();
        if (removedMission == null) {
            throw new IllegalArgumentException("removedMission is null");
        }
        if (removedMission.getId() == null) {
            throw new IllegalEntityException("mission id is null");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "DELETE FROM MISSION WHERE ID = ?");
            st.setLong(1, removedMission.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, removedMission, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when deleting mission from the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    public Mission findMissionById(Long missionId) {
        checkDataSource();

        if (missionId == null) {
            throw new IllegalArgumentException("id is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT ID, CODENAME, INFO, ISSUE_DATE FROM MISSION WHERE ID = ?");
            st.setLong(1, missionId);
            return executeQueryForSingleMission(st);
        } catch (SQLException ex) {
            String msg = "Error when getting mission with id = " + missionId + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }

    }

    static Mission executeQueryForSingleMission(PreparedStatement st) throws SQLException, ServiceFailureException {
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Mission result = rowToMission(rs);
            if (rs.next()) {
                throw new ServiceFailureException(
                        "Internal integrity error: more missions with the same id found!");
            }
            return result;
        } else {
            return null;
        }
    }

    private static Mission rowToMission(ResultSet rs) throws SQLException {
        Mission result = new Mission();
        result.setId(rs.getLong("ID"));
        result.setCodename(rs.getString("CODENAME"));
        result.setInfo(rs.getString("INFO"));
        result.setIssueDate(toLocalDate(rs.getDate("ISSUE_DATE")));
        return result;
    }

    private static LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toLocalDate();
    }

    public List<Mission> getAllMissions() {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT ID, CODENAME, INFO, ISSUE_DATE FROM MISSION ");
            return executeQueryForMultipleMissions(st);
        } catch (SQLException ex) {
            String msg = "Error when getting all agents from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    static List<Mission> executeQueryForMultipleMissions(PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
        List<Mission> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rowToMission(rs));
        }
        return result;
    }

}
