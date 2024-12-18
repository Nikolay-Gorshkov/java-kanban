package http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.FileBackedTaskManager;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final FileBackedTaskManager manager;

    public HistoryHandler(FileBackedTaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            if ("GET".equals(method)) {
                // GET /history
                List<Task> history = manager.getHistory();
                sendResponseWithJson(exchange, 200, GSON.toJson(history));
            } else {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
            }
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void sendResponseWithJson(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] respBytes = response.getBytes();
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, respBytes.length);
        exchange.getResponseBody().write(respBytes);
        exchange.close();
    }
}
