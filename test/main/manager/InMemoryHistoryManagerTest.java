package main.manager;

import main.task.Epic;
import main.task.Status;
import main.task.Task;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private static List<Task> historyList;


    @Test
    void historyAdd() {
        Task task = new Task(0, "Test1", "desription", Status.NEW);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.getTask(0);
        Epic epic = new Epic(0, "Test1", "desription", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.getEpic(0);

        historyList = inMemoryTaskManager.getHistory();
        assertFalse(historyList.isEmpty(), "История не сохраняется");
    }

    @Test
    void maxHistory() {
        for (int i = 0; i <= 8; i++) {
            Task task = new Task(i, "Test2", "desription", Status.NEW);
            inMemoryTaskManager.createTask(task);
            inMemoryTaskManager.getTask(i);
            Epic epic = new Epic(i, "Test2", "desription", Status.NEW);
            inMemoryTaskManager.createEpic(epic);
            inMemoryTaskManager.getEpic(i);
        }
        historyList = inMemoryTaskManager.getHistory();
        assertTrue(historyList.size() >= 10, "В истории сохранено менее 10 тасок:" + historyList.size());
    }

    @Test
    void taskAutoDeletion() {
        Task task = new Task(0, "Test3", "desription", Status.NEW);
        inMemoryTaskManager.createTask(task);
        for (int i = 0; i <= 5; i++) {
            inMemoryTaskManager.getTask(0);
        }
        historyList = inMemoryTaskManager.getHistory();
        assertEquals(1, historyList.size(), "В истории сохранены повторные обращения к таске");
    }

    @Test
    void taskDeletion() {
        Task task = new Task(0, "Test4", "desription", Status.NEW);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.getTask(0);
        Epic epic = new Epic(0, "Test4", "desription", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.getEpic(0);
        inMemoryTaskManager.getEpic(0);

        historyList = inMemoryTaskManager.getHistory();
        assertEquals(2, historyList.size(), "В истории сохранены повторные обращения к таске");

    }

}