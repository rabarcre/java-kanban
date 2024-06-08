package main.manager;

import main.task.Epic;
import main.task.Status;
import main.task.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void TasksAddInHistoryAndCheckingHistory() {
        for (int i = 0; i <= 5; i++) {
            Task task = new Task(i, "Test" + i, "desription", Status.NEW);
            inMemoryTaskManager.createTask(task);
            inMemoryTaskManager.getTask(i);
            Epic epic = new Epic(i, "Test" + i, "desription", Status.NEW);
            inMemoryTaskManager.createEpic(epic);
            inMemoryTaskManager.getEpic(i);
        }
        List <Task> historyList = inMemoryTaskManager.getHistory();
        assertFalse(historyList.isEmpty(), "История не сохраняется");
        assertTrue(historyList.size() <= 10, "В истории сохранено более 10 тасок");
        Epic testEpicLast = inMemoryTaskManager.getEpic(5);
        assertEquals(testEpicLast, historyList.get(9), "Последние таски не сохраняются");
        Task testTaskFirst = inMemoryTaskManager.getTask(2);
        assertEquals(testTaskFirst,historyList.get(0),"Первые таски не удаляются");
    }

}