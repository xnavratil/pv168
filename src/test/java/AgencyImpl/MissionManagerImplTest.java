package AgencyImpl;

import Agency.Mission;
import Agency.MissionManager;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Created by pnavratil on 3/8/17.
 */
public class MissionManagerImplTest {
    private MissionManager manager;
    private Mission firstMission;
    private Mission secondMission;

    @Before
    public void setUp() throws Exception {
        manager = new MissionManagerImpl();
        firstMission = new Mission("Day-D","Preparation for Day-D", LocalDate.of(1944, 9, 3));
        secondMission = new Mission("Overlord", "Some description", LocalDate.of(1940, 12, 12));
    }

    @Test
    public void createMission() throws Exception {
        manager.createMission(firstMission);

        Long missionId = firstMission.getId();
        assertNotNull(missionId);

        Mission result = manager.findMissionById(missionId);
        assertEquals(firstMission, result);
        assertNotSame(firstMission, result);
        assertDeepEquals(firstMission, result);
    }

    @Test
    public void updateMission() throws Exception {
        manager.createMission(firstMission);
        manager.createMission(secondMission);
        Long missionId = firstMission.getId();

        firstMission = manager.findMissionById(missionId);
        assertEquals("Overlord", firstMission.getCodename());
        assertEquals(LocalDate.of(1940, 12, 12), firstMission.getIssueDate());
        assertEquals("Some description", firstMission.getInfo());
        
        firstMission = manager.findMissionById(missionId);
        firstMission.setCodename("Day-Z");
        manager.updateMission(firstMission);
        assertEquals("Day-Z", firstMission.getCodename());
        assertEquals(LocalDate.of(1940, 12, 12), firstMission.getIssueDate());
        assertEquals("Some description", firstMission.getInfo());
        
        firstMission = manager.findMissionById(missionId);
        firstMission.setIssueDate(LocalDate.of(1941, 4, 4));
        manager.updateMission(firstMission);
        assertEquals("Day-Z", firstMission.getCodename());
        assertEquals(LocalDate.of(1941, 4, 4), firstMission.getIssueDate());
        assertEquals("Some description", firstMission.getInfo());
        
        firstMission = manager.findMissionById(missionId);
        firstMission.setInfo("some another description");
        manager.updateMission(firstMission);
        assertEquals("Day-Z", firstMission.getCodename());
        assertEquals(LocalDate.of(1941, 4, 4), firstMission.getIssueDate());
        assertEquals("some another description", firstMission.getInfo());
        
        assertDeepEquals(secondMission, manager.findMissionById(secondMission.getId()));
    }

    @Test
    public void removeMission() throws Exception {
        manager.createMission(firstMission);
        manager.createMission(secondMission);
        assertNotNull(manager.findMissionById(firstMission.getId()));
        assertNotNull(manager.findMissionById(secondMission.getId()));
        
        manager.removeMission(firstMission);
        assertNull(manager.findMissionById(firstMission.getId()));
        assertNotNull(manager.findMissionById(secondMission.getId()));

    }

    private void assertDeepEquals(Mission expected, Mission actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCodename(), actual.getCodename());
        assertEquals(expected.getInfo(), actual.getInfo());
        assertEquals(expected.getIssueDate(), actual.getIssueDate());
    }
}