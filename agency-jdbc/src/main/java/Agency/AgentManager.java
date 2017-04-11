package Agency;

import Exceptions.ValidationException;

import java.util.List;

/**
 * Created by Navratil Peter (xnavrat8@mail.muni.cz) on ${DATE}.
 */

public interface AgentManager {
    void createAgent(Agent newAgent) throws ValidationException;
    void updateAgent(Agent updatedAgent);
    void removeAgent(Agent removeAgent);
    Agent findAgentById(Long agentId);
    List<Agent> getAllAgents();
}
