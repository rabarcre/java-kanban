package main.http;

import com.google.gson.Gson;
import main.manager.InMemoryTaskManager;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer httpTaskServer;
    Gson gson = HttpTaskServer.getGson();

    @BeforeEach
    public void start() throws IOException {
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @AfterEach
    public void stop() {
        httpTaskServer.stop();
    }

    @Test
    public void addTasks() throws IOException, InterruptedException {
        Task task = new Task(0, "Test1", "desription",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа не совпадает");

        List<Task> taskFromManager = taskManager.getTasks();

        assertEquals(1, taskFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void updateTask() throws IOException, InterruptedException {
        Task task = new Task(0, "Test2", "desription",
                Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 0));
        taskManager.createTask(task);

        Task updatedTask = new Task(0, "Test2", "desription2",
                Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 11, 0));
        String updatedTaskJson = gson.toJson(updatedTask);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа не совпадает");
        assertEquals(updatedTask, taskManager.getTask(0), "Задача не обновилась");
    }

    @Test
    public void deleteTask() throws IOException, InterruptedException {
        Task task = new Task(0, "Test3", "desription",
                Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 0));
        taskManager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/0");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        assertEquals(0, taskManager.getTasks().size(), "Задача не удалена");
    }

    @Test
    public void addEpicAndSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Test4", "description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 0));
        Subtask subtask = new Subtask(0, "Test4", "description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 10), 0);

        String epicJson = gson.toJson(epic);
        String subtaskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI uriEpic = URI.create("http://localhost:8080/epics");
        URI uriSubtask = URI.create("http://localhost:8080/subtasks");

        HttpRequest requestEpic = HttpRequest.newBuilder()
                .uri(uriEpic)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        HttpResponse<String> responseEpic = client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestSubtask = HttpRequest.newBuilder()
                .uri(uriSubtask)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> responseSubtask = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, responseEpic.statusCode(), "Код ответа не совпадает");
        assertEquals(201, responseSubtask.statusCode(), "Код ответа не совпадает");

        assertEquals(1, taskManager.getEpics().size(), "Некорректное количество задач");
        assertEquals(1, taskManager.getSubtasks().size(), "Некорректное количество задач");
    }

    @Test
    public void updateEpicAndSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Test5", "description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 0));
        Subtask subtask = new Subtask(0, "Test5", "description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 10), 0);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Epic epicUpdated = new Epic(0, "Test5", "description1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 0));
        Subtask subtaskUpdated = new Subtask(0, "Test5", "description1", Status.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 10), 0);

        String epicJson = gson.toJson(epicUpdated);
        String subtaskJson = gson.toJson(subtaskUpdated);

        HttpClient client = HttpClient.newHttpClient();
        URI uriEpic = URI.create("http://localhost:8080/epics/0");
        URI uriSubtask = URI.create("http://localhost:8080/subtasks");

        HttpRequest requestEpic = HttpRequest.newBuilder()
                .uri(uriEpic)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        HttpResponse<String> responseEpic = client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestSubtask = HttpRequest.newBuilder()
                .uri(uriSubtask)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> responseSubtask = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());

        System.out.println(taskManager.getEpics());
        assertEquals(201, responseEpic.statusCode(), "Код ответа не совпадает");
        assertEquals(201, responseSubtask.statusCode(), "Код ответа не совпадает");
        assertEquals(subtaskUpdated, taskManager.getSubtask(0), "Сабтаска не обновилась");
        assertEquals("description1", taskManager.getEpic(0).getDescription(), "Эпик не обновился");
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(0).getStatus(), "Статус у эпика не обновился");
    }

    @Test
    public void deleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Test6", "description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 0));
        Subtask subtask = new Subtask(0, "Test6", "description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 10), 0);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI uriEpic = URI.create("http://localhost:8080/epics/0");

        HttpRequest requestEpic = HttpRequest.newBuilder()
                .uri(uriEpic)
                .DELETE()
                .build();
        HttpResponse<String> responseEpic = client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseEpic.statusCode(), "Код ответа не совпадает");
        assertEquals(0, taskManager.getEpics().size(), "Эпик не удалился");
        assertEquals(0, taskManager.getSubtasks().size(), "Сабтаски не удалились при удалении эпика");
    }

    @Test
    public void deleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Test7", "description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 0));
        Subtask subtask = new Subtask(0, "Test7", "description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 10), 0);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI uriSubtask = URI.create("http://localhost:8080/subtasks/0");

        HttpRequest requestSubtask = HttpRequest.newBuilder()
                .uri(uriSubtask)
                .DELETE()
                .build();
        HttpResponse<String> responseSubtask = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseSubtask.statusCode(), "Код ответа не совпадает");
        assertEquals(0, taskManager.getSubtasks().size(), "Сабтаска не удалилась");
        assertEquals(0, taskManager.getEpic(0).getSubtasksList().size(), "Сабтаска не удалилась у эпика");
    }

    @Test
    public void returnHistory() throws IOException, InterruptedException {
        Task task = new Task(0, "Test8", "desription",
                Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 0));

        Task task2 = new Task(0, "Test8", "desription2",
                Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 11, 0));

        taskManager.createTask(task);
        taskManager.createTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/history");

        HttpRequest requestSubtask = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
    }

    @Test
    public void returnPrioritized() throws IOException, InterruptedException {
        Task task = new Task(0, "Test9", "desription",
                Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 0));

        Task task2 = new Task(0, "Test9", "desription2",
                Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 11, 0));

        taskManager.createTask(task);
        taskManager.createTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/prioritizedTasks");

        HttpRequest requestSubtask = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
    }
}