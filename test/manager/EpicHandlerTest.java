package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handlers.DurationAdapter;
import handlers.EpicsTypeToken;
import handlers.LocalDateTimeAdapter;
import handlers.SubTastTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskclasses.Epic;
import taskclasses.SubTask;
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

public class EpicHandlerTest extends BaseHandler {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public EpicHandlerTest() throws IOException {
    }

    @Test
    void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        String taskJson = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertFalse(taskManager.getEpicList().isEmpty(), "Задача не добавлена в менеджер");
        Task savedTask = taskManager.getEpic(1);
        assertNotNull(savedTask, "Задача не найдена в менеджере");
        assertEquals("epic", savedTask.getName(), "Название задачи не совпадает");
    }

    @Test
    void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("123", "456");
        taskManager.createEpic(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epic.getId()))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic receivedTask = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic.getId(), receivedTask.getId(), "ID задачи не совпадает");
        assertEquals(epic.getName(), receivedTask.getName(), "Название задачи не совпадает");
    }

    @Test
    void testGetAllEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("123", "456");
        taskManager.createEpic(epic);
        Epic epic1 = new Epic("126", "496");
        taskManager.createEpic(epic1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        ArrayList<Epic> receivedEpic = gson.fromJson(response.body(), new EpicsTypeToken().getType());
        assertEquals(2, receivedEpic.size());
    }

    @Test
    void testGetAllSubtasksOnEpicId() throws IOException, InterruptedException {
        Epic epic = new Epic("123", "456");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask", "Description", epic.getId());
        subTask.setStartTime(LocalDateTime.of(2023, 5, 1, 10, 10));
        taskManager.createSubTask(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        ArrayList<SubTask> receivedEpic = gson.fromJson(response.body(), new SubTastTypeToken().getType());
        assertEquals(1, receivedEpic.size());
    }

    @Test
    public void testDeleteTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("123", "456");
        taskManager.createEpic(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:8080/epics/" + epic.getId()))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(taskManager.getEpicList().isEmpty());
    }
}
