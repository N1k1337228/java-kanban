package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptons.NotFoundException;

import manager.TaskManager;
import taskclasses.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubTasksHandler extends BaseHttpHandler implements HttpHandler {
    protected TaskManager manager;

    public SubTasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (getHttpEndPoint(exchange)) {
            case "getSubTasks":
                sendText(exchange, manager.getSubTaskList());
                break;
            case "postSubTasks":
                postSubTasksHandler(exchange);

                break;
            case "getSubTasksId":
                getSubTasksIdHandler(exchange);
                break;
            case "postSubTasksId":
                postSubTasksHandler(exchange);
                break;
            case "deleteSubTasksId":
                deleteSubTaskHandler(exchange);
                break;
            default:
                sendNotFound(exchange);
                break;
        }
    }


    private void getSubTasksIdHandler(HttpExchange exchange) throws IOException {
        if (getId(exchange).isEmpty()) {
            sendNotFound(exchange);
            return;
        }
        SubTask subTask = manager.getSubTask(getId(exchange).get());
        if (subTask == null) {
            sendNotFound(exchange);
            return;
        }
        sendText(exchange, subTask);
    }

    private void postSubTasksHandler(HttpExchange exchange) throws IOException {
        SubTask subTask = readRequestBody(exchange);
        try {
            if (subTask.getId() == 0) {
                manager.createSubTask(subTask);
                sendGoodResponse(exchange, 201);
            } else {
                manager.updateSabTask(subTask);
                sendGoodResponse(exchange, 201);
            }
        } catch (NotFoundException e) {
            sendHasInteractions(exchange);
        }
    }

    private void deleteSubTaskHandler(HttpExchange exchange) throws IOException {
        if (getId(exchange).isEmpty()) {
            sendNotFound(exchange);
            return;
        }
        int id = getId(exchange).get();
        manager.removeSubTaskOnId(id);
        sendGoodResponse(exchange, 200);
    }


    private String getHttpEndPoint(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] pathEl = path.split("/");

        if (pathEl.length == 2) {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    return "getSubTasks";
                case "POST":
                    return "postSubTasks";
            }
        }
        if (pathEl.length == 3) {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    return "getSubTasksId";
                case "POST":
                    return "postSubTasksId";
                case "DELETE":
                    return "deleteSubTasksId";
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

    protected SubTask readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            return gson.fromJson(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), SubTask.class);
        }
    }
}
