import manager.inMemoryTaskManager;
import taskclasses.Epic;
import taskclasses.Status;
import taskclasses.SubTask;
import taskclasses.Task;

public class Main {

    public static void main(String[] args) {
        inMemoryTaskManager taskManager = new inMemoryTaskManager(); // Менеджер задач

        // Создаём две обычные задачи (ID и статус создаются автоматически)
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Получаем сгенерированные ID
        int task1Id = task1.getId();
        int task2Id = task2.getId();

        // Создаём два эпика
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        System.out.println(taskManager.getEpicList());

        // Получаем сгенерированные ID эпиков
        int epic1Id = epic1.getId();
        int epic2Id = epic2.getId();


        SubTask subTask1 = new SubTask("Подзадача 1.1", "Описание", epic1Id);
        SubTask subTask2 = new SubTask("Подзадача 1.2", "Описание", epic1Id);
        SubTask subTask3 = new SubTask("Подзадача 2.1", "Описание", epic2Id);


        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        epic1.addSubTasksId(subTask1.getId());
        epic1.addSubTasksId(subTask2.getId());
        taskManager.createSubTask(subTask3);
        epic2.addSubTasksId(subTask3.getId());
        System.out.println(taskManager.getSubTaskList());

        System.out.println(epic1.getStatus());
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        taskManager.updateEpic(epic1);
        System.out.println(epic1.getStatus());


    }
}

