package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;

public class TaskTest {
    Epic epic1;
    Epic epic2;
    Task task1;
    Task task2;
    SubTask subTask1;
    SubTask subTask2;


    @BeforeEach
    void createTaskManager() {
        task1 = new Task("Задача 1", "Описание задачи 1");
        task2 = new Task("Задача 2", "Описание задачи 2");

        epic2 = new Epic("Задача 2", "Описание задачи 2");
        epic1 = new Epic("Эпик 1", "Описание эпика 1");
        epic1.setId(1);

        subTask1 = new SubTask("Подзадача 1.1", "Описание", epic1.getId());
        subTask2 = new SubTask("Подзадача 1.2", "Описание", epic1.getId());
    }

    @Test
    void taskIdTest() {
        task1 = new Task("Задача 1", "Описание задачи 1");
        task2.setId(task1.getId());
        boolean test = task1.equals(task2);
        Assertions.assertTrue(test);
    }

    @Test
    void epicIdTest() {
        epic1 = new Epic("Задача 1", "Описание задачи 1");
        epic2.setId(epic1.getId());
        boolean test = epic1.equals(epic2);
        Assertions.assertTrue(test);
    }

    @Test
    void subTaskIdTest() {
        subTask1 = new SubTask("Задача 1", "Описание задачи 1", epic1.getId());
        subTask2 = new SubTask("Задача 2", "Описание задачи 2", epic1.getId());
        task2 = new Task("Задача 2", "Описание задачи 2");
        subTask2.setId(subTask1.getId());
        boolean test = subTask1.equals(subTask2);
        Assertions.assertTrue(test);
    }
}
