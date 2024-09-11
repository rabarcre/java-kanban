package main.http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.exception.ManagerOverlappingException;
import main.exception.RequestMethodException;
import main.exception.TaskExistsException;
import main.http.HttpTaskServer;
import main.manager.TaskManager;
import main.task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());

        switch (exchange.getRequestMethod()) {
            case "GET":
                try {
                    if (id == null) {
                        List<Subtask> subtasks = taskManager.getSubtasks();
                        String response = HttpTaskServer.getGson().toJson(subtasks);
                        sendText(exchange, response, 200);
                    } else {
                        Subtask subtask = taskManager.getSubtask(id);
                        String response = HttpTaskServer.getGson().toJson(subtask);
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
                    Subtask subtask = HttpTaskServer.getGson().fromJson(body, Subtask.class);
                    taskManager.updateSubtask(subtask);
                    if (id == null) {
                        sendText(exchange, "Сабтаск создан", 201);
                    } else {
                        sendText(exchange, "Сабтаск обновлён", 201);
                    }
                } catch (ManagerOverlappingException exception) {
                    sendHasInteractions(exchange, "Введённый эпик пересекается с созданным ранее");
                }
                break;

            case "DELETE":
                try {
                    if (id != null) {
                        taskManager.deleteSubtask(id);
                        sendText(exchange, "Сабтаск удалён", 200);
                    }
                } catch (TaskExistsException exception) {
                    sendNotFound(exchange, "Сабтаск не найден");
                }
                break;

            default:
                throw new RequestMethodException("Метод " + exchange.getRequestMethod() + " не существует");
        }
    }
}