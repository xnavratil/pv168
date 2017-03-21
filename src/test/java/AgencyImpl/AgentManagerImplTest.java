package AgencyImpl;

import Agency.Agent;
import Agency.AgentBuilder;
import org.junit.Test;
import org.junit.Before;
import Agency.AgentManager;

import java.time.LocalDate;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by matusmacko on 8.3.17.
 */
public class AgentManagerImplTest {
    private AgentManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        manager = new AgentManagerImpl();
    }

    private AgentBuilder ruthlessAgentBuilder() {
        return new AgentBuilder()
                .id(null)
                .name("James Bond")
                .born(LocalDate.of(1970, 9, 2))
                .recruitmentDate(LocalDate.of(1990, 9, 2));
    }

    private AgentBuilder ultraAgentBuilder() {
        return new AgentBuilder()
                .id(null)
                .name("Sherlock Holmes")
                .born(LocalDate.of(1830, 9, 2))
                .recruitmentDate(LocalDate.of(1850, 9, 2));
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
    public void createNullMission() {
        manager.createAgent(null);
    }

    @Test
    public void createAgentWithNullName() {
        Agent agent = ruthlessAgentBuilder().name(null).build();
        manager.createAgent(agent);

        assertThat(manager.findAgentById(agent.getId()))
                .isNotNull()
                .isEqualToComparingFieldByField(agent);
    }

    @Test
    public void createAgentWithNullBorn() {
        Agent agent = ruthlessAgentBuilder().born(null).build();
        manager.createAgent(agent);

        assertThat(manager.findAgentById(agent.getId()))
                .isNotNull()
                .isEqualToComparingFieldByField(agent);
    }

    @Test
    public void createAgentWithNullRecruitmentDate() {
        Agent agent = ruthlessAgentBuilder().recruitmentDate(null).build();
        manager.createAgent(agent);

        assertThat(manager.findAgentById(agent.getId()))
                .isNotNull()
                .isEqualToComparingFieldByField(agent);
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
}