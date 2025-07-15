package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;


import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    protected InMemoryTaskManager manager;

    public HistoryHandler(InMemoryTaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        sendText(exchange, manager.getHistory());
    }

}
