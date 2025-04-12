package manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclasses.Epic;
import taskclasses.Status;
import taskclasses.SubTask;
import taskclasses.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    Epic epic1;
    Task task1;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;

    @BeforeEach
    void createTaskManager() {
        taskManager = Managers.getDefault();
        task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.createTask(task1);

        epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic1);

        subTask1 = new SubTask("Подзадача 1.1", "Описание", epic1.getId());
        taskManager.createSubTask(subTask1);
        subTask2 = new SubTask("Подзадача 1.2", "Описание", epic1.getId());
        taskManager.createSubTask(subTask2);
        subTask3 = new SubTask("Подзадача 2.1", "Описание", epic1.getId());
        taskManager.createSubTask(subTask3);

    }

    @Test
    void createTaskTest() {
        Assertions.assertTrue(taskManager.getTaskList().contains(task1));
    }

    @Test
    void createSubTaskTest() {
        Assertions.assertTrue(taskManager.getSubTaskList().contains(subTask1));
    }

    @Test
    void createEpicTest() {
        Assertions.assertTrue(taskManager.getEpicList().contains(epic1));
    }

    @Test
    void getTaskTest() {
        Assertions.assertEquals(task1, taskManager.getTask(task1.getId()));
    }

    @Test
    void getSubTaskTest() {
        Assertions.assertEquals(subTask1, taskManager.getSubTask(subTask1.getId()));
    }

    @Test
    void getEpicTest() {
        Assertions.assertEquals(epic1, taskManager.getEpic(epic1.getId()));
    }

    @Test
    void checkVariabelsOfTasks() {
        Task task = new Task("Task 1", "Description of Task 1");
        taskManager.createTask(task);
        Task retrievedTask = taskManager.getTask(task.getId());
        assertEquals(task, retrievedTask, "Задача изменилась после добавления в менеджер");
        assertEquals(task.getName(), retrievedTask.getName(), "Название задачи изменилось");
        assertEquals(task.getDescription(), retrievedTask.getDescription(), "Описание задачи изменилось");
        assertEquals(task.getStatus(), retrievedTask.getStatus(), "Статус задачи изменился");
    }

    @Test
    void removeEpicsTest() {
        taskManager.removeEpics();
        Assertions.assertTrue(taskManager.getEpicList().isEmpty());
    }

    @Test
    void removeSubTaskTest() {
        taskManager.removeSubTask();
        Assertions.assertTrue(taskManager.getSubTaskList().isEmpty());
    }

    @Test
    void removeTasksTest() {
        taskManager.removeTask();
        Assertions.assertTrue(taskManager.getTaskList().isEmpty());
    }

    @Test
    void removeTaskOnId() {
        taskManager.removeTaskOnId(task1.getId());
        Assertions.assertFalse(taskManager.getTaskList().contains(task1));
    }

    @Test
    void removeSubTaskOnId() {
        taskManager.removeSubTaskOnId(subTask1.getId());
        Assertions.assertFalse(taskManager.getSubTaskList().contains(subTask1));
        Assertions.assertFalse(taskManager.getEpic(subTask1.getEpicId()).getSubTasksId().contains(subTask1.getId()));
    }

    @Test
    void removeEpicOnId() {
        taskManager.removeEpicOnId(epic1.getId());
        Assertions.assertFalse(taskManager.getEpicList().contains(epic1));
    }

    @Test
    void getSubtaskIdTest() {
        epic1.addSubTasksId(subTask1);
        epic1.addSubTasksId(subTask2);
        epic1.addSubTasksId(subTask3);
        ArrayList<Integer> subTaskId = epic1.getSubTasksId();
        Assertions.assertEquals(subTaskId, epic1.getSubTasksId());
    }

    @Test
    void updateSabTaskTest() {
        subTask1.setName("new name");
        subTask1.setDescription("new description");
        taskManager.updateSabTask(subTask1);
        Assertions.assertEquals("new name", taskManager.getSubTask(subTask1.getId()).getName());
        Assertions.assertEquals("new description", taskManager.getSubTask(subTask1.getId()).getDescription());
    }

    @Test
    void updateTaskTest() {
        // Создаем обновленную задачу с тем же ID
        task1.setName("new name");
        task1.setDescription("new description");
        taskManager.updateTask(task1);
        Assertions.assertEquals("new name", taskManager.getTask(task1.getId()).getName());
        Assertions.assertEquals("new description", taskManager.getTask(task1.getId()).getDescription());
    }

    @Test
    void updateEpicTest() {
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        subTask3.setStatus(Status.DONE);
        taskManager.updateSabTask(subTask1);
        taskManager.updateSabTask(subTask2);
        taskManager.updateSabTask(subTask3);
        epic1.setName("new name");
        epic1.setDescription("new description");
        taskManager.updateEpic(epic1);
        Assertions.assertEquals("new name", taskManager.getEpic(epic1.getId()).getName());
        Assertions.assertEquals("new description", taskManager.getEpic(epic1.getId()).getDescription());
        Assertions.assertEquals(Status.DONE, epic1.getStatus());
    }

    @Test
    void getSubTaskListOfEpicOnIdTest() {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        subTasks.add(subTask3);
        Assertions.assertEquals(subTasks, taskManager.getSubTaskListOfEpicOnId(epic1.getId()));
    }
}