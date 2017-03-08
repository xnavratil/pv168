package Agency;

import java.util.List;

/**
 * Created by Navratil Peter (xnavrat8@mail.muni.cz) on ${DATE}.
 */

public interface AgentManager {
    void createAgent(Agent newAgent);
    void updateAgent(Agent updatedAgent);
    void removeAgent(Agent removeAgent);
    Agent findAgentById(long agentId);
    List<Agent> getAllAgents();
}
