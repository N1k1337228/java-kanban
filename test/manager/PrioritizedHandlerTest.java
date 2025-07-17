package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handlers.DurationAdapter;
import handlers.LocalDateTimeAdapter;
import handlers.TasksListTypeToken;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrioritizedHandlerTest extends BaseHandler {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public PrioritizedHandlerTest() throws IOException {
    }

    @Test
    public void getPrioritizedListOfTasksTest() throws IOException, InterruptedException {
        Task task = new Task("Task", "Description");
        task.setStartTime(LocalDateTime.of(2023, 6, 2, 11, 15));
        taskManager.createTask(task);
        Task task1 = new Task("Task", "Description");
        task1.setStartTime(LocalDateTime.of(2020, 4, 12, 3, 55));
        taskManager.createTask(task1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized/"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        ArrayList<Task> receivedTask = gson.fromJson(response.body(), new TasksListTypeToken().getType());
        assertEquals(2, receivedTask.size());
    }
}
