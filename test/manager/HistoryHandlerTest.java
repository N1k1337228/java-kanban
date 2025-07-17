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

public class HistoryHandlerTest extends BaseHandler {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public HistoryHandlerTest() throws IOException {
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {
        Task task = new Task("Task", "Description");
        taskManager.createTask(task);
        taskManager.getTask(task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history/"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        ArrayList<Task> receivedTask = gson.fromJson(response.body(), new TasksListTypeToken().getType());
        assertEquals(1, receivedTask.size());
    }
}
