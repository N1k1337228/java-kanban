package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handlers.DurationAdapter;
import handlers.LocalDateTimeAdapter;
import handlers.SubTastTypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclasses.Epic;
import taskclasses.SubTask;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskHandlerTest {
    Epic epic;
    private HttpTaskServer taskServer;
    private InMemoryTaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager(); // Используем InMemoryTaskManager
        taskServer = new HttpTaskServer(taskManager);
        epic = new Epic("333", "444");
        taskManager.createEpic(epic);
        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("subTask", "Description", epic.getId());
        subTask.setStartTime(LocalDateTime.of(2023, 5, 1, 10, 10));
        String taskJson = gson.toJson(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertFalse(taskManager.getSubTaskList().isEmpty(), "Задача не добавлена в менеджер");
        SubTask savedSubTask = taskServer.taskManager.getSubTask(2);
        assertNotNull(savedSubTask, "Задача не найдена в менеджере");
        assertEquals("subTask", savedSubTask.getName(), "Название задачи не совпадает");
    }

    @Test
    public void NotAddSubTaskTest() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("SubTask", "Description", epic.getId());
        subTask.setStartTime(LocalDateTime.of(2023, 5, 1, 10, 10));
        SubTask subTask1 = new SubTask("SubTask1", "Description", epic.getId());
        subTask1.setStartTime(LocalDateTime.of(2023, 5, 1, 10, 10));
        taskManager.createSubTask(subTask1);
        String taskJson = gson.toJson(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
    }

    @Test
    void testGetAllSubTasks() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("subTask", "Description", epic.getId());
        subTask.setStartTime(LocalDateTime.of(2023, 5, 1, 10, 10));
        taskManager.createSubTask(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        ArrayList<SubTask> receivedEpic = gson.fromJson(response.body(), new SubTastTypeToken().getType());
        assertEquals(1, receivedEpic.size());
    }

    @Test
    void testGetSubTaskById() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("SubTask", "Description", epic.getId());
        subTask.setStartTime(LocalDateTime.of(2023, 5, 1, 10, 10));
        taskManager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("SubTask1", "Description", epic.getId());
        subTask1.setStartTime(LocalDateTime.of(2023, 6, 2, 11, 15));
        taskManager.createSubTask(subTask1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subTask.getId()))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный статус-код ответа");
        SubTask receivedSubTask = gson.fromJson(response.body(), SubTask.class);
        assertNotNull(receivedSubTask, "Подзадача не найдена");
        assertEquals(subTask.getId(), receivedSubTask.getId(), "ID не совпадает");
        assertEquals(epic.getId(), receivedSubTask.getEpicId(), "ID эпика не совпадает");
    }

    @Test
    public void testDeleteTaskById() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("123", "456", epic.getId());
        taskManager.createTask(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:8080/subtasks/" + subTask.getId()))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(taskManager.getSubTaskList().isEmpty());
    }
}
