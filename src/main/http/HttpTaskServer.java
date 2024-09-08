package main.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import main.http.adapter.DurationAdapter;
import main.http.adapter.LocalDateTimeAdapter;
import main.http.handler.EpicHandler;
import main.http.handler.HistoryHandler;
import main.http.handler.PrioritizedTasksHandler;
import main.http.handler.SubtaskHandler;
import main.http.handler.TaskHttpHandler;
import main.manager.Managers;
import main.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static final int PORT = 8080;

    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TaskHttpHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedTasksHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));

    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }
}
