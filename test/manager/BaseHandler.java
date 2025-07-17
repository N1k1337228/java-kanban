package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

public class BaseHandler {
    protected InMemoryTaskManager taskManager = new InMemoryTaskManager();
    protected HttpTaskServer taskServer = new HttpTaskServer(taskManager);

    public BaseHandler() throws IOException {
    }

    @BeforeEach
    void setUp() {
        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }
}
