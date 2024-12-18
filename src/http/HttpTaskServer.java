package http;

import com.sun.net.httpserver.HttpServer;
import http.handler.*;
import service.FileBackedTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer server;
    private final FileBackedTaskManager manager;

    public HttpTaskServer(FileBackedTaskManager manager, int port) throws IOException {
        this.manager = manager;
        server = HttpServer.create(new InetSocketAddress(port), 0);
        initContext();
        server.setExecutor(null);
    }

    private void initContext() {
        server.createContext("/tasks", new TasksHandler(manager));
        server.createContext("/epics", new EpicsHandler(manager));
        server.createContext("/subtasks", new SubtasksHandler(manager));
        server.createContext("/history", new HistoryHandler(manager));
        server.createContext("/prioritized", new PrioritizedTasksHandler(manager));
    }

    public void start() {
        server.start();
        System.out.println("Server started on port " + server.getAddress().getPort());
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped.");
    }
}