package AgencyImpl;

import Agency.Agent;
import org.junit.Test;
import java.util.Date;
import org.junit.Before;
import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Created by matusmacko on 8.3.17.
 */
public class AgentManagerImplTest {
    private AgentManagerImpl manager;

    @Before
    public void setUp() {
        manager = new AgentManagerImpl();
    }

    @Test
    public void createAgent() throws Exception {
        Agent agent = newAgent("James Bond", LocalDate.of(2017, 3, 1), LocalDate.of(2017, 3, 1));
        manager.createAgent(agent);

        Long agentId = agent.getId();
        assertNotNull(agentId);
        Agent result = manager.findAgentById(agentId);
        assertEquals(agent, result);
        assertNotSame(agent, result);
        assertDeepEquals(agent, result);
    }

    @Test
    public void updateAgent() throws Exception {
        Agent agent = newAgent("James Bond", LocalDate.of(1970, 1, 1), LocalDate.of(1985, 1, 1));
        Agent anotherAgent = newAgent("Sterling Archer", LocalDate.of(1990, 1, 1) ,LocalDate.of(2000, 1, 1));
        manager.createAgent(agent);
        manager.createAgent(anotherAgent);
        Long agentId = agent.getId();

        agent = manager.findAgentById(agentId);
        agent.setName("John Bond");
        manager.updateAgent(agent);
        assertEquals("John Bond", agent.getName());
        assertEquals(LocalDate.of(1970, 1, 1), agent.getBorn());
        assertEquals(LocalDate.of(1985, 1, 1), agent.getRecruitmentDate());

        agent = manager.findAgentById(agentId);
        agent.setBorn(LocalDate.of(1971, 1, 1));
        manager.updateAgent(agent);
        assertEquals("John Bond", agent.getName());
        assertEquals(LocalDate.of(1971, 1, 1), agent.getBorn());
        assertEquals(LocalDate.of(1985, 1, 1), agent.getRecruitmentDate());

        agent = manager.findAgentById(agentId);
        agent.setRecruitmentDate(LocalDate.of(1986, 1, 1));
        manager.updateAgent(agent);
        assertEquals("John Bond", agent.getName());
        assertEquals(LocalDate.of(1971, 1, 1), agent.getBorn());
        assertEquals(LocalDate.of(1986, 1, 1), agent.getRecruitmentDate());

        assertDeepEquals(anotherAgent, manager.findAgentById(anotherAgent.getId()));
    }

    @Test
    public void removeAgent() throws Exception {
        Agent firstAgent = newAgent("James Bond", LocalDate.of(1970, 1, 1), LocalDate.of(1985, 1, 1));
        Agent secondAgent = newAgent("Sterling Archer", LocalDate.of(1990, 1, 1) ,LocalDate.of(2000, 1, 1));
        manager.createAgent(firstAgent);
        manager.createAgent(secondAgent);

        assertNotNull(manager.findAgentById(firstAgent.getId()));
        assertNotNull(manager.findAgentById(secondAgent.getId()));

        manager.removeAgent(firstAgent);

        assertNull(manager.findAgentById(firstAgent.getId()));
        assertNotNull(manager.findAgentById(secondAgent.getId()));
    }

    private static Agent newAgent(String name, LocalDate born, LocalDate recruitmentDate) {
        Agent agent = new Agent();
        agent.setName(name);
        agent.setBorn(born);
        agent.setRecruitmentDate(recruitmentDate);
        return agent;
    }

    private void assertDeepEquals(Agent expected, Agent actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getBorn(), actual.getBorn());
        assertEquals(expected.getRecruitmentDate(), actual.getRecruitmentDate());
    }
}