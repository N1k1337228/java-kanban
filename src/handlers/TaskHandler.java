package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptons.NotFoundException;
import manager.TaskManager;
import taskclasses.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager manager;

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (getHttpEndPoint(exchange)) {
            case "getTasks":
                sendText(exchange, manager.getTaskList());
                break;
            case "postTasks":
                postTasksHandler(exchange);

                break;
            case "getTasksId":
                getTasksIdHandler(exchange);
                break;
            case "postTasksId":
                postTasksHandler(exchange);
                break;
            case "deleteTasksId":
                deleteTaskHandler(exchange);
                break;
            default:
                sendNotFound(exchange);
                break;
        }
    }

    private void getTasksIdHandler(HttpExchange exchange) throws IOException {
        if (getId(exchange).isEmpty()){
            sendNotFound(exchange);
            return;
        }
        Task task = manager.getTask(getId(exchange).get());
        if (task == null) {
            sendNotFound(exchange);
            return;
        }
        sendText(exchange, task);
    }

    private void postTasksHandler(HttpExchange exchange) throws IOException {
        Task task = readRequestBody(exchange);
        try {
            if (task.getId() == 0) {
                manager.createTask(task);
                sendGoodResponse(exchange, 201);
            } else {
                manager.updateTask(task);
                sendGoodResponse(exchange, 201);
            }
        } catch (NotFoundException e) {
            sendHasInteractions(exchange);
        }

    }

    private void deleteTaskHandler(HttpExchange exchange) throws IOException {
        if (getId(exchange).isEmpty()) {
            sendNotFound(exchange);
            return;
        }
        int id = getId(exchange).get();
        manager.removeTaskOnId(id);
        sendGoodResponse(exchange, 200);
    }

    private String getHttpEndPoint(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] pathEl = path.split("/");
        if (pathEl.length == 2) {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    return "getTasks";
                case "POST":
                    return "postTasks";
            }
        }
        if (pathEl.length == 3) {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    return "getTasksId";
                case "POST":
                    return "postTasksId";
                case "DELETE":
                    return "deleteTasksId";
            }
        }
        return "unknown";
    }

    private Optional<Integer> getId(HttpExchange exchange) {
        String[] elPath = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(elPath[2]));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    protected Task readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            return gson.fromJson(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), Task.class);
        }
    }
}
