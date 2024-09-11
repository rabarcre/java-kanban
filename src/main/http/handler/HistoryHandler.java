package main.http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.http.HttpTaskServer;
import main.manager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;


    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            String historyList = HttpTaskServer.getGson().toJson(taskManager.getHistory());
            sendText(exchange, historyList, 200);
        } else {
            System.out.println("Возможен только GET метод");
            sendInternalServerError(exchange);
        }
    }
}
