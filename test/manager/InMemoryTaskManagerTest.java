package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskclasses.Status;
import taskclasses.SubTask;
import taskclasses.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

    @Test
    void checkVariabelsOfTasks() {
        Task task = new Task("Task 1", "Description of Task 1");
        task.setStartTime(LocalDateTime.of(2024, 7, 22, 10, 44));
        taskManager.createTask(task);
        System.out.println(task);
        Task retrievedTask = taskManager.getTask(task.getId());
        assertEquals(task, retrievedTask, "Задача изменилась после добавления в менеджер");
        assertEquals(task.getName(), retrievedTask.getName(), "Название задачи изменилось");
        assertEquals(task.getDescription(), retrievedTask.getDescription(), "Описание задачи изменилось");
        assertEquals(task.getStatus(), retrievedTask.getStatus(), "Статус задачи изменился");
    }

    @Test
    void getSubtaskIdTest() {
        epic1.addSubTasksId(subTask1);
        epic1.addSubTasksId(subTask2);
        epic1.addSubTasksId(subTask3);
        ArrayList<SubTask> subTaskId = epic1.getSubTasks();
        Assertions.assertEquals(subTaskId, epic1.getSubTasks());
    }

    @Test
    void updateEpicNewTest() {
        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.NEW);
        subTask3.setStatus(Status.NEW);
        taskManager.updateSabTask(subTask1);
        taskManager.updateSabTask(subTask2);
        taskManager.updateSabTask(subTask3);
        epic1.setName("new name");
        epic1.setDescription("new description");
        taskManager.updateEpic(epic1);
        Assertions.assertEquals("new name", taskManager.getEpic(epic1.getId()).getName());
        Assertions.assertEquals("new description", taskManager.getEpic(epic1.getId()).getDescription());
        Assertions.assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    void updateEpicInProgressTest() {
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        subTask3.setStatus(Status.IN_PROGRESS);
        taskManager.updateSabTask(subTask1);
        taskManager.updateSabTask(subTask2);
        taskManager.updateSabTask(subTask3);
        epic1.setName("new name");
        epic1.setDescription("new description");
        taskManager.updateEpic(epic1);
        Assertions.assertEquals("new name", taskManager.getEpic(epic1.getId()).getName());
        Assertions.assertEquals("new description", taskManager.getEpic(epic1.getId()).getDescription());
        Assertions.assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    void updateEpicNewAndDoneTest() {
        subTask1.setStatus(Status.NEW);
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
        Assertions.assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void checkIntersectionTasksTest() {
        // Проверить создаётся ли задача т.к. если нет, то она пересекается с какой - то другой задачей
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task2 = new Task("rrr", "hhh");
        task2.setStartTime(LocalDateTime.of(2020, 2, 15, 10, 15));
        task2.setDuration(15);
        manager.createTask(task1);
        manager.createTask(task2);
        boolean result = manager.taskMap.isEmpty();
        Assertions.assertFalse(result);
    }
}