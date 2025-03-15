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
         epic2 = new Epic("Эпик 2", "Описание эпика 2");
         taskManager.createEpic(epic1);
         taskManager.createEpic(epic2);

         subTask1 = new SubTask("Подзадача 1.1", "Описание", epic1.getId());
         subTask2 = new SubTask("Подзадача 1.2", "Описание", epic1.getId());
         subTask3 = new SubTask("Подзадача 2.1", "Описание", epic2.getId());
         taskManager.createSubTask(subTask1);
         taskManager.createSubTask(subTask2);
         taskManager.createSubTask(subTask3);
    }

    @Test
    void createTaskTest() {
        Assertions.assertNotNull(taskManager.getTaskList());
    }

    @Test
    void createSubTaskTest() {
        Assertions.assertNotNull(taskManager.getSubTaskList());
    }

    @Test
    void createEpicTest() {
        Assertions.assertNotNull(taskManager.getEpicList());
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


}