package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskclasses.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager manager = new InMemoryHistoryManager();
    Task task = new Task("12345", "45670987");
    ArrayList<Task> history;


    @Test
    void addTest() {
        manager.add(task);
        history = manager.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
    }

    @Test
    void getHistoryTest() {
        manager.add(task);
        history = new ArrayList<>();
        history.add(task);
        assertEquals(history, manager.getHistory());
    }

    @Test
    void maxSizeOfHistoryOfTask() {
        for (int i = 0; i < 10; i++) {
            manager.add(task);
        }
        Task task1 = new Task("name", "description");
        manager.add(task1);
        Assertions.assertEquals(10, manager.getHistory().size());
    }

    @Test
    void TaskInHistoryNotChangedTest() {
        manager.add(task);
        Assertions.assertEquals(task.getName(), manager.getHistory().get(task.getId()).getName());
        Assertions.assertEquals(task.getDescription(), manager.getHistory().get(task.getId()).getDescription());
        Assertions.assertEquals(task.getStatus(), manager.getHistory().get(task.getId()).getStatus());
    }
}