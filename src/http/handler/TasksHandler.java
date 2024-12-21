package http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");

            if ("GET".equals(method)) {
                if (pathSegments.length == 2) {
                    List<Task> tasks = manager.getAllTasks();
                    String response = GSON.toJson(tasks);
                    sendResponseWithJson(exchange, 200, response);
                } else if (pathSegments.length == 3) {
                    int id = Integer.parseInt(pathSegments[2]);
                    Task task = manager.getTask(id);
                    if (task != null) {
                        String response = GSON.toJson(task);
                        sendResponseWithJson(exchange, 200, response);
                    } else {
                        sendNotFound(exchange);
                    }
                } else {
                    sendNotFound(exchange);
                }
            } else if ("POST".equals(method)) {
                if (pathSegments.length == 2) {
                    String body = readRequestBody(exchange);
                    Task task = GSON.fromJson(body, Task.class);
                    if (task == null) {
                        sendNotFound(exchange);
                        return;
                    }
                    if (task.getId() == 0) {
                        try {
                            manager.createTask(task);
                            Map<String, Object> responseData = new HashMap<>();
                            responseData.put("message", "Задача создана");
                            responseData.put("id", task.getId());
                            sendResponseWithJson(exchange, 201, GSON.toJson(responseData));
                        } catch (IllegalArgumentException e) {
                            sendHasInteractions(exchange);
                        }
                    } else {
                        if (manager.getTask(task.getId()) != null) {
                            try {
                                manager.updateTask(task);
                                sendResponseWithJson(exchange, 200, "Задача обновлена");
                            } catch (IllegalArgumentException e) {
                                sendHasInteractions(exchange);
                            }
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                } else {
                    sendNotFound(exchange);
                }
            } else if ("DELETE".equals(method)) {
                if (pathSegments.length == 2) {
                    manager.deleteAllTasks();
                    sendResponseWithJson(exchange, 200, "Все задачи удалены");
                } else if (pathSegments.length == 3) {
                    int id = Integer.parseInt(pathSegments[2]);
                    if (manager.getTask(id) != null) {
                        manager.deleteTask(id);
                        sendResponseWithJson(exchange, 200, "Задача удалена");
                    } else {
                        sendNotFound(exchange);
                    }
                } else {
                    sendNotFound(exchange);
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
            }
        } catch (NumberFormatException e) {
            sendResponseWithJson(exchange, 400, "Некорректный ID задачи");
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