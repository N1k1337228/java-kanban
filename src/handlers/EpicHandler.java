package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import taskclasses.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    protected TaskManager manager;

    public EpicHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (getHttpEndPoint(exchange)) {
            case "getEpics":
                sendText(exchange, manager.getEpicList());
                break;
            case "postEpic":
                postEpicHandler(exchange);
                break;
            case "getEpicsId":
                getEpicIdHandler(exchange);
                break;
            case "deleteEpicsId":
                deleteEpicHandler(exchange);
                break;
            case "getSubTasksToEpicId":
                getSubTasksToEpicId(exchange);
                break;
            default:
                sendNotFound(exchange);
                break;
        }
    }

    private void getEpicIdHandler(HttpExchange exchange) throws IOException {
        if (getId(exchange).isEmpty()) {
            sendNotFound(exchange);
            return;
        }
        Epic epic = manager.getEpic(getId(exchange).get());
        if (epic == null) {
            sendNotFound(exchange);
            return;
        }
        sendText(exchange, epic);
    }

    private void postEpicHandler(HttpExchange exchange) throws IOException {
        Epic epic = readRequestBody(exchange);

        if (epic.getId() == 0) {
            manager.createEpic(epic);
            sendGoodResponse(exchange, 201);
        }
    }

    private void deleteEpicHandler(HttpExchange exchange) throws IOException {
        if (getId(exchange).isEmpty()) {
            sendNotFound(exchange);
            return;
        }
        int id = getId(exchange).get();
        manager.removeEpicOnId(id);
        sendGoodResponse(exchange, 200);
    }

    void getSubTasksToEpicId(HttpExchange exchange) throws IOException {
        if (getId(exchange).isEmpty() || manager.getEpic(getId(exchange).get()) == null) {
            sendNotFound(exchange);
            return;
        }
        int id = getId(exchange).get();
        sendText(exchange, manager.getSubTaskListOfEpicOnId(id));
    }

    private String getHttpEndPoint(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] pathEl = path.split("/");
        if (pathEl.length == 2) {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    return "getEpics";
                case "POST":
                    return "postEpic";
            }
        }
        if (pathEl.length == 3) {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    return "getEpicsId";
                case "DELETE":
                    return "deleteEpicsId";
            }
        }
        if (pathEl.length == 4) {
            return "getSubTasksToEpicId";
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

    protected Epic readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            return gson.fromJson(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), Epic.class);
        }
    }
}
