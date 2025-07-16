package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handlers.DurationAdapter;
import handlers.LocalDateTimeAdapter;
import handlers.TasksListTypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclasses.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TaskHandlerTest {
    private final InMemoryTaskManager taskManager = new InMemoryTaskManager();
    private final HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public TaskHandlerTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("task", "description");
        String taskJson = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertFalse(taskManager.getTaskList().isEmpty(), "Задача не добавлена в менеджер");
        Task savedTask = taskManager.getTask(1);
        assertNotNull(savedTask, "Задача не найдена в менеджере");
        assertEquals("task", savedTask.getName(), "Название задачи не совпадает");
    }

    @Test
    void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Test task", "Test description");
        taskManager.createTask(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + task.getId()))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task receivedTask = gson.fromJson(response.body(), Task.class);
        assertEquals(task.getId(), receivedTask.getId(), "ID задачи не совпадает");
        assertEquals(task.getName(), receivedTask.getName(), "Название задачи не совпадает");
    }

    @Test
    void testGetAllTasks() throws IOException, InterruptedException {
        Task task = new Task("Task", "Description");
        taskManager.createTask(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        ArrayList<Task> receivedTask = gson.fromJson(response.body(), new TasksListTypeToken().getType());
        assertEquals(1, receivedTask.size());
    }

    @Test
    public void testDeleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("123", "456");
        taskManager.createTask(task);
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/" + task.getId()))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(taskManager.getTaskList().isEmpty());
    }
}