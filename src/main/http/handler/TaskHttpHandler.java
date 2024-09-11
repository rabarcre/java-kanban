package main.http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.exception.ManagerOverlappingException;
import main.exception.RequestMethodException;
import main.exception.TaskExistsException;
import main.http.HttpTaskServer;
import main.manager.TaskManager;
import main.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public TaskHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());

        switch (exchange.getRequestMethod()) {
            case "GET":
                try {
                    if (id == null) {
                        List<Task> tasks = taskManager.getTasks();
                        String response = HttpTaskServer.getGson().toJson(tasks);
                        sendText(exchange, response, 200);
                    } else {
                        Task task = taskManager.getTask(id);
                        String response = HttpTaskServer.getGson().toJson(task);
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
                    Task task = HttpTaskServer.getGson().fromJson(body, Task.class);
                    if (id == null) {
                        taskManager.createTask(task);
                        sendText(exchange, "Задача создана", 201);
                    } else {
                        taskManager.updateTask(task);
                        sendText(exchange, "Задача обновлена", 201);
                    }
                } catch (ManagerOverlappingException exception) {
                    sendHasInteractions(exchange, "Введённая задача пересекается с созданной ранее");
                }
                break;

            case "DELETE":
                try {
                    if (id != null) {
                        taskManager.deleteTask(id);
                        sendText(exchange, "Задача удалена", 200);
                    }
                } catch (TaskExistsException exception) {
                    sendNotFound(exchange, "Задача не найдена");
                }
                break;

            default:
                throw new RequestMethodException("Метод " + exchange.getRequestMethod() + " не существует");
        }
    }
}
