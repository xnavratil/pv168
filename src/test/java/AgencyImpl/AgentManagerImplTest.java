package AgencyImpl;

import Agency.Agent;
import Agency.AgentBuilder;
import org.junit.Test;
import javax.sql.DataSource;
import org.junit.rules.ExpectedException;
import org.junit.Before;
import java.sql.SQLException;
import org.apache.derby.jdbc.EmbeddedDataSource;
import Agency.AgentManager;
import Exceptions.ValidationException;
import java.time.LocalDate;
import java.util.function.Consumer;
import Utils.DBUtils;
import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by matusmacko on 8.3.17.
 */
public class AgentManagerImplTest {
    private AgentManagerImpl manager;
    private DataSource ds;


    @Rule
    // attribute annotated with @Rule annotation must be public :-(
    public ExpectedException expectedException = ExpectedException.none();

    //--------------------------------------------------------------------------
    // Test initialization
    //--------------------------------------------------------------------------

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        // we will use in memory database
        ds.setDatabaseName("memory:agentmanager-test");
        // database is created automatically if it does not exist yet
        ds.setCreateDatabase("create");
        return ds;
    }

    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,AgentManager.class.getResource("/createTables.sql"));
        manager = new AgentManagerImpl();
        manager.setDataSource(ds);
    }

    @After
    public void tearDown() throws SQLException {
        // Drop tables after each test
        DBUtils.executeSqlScript(ds,AgentManager.class.getResource("/dropTables.sql"));
    }

    static AgentBuilder ruthlessAgentBuilder() {
        return new AgentBuilder()
                .id(null)
                .name("James Bond")
                .born(LocalDate.of(1970, 9, 2))
                .recruitmentDate(LocalDate.of(1990, 9, 2));
    }

    static AgentBuilder ultraAgentBuilder() {
        return new AgentBuilder()
                .id(null)
                .name("Sherlock Holmes")
                .born(LocalDate.of(1920, 2, 10)) //.born(LocalDate.of(1830, 9, 2))
                .recruitmentDate(LocalDate.of(1941, 9, 2));//.recruitmentDate(LocalDate.of(1850, 9, 2));
    }

    @Test
    public void createAgent()  {
        Agent firstAgent = ruthlessAgentBuilder().build();
        manager.createAgent(firstAgent);

        Long agentId = firstAgent.getId();
        assertThat(agentId).isNotNull();

        assertThat(manager.findAgentById(agentId))
                .isNotSameAs(firstAgent)
                .isEqualToComparingFieldByField(firstAgent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullAgent() {
        manager.createAgent(null);
    }

    @Test
    public void createAgentWithNullName() {
        Agent agent = ruthlessAgentBuilder().name(null).build();
        expectedException.expect(ValidationException.class);
        manager.createAgent(agent);
    }

    @Test
    public void createAgentWithNullBorn() {
        Agent agent = ruthlessAgentBuilder().born(null).build();
        expectedException.expect(ValidationException.class);
        manager.createAgent(agent);
    }

    @Test
    public void createAgentWithNullRecruitmentDate() {
        Agent agent = ruthlessAgentBuilder().recruitmentDate(null).build();
        expectedException.expect(ValidationException.class);
        manager.createAgent(agent);
    }

    private void testUpdateAgent(Consumer<Agent> updateOperation) {
        Agent sourceAgent = ruthlessAgentBuilder().build();
        Agent anotherAgent = ultraAgentBuilder().build();
        manager.createAgent(sourceAgent);
        manager.createAgent(anotherAgent);

        updateOperation.accept(sourceAgent);

        manager.updateAgent(sourceAgent);
        assertThat(manager.findAgentById(sourceAgent.getId()))
                .isEqualToComparingFieldByField(sourceAgent);

        assertThat(manager.findAgentById(anotherAgent.getId()))
                .isEqualToComparingFieldByField(anotherAgent);
    }

    @Test
    public void updateAgentName() {
        testUpdateAgent(agent -> agent.setName("John Bond"));
    }

    @Test
    public void updateAgentBorn() {
        testUpdateAgent(agent -> agent.setBorn(LocalDate.of(1950, 9, 2)));
    }

    @Test
    public void updateAgentRecruitmentDate() {
        testUpdateAgent(agent -> agent.setRecruitmentDate(LocalDate.of(1960, 9, 2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullAgent() {
        manager.updateAgent(null);
    }

    @Test
    public void removeAgent() {
        Agent firstAgent = ruthlessAgentBuilder().build();
        Agent secondAgent = ultraAgentBuilder().build();
        manager.createAgent(firstAgent);
        manager.createAgent(secondAgent);

        assertThat(manager.findAgentById(firstAgent.getId())).isNotNull();
        assertThat(manager.findAgentById(secondAgent.getId())).isNotNull();

        manager.removeAgent(firstAgent);

        assertThat(manager.findAgentById(firstAgent.getId())).isNull();
        assertThat(manager.findAgentById(secondAgent.getId())).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteNullAgent() {
        manager.removeAgent(null);
    }

    @Test
    public void findAllAgents() {

        assertThat(manager.getAllAgents()).isEmpty();

        Agent a1 = ruthlessAgentBuilder().build();
        Agent a2 = ultraAgentBuilder().build();

        manager.createAgent(a1);
        manager.createAgent(a2);

        assertThat(manager.getAllAgents())
                .usingFieldByFieldElementComparator()
                .containsOnly(a1,a2);
    }
}