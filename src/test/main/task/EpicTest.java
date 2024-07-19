package main.task;

import main.manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void tasksWithSameIdAreEqual() {
        Epic epic1 = new Epic("epic1", "Description1", Status.NEW);
        Epic epic1Created = inMemoryTaskManager.createEpic(epic1);
        Epic epic2 = new Epic("epic1", "Description1", Status.NEW);
        Epic epic2Created = inMemoryTaskManager.createEpic(epic2);

        epic1Created.setId(1);
        assertEquals(epic2Created, epic1Created, "Эпики должны быть равны");
    }
}