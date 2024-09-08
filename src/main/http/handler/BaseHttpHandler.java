package main.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {

    protected void sendText(HttpExchange h, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected Integer getIdFromPath(String path) {
        String[] parts = path.split("/");
        if (parts.length >= 3) {
            return Integer.parseInt(parts[2]);
        }
        return null;
    }

    protected void sendNotFound(HttpExchange httpExchange, String text) throws IOException {
        sendText(httpExchange, text, 404);
    }

    protected void sendHasInteractions(HttpExchange httpExchange, String text) throws IOException {
        sendText(httpExchange, text, 406);
    }

    protected void sendInternalServerError(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, "Внутренняя ошибка сервера", 500);
    }

}
