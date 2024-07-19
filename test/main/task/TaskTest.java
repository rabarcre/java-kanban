package main.task;

import main.manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void tasksWithSameIdAreEqual() {
        Task task1 = new Task("task1", "Description1", Status.NEW);
        Task task1Created = inMemoryTaskManager.createTask(task1);
        Task task2 = new Task("task1", "Description1", Status.NEW);
        Task task2Created = inMemoryTaskManager.createTask(task2);

        task1Created.setId(1);
        assertEquals(task2Created, task1Created, "Таски должны быть равны");
    }
}