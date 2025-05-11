package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclasses.Epic;
import taskclasses.Status;
import taskclasses.SubTask;
import taskclasses.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    public InMemoryTaskManager taskManager;
    Epic epic1;
    Task task1;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;

    @BeforeEach
    void createTaskManager() {
        taskManager = new InMemoryTaskManager();
        task1 = new Task("Задача 1", "Описание задачи 1", 100);
        task1.setStartTime(LocalDateTime.of(2022, 4, 12, 3, 55));
        task1.setEndTime(LocalDateTime.of(2022, 4, 12, 5, 35));
        taskManager.createTask(task1);
        epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic1);
        subTask1 = new SubTask("Подзадача 1.1", "Описание", epic1.getId(), 10);
        subTask1.setStartTime(LocalDateTime.of(2023, 5, 1, 10, 10));
        subTask1.setEndTime(LocalDateTime.of(2023, 5, 1, 10, 20));
        taskManager.createSubTask(subTask1);
        subTask2 = new SubTask("Подзадача 1.2", "Описание", epic1.getId(), 15);
        subTask2.setStartTime(LocalDateTime.of(2023, 6, 2, 11, 15));
        subTask2.setEndTime(LocalDateTime.of(2023, 6, 2, 11, 30));
        taskManager.createSubTask(subTask2);
        subTask3 = new SubTask("Подзадача 2.1", "Описание", epic1.getId(), 20);
        subTask3.setStartTime(LocalDateTime.of(2023, 7, 11, 15, 20));
        subTask3.setEndTime(LocalDateTime.of(2023, 7, 11, 15, 40));
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
        Task task = new Task("Task 1", "Description of Task 1", 140);
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
        Assertions.assertFalse(taskManager.getEpic(subTask1.getEpicId()).getSubTasks().contains(subTask1));
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
        ArrayList<SubTask> subTaskId = epic1.getSubTasks();
        Assertions.assertEquals(subTaskId, epic1.getSubTasks());
    }

    @Test
    void updateSabTaskTest() {
        subTask1.setName("new name");
        subTask1.setDescription("new description");
        System.out.println(subTask1);
        taskManager.updateSabTask(subTask1);
        Assertions.assertEquals("new name", taskManager.getSubTask(subTask1.getId()).getName());
        Assertions.assertEquals("new description", taskManager.getSubTask(subTask1.getId()).getDescription());
    }

    @Test
    void updateTaskTest() {
        task1.setName("new name");
        task1.setDescription("new description");
        taskManager.updateTask(task1);
        Assertions.assertEquals("new name", taskManager.getTask(task1.getId()).getName());
        Assertions.assertEquals("new description", taskManager.getTask(task1.getId()).getDescription());
    }

    @Test
    void updateEpicDoneTest() {
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
    void getSubTaskListOfEpicOnIdTest() {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        subTasks.add(subTask3);
        System.out.println(subTasks);
        System.out.println(taskManager.getSubTaskListOfEpicOnId(epic1.getId()));
        Assertions.assertEquals(subTasks, taskManager.getSubTaskListOfEpicOnId(epic1.getId()));
    }

    @Test
    public void checkIntersectionTasksTest() {
        Task task2 = new Task("rrr", "hhh", 30);
        boolean result = taskManager.checkIntersectionTasks(task2);
        Assertions.assertTrue(result);
    }

    /*Извините,возможно глупый вопрос,но я не совсем понимаю назначение этого класса, логика общая для классов
    InMemoryTaskManager и FileBackedTaskManager проверяется только в классе InMemoryTaskManagerTest если имеется ввиду
    проверка переопределённых методов для FileBackedTaskManager, то они по идее ведь тоже проверяются,
    т.к. если бы они неправильно работали, то тесты в FileBackedTaskManagerTest бы не проходили*/
}