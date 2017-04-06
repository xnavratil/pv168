package Agency;

import Exceptions.MissionException;

import java.util.List;

/**
 * Created by Navratil Peter (xnavrat8@mail.muni.cz) on ${DATE}.
 */

public interface MissionManager {
    void createMission(Mission newMission);
    void updateMission(Mission updatedMission);
    void removeMission(Mission removedMission);
    Mission findMissionById(Long missionId);
    List<Mission> getAllMissions();

}

