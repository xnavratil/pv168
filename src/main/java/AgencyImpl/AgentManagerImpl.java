package AgencyImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;
import Agency.Agent;
import java.util.List;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import Agency.AgentManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import Exceptions.ValidationException;
import Exceptions.IllegalEntityException;
import Exceptions.ServiceFailureException;
import Utils.DBUtils;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Navratil Peter (xnavrat8@mail.muni.cz) on ${DATE}.
 */

public class AgentManagerImpl implements AgentManager {
    private static final Logger logger = Logger.getLogger(
            AgentManagerImpl.class.getName());

    private DataSource dataSource;


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }


    private static void validate(Agent agent) {
        if (agent == null) {
            throw new IllegalArgumentException("agent is null");
        }
        if (agent.getName() == null) {
            throw new ValidationException("name is null");
        }
        if (agent.getBorn() == null) {
            throw new ValidationException("born is null");
        }
        if (agent.getRecruitmentDate() == null) {
            throw new ValidationException("recruitment date is null");
        }
    }

    @Override
    public void createAgent(Agent agent) {
        checkDataSource();
        validate(agent);
        if (agent.getId() != null) {
            throw new IllegalEntityException("agent id is already set");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in
            // method DBUtils.closeQuietly(...)
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "INSERT INTO Agent (name,born,recruitment_date) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, agent.getName());
            st.setDate(2, java.sql.Date.valueOf(agent.getBorn()));
            st.setDate(3, java.sql.Date.valueOf(agent.getRecruitmentDate()));

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, agent, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            agent.setId(id);
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


    public void updateAgent(Agent updatedAgent) {
        throw new UnsupportedOperationException();
    }

    public void removeAgent(Agent removeAgent) {
        throw new UnsupportedOperationException();
    }

    public Agent findAgentById(Long id) {
        checkDataSource();

        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id, name, born, recruitment_date FROM Agent WHERE id = ?");
            st.setLong(1, id);
            return executeQueryForSingleAgent(st);
        } catch (SQLException ex) {
            String msg = "Error when getting agent with id = " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    static Agent executeQueryForSingleAgent(PreparedStatement st) throws SQLException, ServiceFailureException {
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Agent result = rowToAgent(rs);
            if (rs.next()) {
                throw new ServiceFailureException(
                        "Internal integrity error: more agents with the same id found!");
            }
            return result;
        } else {
            return null;
        }
    }

    private static Agent rowToAgent(ResultSet rs) throws SQLException {
        Agent result = new Agent();
        result.setId(rs.getLong("id"));
        result.setName(rs.getString("name"));
        result.setBorn(toLocalDate(rs.getDate("born")));
        result.setRecruitmentDate(toLocalDate(rs.getDate("recruitment_date")));
        return result;
    }

    private static LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toLocalDate();
    }


    public List<Agent> getAllAgents() {
        throw new UnsupportedOperationException();
    }
}
