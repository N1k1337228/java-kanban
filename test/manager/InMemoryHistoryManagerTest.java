package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclasses.Task;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager manager;
    ArrayList<Task> history;
    Task task;
    Task task1;
    Task task2;
    Task task3;

    @BeforeEach
    void createHistoryManager() {
        task = new Task("12345", "45670987");
        task.setId(1);
        task1 = new Task("1", "4");
        task1.setId(2);
        task2 = new Task("2", "5");
        task2.setId(3);
        task3 = new Task("3", "6");
        task3.setId(4);
        manager = new InMemoryHistoryManager();
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);


    }

    @Test
    void addTest() {
        manager.add(task);
        history = new ArrayList<>(manager.getHistory());
        assertTrue(history.contains(task));
    }

    @Test
    void addDuplicatesTest() {
        Task taskDuplicate = new Task("12345", "45670987");
        taskDuplicate.setId(1);
        manager.add(task);
        manager.add(taskDuplicate);
        assertEquals(4, manager.getHistory().size());
    }

    @Test
    void getHistoryTest() {
        manager = new InMemoryHistoryManager();
        manager.add(task);
        history = new ArrayList<>();
        history.add(task);
        assertEquals(history, manager.getHistory());
    }

    @Test
    void removeTest() {
        manager.add(task);
        manager.remove(task.getId());
        assertFalse(manager.getHistory().contains(task));
    }

    @Test
    void TaskInHistoryNotChangedTest() {
        manager.add(task);
        Assertions.assertEquals(task.getName(), manager.getHistory().get(3).getName());
        Assertions.assertEquals(task.getDescription(), manager.getHistory().get(3).getDescription());
        Assertions.assertEquals(task.getStatus(), manager.getHistory().get(3).getStatus());
    }

    @Test
    void OrderOfHistoryAfterRemoveTask1() {
        manager.remove(task1.getId());
        Assertions.assertEquals(Arrays.asList(task2, task3), manager.getHistory());
    }

    @Test
    void OrderOfHistoryAfterRemoveTask2() {
        manager.remove(task2.getId());
        Assertions.assertEquals(Arrays.asList(task1, task3), manager.getHistory());
    }

    @Test
    void OrderOfHistoryAfterRemoveTask3() {
        manager.remove(task3.getId());
        Assertions.assertEquals(Arrays.asList(task1, task2), manager.getHistory());
    }
}