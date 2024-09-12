package main.http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.exception.ManagerOverlappingException;
import main.exception.RequestMethodException;
import main.exception.TaskExistsException;
import main.http.HttpTaskServer;
import main.manager.TaskManager;
import main.task.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());

        switch (exchange.getRequestMethod()) {
            case "GET":
                try {
                    if (id == null) {
                        List<Epic> epics = taskManager.getEpics();
                        String response = HttpTaskServer.getGson().toJson(epics);
                        sendText(exchange, response, 200);
                    } else {
                        Epic epic = taskManager.getEpic(id);
                        String response = HttpTaskServer.getGson().toJson(epic);
                        sendText(exchange, response, 200);
                    }
                } catch (TaskExistsException exception) {
                    sendNotFound(exchange, "Задача не найдена");
                }
                break;
            case "POST":
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                try {
                    Epic epic = HttpTaskServer.getGson().fromJson(body, Epic.class);
                    if (id == null) {
                        taskManager.createEpic(epic);
                        sendText(exchange, "Эпик создан", 201);
                    } else {
                        taskManager.updateEpic(epic);
                        sendText(exchange, "Эпик обновлён", 201);
                    }
                } catch (ManagerOverlappingException exception) {
                    sendHasInteractions(exchange, "Введённый эпик пересекается с созданным ранее");
                }
                break;

            case "DELETE":
                try {
                    if (id != null) {
                        taskManager.deleteEpic(id);
                        sendText(exchange, "Эпик удалён", 200);
                    }
                } catch (TaskExistsException exception) {
                    sendNotFound(exchange, "Эпик не найден");
                }
                break;

            default:
                throw new RequestMethodException("Метод " + exchange.getRequestMethod() + " не существует");
        }
    }
}
