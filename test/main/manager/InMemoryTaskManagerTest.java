package main.manager;

import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void addNewTask() {
        Task task = new Task("Test1 task", "Description1", Status.NEW);
        Task taskCreated = inMemoryTaskManager.createTask(task);

        Task savedTask = inMemoryTaskManager.getTask(taskCreated.getId());

        assertNotNull(savedTask, "Таска не найдена");
        assertEquals(taskCreated, savedTask, "Таски не совпадают");

        List<Task> tasks = inMemoryTaskManager.getTasks();
        assertNotNull(tasks, "Таски не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество тасок");
        assertEquals(taskCreated, tasks.get(0), "Таски не совпадают");
    }

    @Test
    void updateTask() {
        Task task = new Task("Test2 task", "Description", Status.NEW);
        Task taskCreated = inMemoryTaskManager.createTask(task);
        Task updatedTask = new Task(0, "Test2 taskUpdated", "UpdatedDescription", Status.DONE);
        Task updateTask = inMemoryTaskManager.updateTask(updatedTask);

        assertEquals(updatedTask, inMemoryTaskManager.getTask(0), "Таска не обновилась");
    }

    @Test
    void deleteTask() {
        Task task = new Task("Test3 task", "Description", Status.NEW);
        Task taskCreated = inMemoryTaskManager.createTask(task);

        assertEquals(1, inMemoryTaskManager.deleteTask(0), "Таска не удалилась");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Test4 epic", "description", Status.NEW);
        Epic epicCreated = inMemoryTaskManager.createEpic(epic);

        Epic savedEpic = inMemoryTaskManager.getEpic(epicCreated.getId());

        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epicCreated, savedEpic, "Эпики не совпадают");

        List<Epic> epics = inMemoryTaskManager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        assertEquals(epicCreated, epics.get(0), "Эпики не совпадают");
    }

    @Test
    void addNewSubtask() {
        Epic epic = new Epic("Test5 epic", "description", Status.NEW);
        Epic epicCreated = inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test5 Subtask", "description", Status.NEW, epicCreated.getId());
        Subtask subtaskCreated = inMemoryTaskManager.createSubtask(subtask);

        Subtask savedSubtask = inMemoryTaskManager.getSubtask(subtaskCreated.getId());

        assertNotNull(savedSubtask, "Сабтаск не найден");
        assertEquals(subtaskCreated, savedSubtask, "Сабтаски не совпадают");

        List<Subtask> subtasks = inMemoryTaskManager.getSubtasks();
        assertNotNull(subtasks, "Сабтаски не возвращаются");
        assertEquals(1, subtasks.size(), "Неверное количество сабтасок");
        assertEquals(subtaskCreated, subtasks.get(0), "Сабтаски не совпадают");

        assertNotNull(epicCreated.getSubtasksList(), "Сабтаски не привязаны к эпику");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("Test6 epic", "description", Status.NEW);
        Epic epicCreated = inMemoryTaskManager.createEpic(epic);
        Epic updatedEpic = new Epic(epicCreated.getId(), "Updatedepic", "Updated description",
                Status.DONE);
        inMemoryTaskManager.updateEpic(updatedEpic);
        assertEquals(inMemoryTaskManager.getEpic(epicCreated.getId()), updatedEpic, "Эпик не обновился");
    }

    @Test
    void updateSubtaskAndEpicStatus() {
        Epic epic = new Epic("Test7 epic", "description", Status.NEW);
        Epic epicCreated = inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test7 Subtask", "description", Status.NEW, epicCreated.getId());
        Subtask subtaskCreated = inMemoryTaskManager.createSubtask(subtask);
        Subtask updatedSubtask = new Subtask(subtaskCreated.getId(), "UpdatedSubtask",
                "UpdatedDescription", Status.DONE, epicCreated.getId());
        inMemoryTaskManager.updateSubtask(updatedSubtask);

        Subtask savedSubtask = inMemoryTaskManager.getSubtask(subtaskCreated.getId());
        assertEquals(savedSubtask, updatedSubtask, "Сабтаск не обновился");

        Epic savedEpic = inMemoryTaskManager.getEpic(0);
        assertEquals(savedEpic.getStatus(), savedEpic.getStatus(), "Статус эпика не обновился");
    }

    @Test
    void deleteSubtask() {
        Epic epic = new Epic("Test8 epic", "description", Status.NEW);
        Epic epicCreated = inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test8 Subtask", "description", Status.NEW, epicCreated.getId());
        Subtask subtaskCreated = inMemoryTaskManager.createSubtask(subtask);

        assertEquals(1, inMemoryTaskManager.deleteSubtask(subtaskCreated.getId()), "Сабтаска не удалилась");

        Epic epicInMemory = inMemoryTaskManager.getEpic(0);
        List<Subtask> emptyList = new ArrayList<>();
        List<Subtask> subtaskList = epicInMemory.getSubtasksList();
        assertEquals(emptyList, subtaskList, "Сабтаск не удалён из эпика");
    }

    @Test
    void deleteEpicAndLinkedSubtasks() {
        Epic epic = new Epic("Test9 epic", "description", Status.NEW);
        Epic epicCreated = inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test9 Subtask", "description", Status.NEW, epicCreated.getId());

        assertEquals(1, inMemoryTaskManager.deleteEpic(epicCreated.getId()), "Эпик не удалился");
        List<Subtask> emptyList = new ArrayList<>();
        assertEquals(emptyList, inMemoryTaskManager.getSubtasks(), "Связанные сабтаски не удалились");
    }
}