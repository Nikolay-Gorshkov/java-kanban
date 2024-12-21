package http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Subtask;
import service.TaskManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");

            switch (method) {
                case "GET":
                    if (pathSegments.length == 2) {
                        List<Subtask> subtasks = manager.getAllSubtasks();
                        sendResponseWithJson(exchange, 200, GSON.toJson(subtasks));
                    } else if (pathSegments.length == 3) {
                        int id = Integer.parseInt(pathSegments[2]);
                        Subtask subtask = manager.getSubtask(id);
                        if (subtask != null) {
                            sendResponseWithJson(exchange, 200, GSON.toJson(subtask));
                        } else {
                            sendNotFound(exchange);
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                case "POST":
                    if (pathSegments.length == 2) {
                        String body = readRequestBody(exchange);
                        Subtask subtask = GSON.fromJson(body, Subtask.class);
                        if (subtask == null) {
                            sendNotFound(exchange);
                            return;
                        }
                        if (subtask.getId() == 0) {
                            try {
                                manager.createSubtask(subtask);
                                Map<String, Object> responseData = new HashMap<>();
                                responseData.put("message", "Подзадача создана");
                                responseData.put("id", subtask.getId());
                                sendResponseWithJson(exchange, 201, GSON.toJson(responseData));
                            } catch (IllegalArgumentException e) {
                                sendHasInteractions(exchange);
                            }
                        } else {
                            if (manager.getSubtask(subtask.getId()) != null) {
                                try {
                                    manager.updateSubtask(subtask);
                                    sendResponseWithJson(exchange, 200, "Подзадача обновлена");
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
                    break;

                case "DELETE":
                    if (pathSegments.length == 2) {
                        manager.deleteAllSubtasks();
                        sendResponseWithJson(exchange, 200, "Все подзадачи удалены");
                    } else if (pathSegments.length == 3) {
                        int id = Integer.parseInt(pathSegments[2]);
                        if (manager.getSubtask(id) != null) {
                            manager.deleteSubtask(id);
                            sendResponseWithJson(exchange, 200, "Подзадача удалена");
                        } else {
                            sendNotFound(exchange);
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                default:
                    exchange.sendResponseHeaders(405, -1);
                    exchange.close();
            }
        } catch (NumberFormatException e) {
            sendResponseWithJson(exchange, 400, "Некорректный ID подзадачи");
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