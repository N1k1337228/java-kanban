package manager;

import Exceptons.ManagerSaveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTaskManagerTest {
    FileBackedTaskManager fileBackedTaskManager;
    Path file;

    @BeforeEach
    public void createManagers() {
        try {
            file = Files.createTempFile("testFile", ".csv");
            fileBackedTaskManager = new FileBackedTaskManager(file);
        } catch (IOException e) {
            throw new ManagerSaveException();
        }

    }

    @Test
    public void saveEmptyFileAndLoadEmptyFile() {
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertTrue(fileBackedTaskManager1.taskMap.isEmpty());
        Assertions.assertTrue(fileBackedTaskManager1.epicMap.isEmpty());
        Assertions.assertTrue(fileBackedTaskManager1.subTaskMap.isEmpty());
        // сохранение пустого файла проверить нельзя, т.к. метод save() приватный, а также при использовании методов
        // которые его вызывают, файл уже не будет пустым.
    }

    @Test
    public void saveSomeTasks() {
        fileBackedTaskManager.createTask(new Task("1", "aaa"));
        fileBackedTaskManager.createTask(new Task("2", "bbb"));
        fileBackedTaskManager.createTask(new Task("3", "ccc"));
        try {
            List<String> lines = new ArrayList<>(Files.readAllLines(fileBackedTaskManager.getPath()));
            Assertions.assertEquals(3, lines.size());
            Assertions.assertEquals(fileBackedTaskManager.getTask(1).toString().trim(), lines.get(0));
            Assertions.assertEquals(fileBackedTaskManager.getTask(2).toString().trim(), lines.get(1));
            Assertions.assertEquals(fileBackedTaskManager.getTask(3).toString().trim(), lines.get(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadTasksTest() {
        fileBackedTaskManager.createTask(new Task("1", "aaa"));
        fileBackedTaskManager.createEpic(new Epic("2", "bbb"));
        fileBackedTaskManager.createSubTask(new SubTask("3", "ccc", 2));
        FileBackedTaskManager fileLoad = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertFalse(fileLoad.epicMap.isEmpty());
        Assertions.assertFalse(fileLoad.taskMap.isEmpty());
        Assertions.assertFalse(fileLoad.subTaskMap.isEmpty());
    }

}
