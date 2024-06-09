package main.manager;

import main.task.Epic;
import main.task.Status;
import main.task.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private static List <Task> historyList = inMemoryTaskManager.getHistory();



    @Test
    void historyCheck() {
        for (int i = 0; i <= 5; i++) {
            Task task = new Task(i, "Test" + i, "desription", Status.NEW);
            inMemoryTaskManager.createTask(task);
            inMemoryTaskManager.getTask(i);
            Epic epic = new Epic(i, "Test" + i, "desription", Status.NEW);
            inMemoryTaskManager.createEpic(epic);
            inMemoryTaskManager.getEpic(i);
        }
        assertFalse(historyList.isEmpty(), "История не сохраняется");
    }

    @Test
    void maxHistory(){
        for (int i = 0; i <= 5; i++) {
            Task task = new Task(i, "Test" + i, "desription", Status.NEW);
            inMemoryTaskManager.createTask(task);
            inMemoryTaskManager.getTask(i);
            Epic epic = new Epic(i, "Test" + i, "desription", Status.NEW);
            inMemoryTaskManager.createEpic(epic);
            inMemoryTaskManager.getEpic(i);
        }
        assertTrue(historyList.size() <= 10, "В истории сохранено более 10 тасок");
    }

    @Test
    void lastTask(){
        for (int i = 0; i <= 5; i++) {
            Task task = new Task(i, "Test" + i, "desription", Status.NEW);
            inMemoryTaskManager.createTask(task);
            inMemoryTaskManager.getTask(i);
            Epic epic = new Epic(i, "Test" + i, "desription", Status.NEW);
            inMemoryTaskManager.createEpic(epic);
            inMemoryTaskManager.getEpic(i);
        }
        Epic testEpicLast = inMemoryTaskManager.getEpic(5);
        assertEquals(testEpicLast, historyList.get(9), "Последние таски не сохраняются");
    }

    @Test
    void firstTask(){
        for (int i = 0; i <= 5; i++) {
            Task task = new Task(i, "Test" + i, "desription", Status.NEW);
            inMemoryTaskManager.createTask(task);
            inMemoryTaskManager.getTask(i);
            Epic epic = new Epic(i, "Test" + i, "desription", Status.NEW);
            inMemoryTaskManager.createEpic(epic);
            inMemoryTaskManager.getEpic(i);
        }
        Epic testEpicFirst = inMemoryTaskManager.getEpic(1);
        assertEquals(testEpicFirst,historyList.get(0),"Первые таски не удаляются");
    }

}