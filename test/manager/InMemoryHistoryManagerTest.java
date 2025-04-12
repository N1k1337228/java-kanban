package manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskclasses.Task;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager manager = new InMemoryHistoryManager();
    Task task = new Task("12345", "45670987");
    ArrayList<Task> history;


    @Test
    void addTest() {
        manager.add(task);
        history = manager.getHistory();
        //assertNotNull(history);
        assertEquals(1, history.size());
        assertEquals(1, manager.getHistoryTaskList().getHistoryOfTaskSize());
    }

    @Test
    void getHistoryTest() {
        manager.add(task);
        history = new ArrayList<>();
        history.add(task);
        assertEquals(history, manager.getHistory());
    }

    @Test
    void removeTest() {
        manager.add(task);
        int sizeOfHistoryOfTask = manager.getHistoryTaskList().getHistoryOfTaskSize();
        manager.remove(task.getId());
        history = manager.getHistory();
        int sizeOfHistory = history.size();
        assertEquals(sizeOfHistoryOfTask - 1, manager.getHistoryTaskList().getHistoryOfTaskSize());
        assertEquals(sizeOfHistory, history.size());
    }

    @Test
    void TaskInHistoryNotChangedTest() {
        manager.add(task);
        Assertions.assertEquals(task.getName(), manager.getHistory().get(task.getId()).getName());
        Assertions.assertEquals(task.getDescription(), manager.getHistory().get(task.getId()).getDescription());
        Assertions.assertEquals(task.getStatus(), manager.getHistory().get(task.getId()).getStatus());
    }

    @Test
    void OrderOfHistoryAfterRemoveTask1() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("1", "4");
        Task task2 = new Task("2", "5");
        Task task3 = new Task("3", "6");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());
        taskManager.historyManager.remove(task1.getId());
        Assertions.assertEquals(task2, taskManager.historyManager.getHistoryTaskList().getHead().data);
        Assertions.assertEquals(task3, taskManager.historyManager.getHistoryTaskList().getTail().data);
    }

    @Test
    void OrderOfHistoryAfterRemoveTask2() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("1", "4");
        Task task2 = new Task("2", "5");
        Task task3 = new Task("3", "6");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());
        taskManager.historyManager.remove(task2.getId());
        Assertions.assertEquals(task1, taskManager.historyManager.getHistoryTaskList().getHead().data);
        Assertions.assertEquals(task3, taskManager.historyManager.getHistoryTaskList().getTail().data);
    }

    @Test
    void OrderOfHistoryAfterRemoveTask3() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("1", "4");
        Task task2 = new Task("2", "5");
        Task task3 = new Task("3", "6");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());
        taskManager.historyManager.remove(task3.getId());
        Assertions.assertEquals(task1, taskManager.historyManager.getHistoryTaskList().getHead().data);
        Assertions.assertEquals(task2, taskManager.historyManager.getHistoryTaskList().getTail().data);
    }
}