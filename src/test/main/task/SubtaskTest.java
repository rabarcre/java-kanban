package main.task;

import main.manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void tasksWithSameIdAreEqual() {
        Epic epic1 = new Epic("epic1", "Description1", Status.NEW);
        Epic epic1Created = inMemoryTaskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("subtask1", "Description1", Status.NEW,epic1Created.getId());
        Subtask subtask1Created = inMemoryTaskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask1", "Description1", Status.NEW, subtask1.getEpicId());
        Subtask subtask2Created = inMemoryTaskManager.createSubtask(subtask2);

        subtask1Created.setId(1);
        assertEquals(subtask2Created,subtask1Created, "Сабтаски должны быть равны");
    }

}