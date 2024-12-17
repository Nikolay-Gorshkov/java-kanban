package http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.Subtask;
import service.FileBackedTaskManager;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final FileBackedTaskManager manager;

    public EpicsHandler(FileBackedTaskManager manager) {
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
                    // GET /epics
                    List<Epic> epics = manager.getAllEpics();
                    sendResponseWithJson(exchange, 200, GSON.toJson(epics));
                } else if (pathSegments.length == 3) {
                    // GET /epics/{id}
                    int id = Integer.parseInt(pathSegments[2]);
                    Epic epic = manager.getEpic(id);
                    if (epic != null) {
                        sendResponseWithJson(exchange, 200, GSON.toJson(epic));
                    } else {
                        sendNotFound(exchange);
                    }
                } else if (pathSegments.length == 4 && "subtasks".equals(pathSegments[3])) {
                    // GET /epics/{id}/subtasks
                    int epicId = Integer.parseInt(pathSegments[2]);
                    Epic epic = manager.getEpic(epicId);
                    if (epic != null) {
                        List<Subtask> subtasks = manager.getSubtasksByEpicId(epicId);
                        sendResponseWithJson(exchange, 200, GSON.toJson(subtasks));
                    } else {
                        sendNotFound(exchange);
                    }
                } else {
                    sendNotFound(exchange);
                }
            } else if ("POST".equals(method)) {
                if (pathSegments.length == 2) {
                    String body = readRequestBody(exchange);
                    Epic epic = GSON.fromJson(body, Epic.class);
                    if (epic == null) {
                        sendNotFound(exchange);
                        return;
                    }
                    if (epic.getId() == 0) {
                        // Создание нового эпика
                        manager.createEpic(epic);
                        sendCreated(exchange);
                    } else {
                        // Обновление
                        if (manager.getEpic(epic.getId()) != null) {
                            manager.updateEpic(epic);
                            sendResponseWithJson(exchange, 200, "Эпик обновлен");
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                } else {
                    sendNotFound(exchange);
                }
            } else if ("DELETE".equals(method)) {
                if (pathSegments.length == 3) {
                    int id = Integer.parseInt(pathSegments[2]);
                    if (manager.getEpic(id) != null) {
                        manager.deleteEpic(id);
                        sendResponseWithJson(exchange, 200, "Эпик удален");
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
            sendResponseWithJson(exchange, 400, "Некорректный ID эпика");
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