package main.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.exception.ManagerOverlappingException;
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
            default:
                throw new RuntimeException();
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
                } catch (StringIndexOutOfBoundsException | NumberFormatException exception) {
                    sendNotFound(exchange, "Задача не найдена");
                } catch (Exception exception) {
                    sendInternalServerError(exchange);
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
                } catch (Exception exception) {
                    sendInternalServerError(exchange);
                }
                break;

            case "DELETE":
                try {
                    if (id != null) {
                        taskManager.deleteTask(id);
                        sendText(exchange, "Задача удалена", 200);
                    }
                } catch (StringIndexOutOfBoundsException | NumberFormatException exception) {
                    sendNotFound(exchange, "Задача не найдена");
                } catch (Exception exception) {
                    sendInternalServerError(exchange);
                }
                break;
        }
    }
}
