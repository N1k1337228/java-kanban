package manager;

import exceptons.ManagerSaveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {


    Path file;
    Path tempFile;

    public FileBackedTaskManagerTest() throws IOException {
        super(new FileBackedTaskManager(Files.createTempFile("File", ".csv")));
    }

    @BeforeEach
    public void createManagers() throws IOException {
        file = Files.createTempFile("testFile", ".csv");
        taskManager = new FileBackedTaskManager(file);

    }

    @Test
    public void saveEmptyFileAndLoadEmptyFile() {
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertTrue(fileBackedTaskManager1.taskMap.isEmpty());
        Assertions.assertTrue(fileBackedTaskManager1.epicMap.isEmpty());
        Assertions.assertTrue(fileBackedTaskManager1.subTaskMap.isEmpty());

    }

    @Test
    public void saveSomeTasks() throws IOException {
        taskManager.createTask(task1);
        Task task2 = new Task("2", "bbb");
        task2.setStartTime(LocalDateTime.of(2021, 5, 11, 5, 20));
        taskManager.createTask(task2);
        Task task3 = new Task("3", "ccc");
        task3.setStartTime(LocalDateTime.of(2021, 9, 3, 13, 25));
        taskManager.createTask(task3);
        List<String> lines = new ArrayList<>(Files.readAllLines(taskManager.getPath()));
        Assertions.assertEquals(4, lines.size());
        Assertions.assertEquals(taskManager.getTask(1).toString().trim(), lines.get(1));
        Assertions.assertEquals(taskManager.getTask(2).toString().trim(), lines.get(2));
        Assertions.assertEquals(taskManager.getTask(3).toString().trim(), lines.get(3));
    }

    @Test
    public void loadTasksTest() {
        Task task = new Task("drtyuio", "ftyuio");
        task.setStartTime(LocalDateTime.of(2019, 4, 23, 3, 14));
        Epic epic = new Epic("121", "w2322");
        epic.setStartTime(LocalDateTime.of(2021, 11, 20, 4, 44));
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("werty", "iuytd", epic.getId());
        subTask.setStartTime(LocalDateTime.now());
        taskManager.createSubTask(subTask);
        FileBackedTaskManager fileLoad = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(taskManager.getTask(1), fileLoad.getTask(1));
        Assertions.assertEquals(taskManager.getEpic(2), fileLoad.getEpic(2));
        Assertions.assertEquals(taskManager.getSubTask(3), fileLoad.getSubTask(3));
    }

    @Test
    void loadFromFileShouldThrowWhenMalformedData() throws IOException {
        tempFile = Files.createTempFile("tasks", ".csv");
        String badData = "1,TASK,Test,INVALID_STATUS,Desc,2023.01.01_10:00,2023.01.01_11:00,10,";
        Files.writeString(tempFile, badData);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> FileBackedTaskManager.loadFromFile(tempFile),
                "неверный статус задачи"
        );
    }

    @Test
    void load_ShouldThrowManagerSaveException_WhenFileDoesNotExist() {
        Path nonExistentFile = Paths.get("non_existent_file.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(nonExistentFile);
        ManagerSaveException exception = Assertions.assertThrows(
                ManagerSaveException.class,
                () -> FileBackedTaskManager.loadFromFile(nonExistentFile)
        );
        Assertions.assertEquals("Ошибка загрузки данных", exception.getMessage());
    }
}
