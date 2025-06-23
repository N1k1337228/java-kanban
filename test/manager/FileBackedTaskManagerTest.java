package manager;

import exceptons.ManagerSaveException;
import org.junit.jupiter.api.Assertions;
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
    Path file = Files.createTempFile("testFile", ".csv");
    Path tempFile;

    public FileBackedTaskManagerTest() throws IOException {
        super(new FileBackedTaskManager(Files.createTempFile("File", ".csv")));
        taskManager = new FileBackedTaskManager(file);
    }

    @Test
    public void saveEmptyFileAndLoadEmptyFile() throws IOException {
        Path emptyFile = Files.createTempFile("emptyFile", ".csv");
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(emptyFile);
        Assertions.assertTrue(fileBackedTaskManager1.taskMap.isEmpty());
        Assertions.assertTrue(fileBackedTaskManager1.epicMap.isEmpty());
        Assertions.assertTrue(fileBackedTaskManager1.subTaskMap.isEmpty());
    }

    @Test
    public void saveSomeTasks() throws IOException {
        List<String> lines = new ArrayList<>(Files.readAllLines(taskManager.getPath()));
        System.out.println(lines);
        Assertions.assertEquals(6, lines.size());
        Assertions.assertEquals(taskManager.getTask(1).toString().trim(), lines.get(1));
        Assertions.assertEquals(taskManager.getEpic(2).toString().trim(), lines.get(2));
        Assertions.assertEquals(taskManager.getSubTask(3).toString().trim(), lines.get(3));
        Assertions.assertEquals(taskManager.getSubTask(4).toString().trim(), lines.get(4));
        Assertions.assertEquals(taskManager.getSubTask(5).toString().trim(), lines.get(5));
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
