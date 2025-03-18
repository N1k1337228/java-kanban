package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;
    Epic epic1;
    Epic epic2;
    Task task1;
    Task task2;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;

    @BeforeEach
    void createTaskManager () {
         taskManager = Managers.getDefault();
         task1 = new Task("Задача 1", "Описание задачи 1");
         taskManager.createTask(task1);

         epic1 = new Epic("Эпик 1", "Описание эпика 1");
         taskManager.createEpic(epic1);

         subTask1 = new SubTask("Подзадача 1.1", "Описание", epic1.getId());
         taskManager.createSubTask(subTask1);
    }

    @Test
    void createTaskTest () {
        Assertions.assertTrue(taskManager.getTaskList().contains(task1));
    }

    @Test
    void createSubTaskTest () {
        Assertions.assertTrue(taskManager.getSubTaskList().contains(subTask1));
    }

    @Test
    void createEpicTest () {
        Assertions.assertTrue(taskManager.getEpicList().contains(epic1));
    }

    @Test
    void getTaskTest () {
        Assertions.assertEquals(task1,taskManager.getTask(task1.getId()));
    }

    @Test
    void getSubTaskTest () {
        Assertions.assertEquals(subTask1,taskManager.getSubTask(subTask1.getId()));
    }

    @Test
    void getEpicTest () {
        Assertions.assertEquals(epic1,taskManager.getEpic(epic1.getId()));
    }

    @Test
    void checkVariabelsOfTasks () {
        Task task = new Task( "Task 1", "Description of Task 1");
        taskManager.createTask(task);
        Task retrievedTask = taskManager.getTask(task.getId());
        assertEquals(task, retrievedTask, "Задача изменилась после добавления в менеджер");
        assertEquals(task.getName(), retrievedTask.getName(), "Название задачи изменилось");
        assertEquals(task.getDescription(), retrievedTask.getDescription(), "Описание задачи изменилось");
        assertEquals(task.getStatus(), retrievedTask.getStatus(), "Статус задачи изменился");
    }

    @Test
    void managersTest () {
        Assertions.assertNotNull(Managers.getDefault());
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }

    @Test
    void removeEpicsTest () {
        taskManager.removeEpics();
        Assertions.assertTrue(taskManager.getEpicList().isEmpty());
    }

    @Test
    void removeSubTaskTest () {
        taskManager.removeSubTask();
        Assertions.assertTrue(taskManager.getSubTaskList().isEmpty());
    }

    @Test
    void removeTasksTest () {
        taskManager.removeTask();
        Assertions.assertTrue(taskManager.getTaskList().isEmpty());
    }

    @Test
    void removeTaskOnId () {
        taskManager.removeTaskOnId(task1.getId());
        Assertions.assertFalse(taskManager.getTaskList().contains(task1));
    }

    @Test
    void removeSubTaskOnId () {
        taskManager.removeSubTaskOnId(subTask1.getId());
        Assertions.assertFalse(taskManager.getSubTaskList().contains(subTask1));
    }

    @Test
    void removeEpicOnId () {
        taskManager.removeEpicOnId(epic1.getId());
        Assertions.assertFalse(taskManager.getEpicList().contains(epic1));
    }
}