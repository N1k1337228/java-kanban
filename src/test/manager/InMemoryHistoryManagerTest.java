package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclasses.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

        HistoryManager  manager = new InMemoryHistoryManager();
        TaskManager taskManager = new inMemoryTaskManager();
        Task task = new Task("12345","45670987");


    @Test
    void addTest () {
        manager.add(task);
        ArrayList<Task> history = manager.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());

    }

    @Test
    void getHistoryTest () {
        manager.add(task);
        ArrayList<Task> history = new ArrayList<>();
        history.add(task);
        Assertions.assertEquals(history,manager.getHistory());
    }
}