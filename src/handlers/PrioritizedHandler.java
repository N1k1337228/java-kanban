package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;
import manager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private  InMemoryTaskManager manager;

    public PrioritizedHandler(TaskManager manager) {
        this.manager = (InMemoryTaskManager) manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        sendText(exchange, manager.getPrioritizedTasks());
    }
}
