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

public class FileBackedTaskManagerTest {
    FileBackedTaskManager fileBackedTaskManager;
    Path file;
    Path tempFile;

    @BeforeEach
    public void createManagers() throws IOException {
        file = Files.createTempFile("testFile", ".csv");
        fileBackedTaskManager = new FileBackedTaskManager(file);
    }

    @Test
    public void saveEmptyFileAndLoadEmptyFile() {
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertTrue(fileBackedTaskManager1.taskMap.isEmpty());
        Assertions.assertTrue(fileBackedTaskManager1.epicMap.isEmpty());
        Assertions.assertTrue(fileBackedTaskManager1.subTaskMap.isEmpty());

    }

    @Test
    public void saveSomeTasks() throws IOException  {
        Task task1 = new Task("1", "aaa",10);
        task1.setStartTime(LocalDateTime.of(2021,4,12,3,15));
        task1.setEndTime(LocalDateTime.of(2021,4,12,3,25));
        fileBackedTaskManager.createTask(task1);
        Task task2 = new Task("2", "bbb",20);
        task2.setStartTime(LocalDateTime.of(2021,5,11,5,20));
        task2.setEndTime(LocalDateTime.of(2021,5,11,5,40));
        fileBackedTaskManager.createTask(task2);
        Task task3 = new Task("3", "ccc",30);
        task3.setStartTime(LocalDateTime.of(2021,9,3,13,25));
        task3.setEndTime(LocalDateTime.of(2021,9,3,13,55));
        fileBackedTaskManager.createTask(task3);
        List<String> lines = new ArrayList<>(Files.readAllLines(fileBackedTaskManager.getPath()));
        Assertions.assertEquals(4, lines.size());
        Assertions.assertEquals(fileBackedTaskManager.getTask(1).toString().trim(), lines.get(1));
        Assertions.assertEquals(fileBackedTaskManager.getTask(2).toString().trim(), lines.get(2));
        Assertions.assertEquals(fileBackedTaskManager.getTask(3).toString().trim(), lines.get(3));
    }

    @Test
    public void loadTasksTest() {
        fileBackedTaskManager.createTask(new Task("1", "aaa",150));
        fileBackedTaskManager.createSubTask(new SubTask("3", "ccc", 2,25));
        fileBackedTaskManager.createEpic(new Epic("2", "bbb"));
        FileBackedTaskManager fileLoad = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(fileBackedTaskManager.getTask(1),fileLoad.getTask(1));
        Assertions.assertEquals(fileBackedTaskManager.getEpic(2),fileLoad.getEpic(2));
        Assertions.assertEquals(fileBackedTaskManager.getSubTask(3),fileLoad.getSubTask(3));
    }

    @Test
    void loadFromFileShouldThrowWhenMalformedData() throws IOException {
        tempFile =  Files.createTempFile("tasks", ".csv");
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
