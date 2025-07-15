package manager;

import com.sun.net.httpserver.HttpServer;
import handlers.*;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer server;
    public final InMemoryTaskManager taskManager;

    public HttpTaskServer(InMemoryTaskManager manager) throws IOException {
        this.taskManager = manager;
        this.server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/tasks/", new TaskHandler(taskManager));
        server.createContext("/subtasks/", new SubTasksHandler(taskManager));
        server.createContext("/epics/", new EpicHandler(taskManager));
        server.createContext("/history/", new HistoryHandler(taskManager));
        server.createContext("/prioritized/", new PrioritizedHandler(taskManager));
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

    public static void main(String[] args) throws IOException {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
    }
}
