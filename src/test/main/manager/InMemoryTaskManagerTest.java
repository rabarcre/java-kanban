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
        Task task1 = new Task("Test1 task1", "Description1", Status.NEW);
        Task task1Created = inMemoryTaskManager.createTask(task1);

        Task savedTask = inMemoryTaskManager.getTask(task1Created.getId());

        assertNotNull(savedTask, "Таска не найдена");
        assertEquals(task1Created, savedTask, "Таски не совпадают");

        List<Task> tasks = inMemoryTaskManager.getTasks();
        assertNotNull(tasks, "Таски не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество тасок");
        assertEquals(task1Created, tasks.get(0), "Таски не совпадают");
    }

    @Test
    void updateTask() {
        Task task1 = new Task("Test2 task1", "Description", Status.NEW);
        Task task1Created = inMemoryTaskManager.createTask(task1);
        Task updatedTask = new Task(0, "Test2 task1Updated", "UpdatedDescription", Status.DONE);
        Task updateTask = inMemoryTaskManager.updateTask(updatedTask);

        assertEquals(updatedTask, inMemoryTaskManager.getTask(0), "Таска не обновилась");
    }

    @Test
    void deleteTask() {
        Task task1 = new Task("Test3 task1", "Description", Status.NEW);
        Task task1Created = inMemoryTaskManager.createTask(task1);

        assertTrue(inMemoryTaskManager.deleteTask(0), "Таска не удалилась");
    }

    @Test
    void addNewEpic() {
        Epic epic1 = new Epic("Test4 Epic1","description",Status.NEW);
        Epic epic1created = inMemoryTaskManager.createEpic(epic1);

        Epic savedEpic = inMemoryTaskManager.getEpic(epic1created.getId());

        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic1created,savedEpic, "Эпики не совпадают");

        List<Epic> epics = inMemoryTaskManager.getEpics();
        assertNotNull(epics,"Эпики не возвращаются");
        assertEquals(1,epics.size(), "Неверное количество эпиков");
        assertEquals(epic1created,epics.get(0),"Эпики не совпадают");
    }

    @Test
    void addNewSubtask() {
        Epic epic1 = new Epic("Test5 Epic1","description",Status.NEW);
        Epic epic1created = inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Test5 Subtask1","description",Status.NEW,epic1created.getId());
        Subtask subtask1Created = inMemoryTaskManager.createSubtask(subtask1);

        Subtask savedSubtask = inMemoryTaskManager.getSubtask(subtask1Created.getId());

        assertNotNull(savedSubtask,"Сабтаск не найден");
        assertEquals(subtask1Created,savedSubtask, "Сабтаски не совпадают");

        List<Subtask> subtasks = inMemoryTaskManager.getSubtasks();
        assertNotNull(subtasks, "Сабтаски не возвращаются");
        assertEquals(1,subtasks.size(),"Неверное количество сабтасок");
        assertEquals(subtask1Created,subtasks.get(0), "Сабтаски не совпадают");

        assertNotNull(epic1created.getSubtasksList(), "Сабтаски не привязаны к эпику");
    }

    @Test
    void updateEpic(){
        Epic epic1 = new Epic("Test6 Epic1","description",Status.NEW);
        Epic epic1created = inMemoryTaskManager.createEpic(epic1);
        Epic updatedEpic = new Epic(epic1created.getId(), "UpdatedEpic1", "Updated description",
                Status.DONE);
        inMemoryTaskManager.updateEpic(updatedEpic);
        assertEquals(inMemoryTaskManager.getEpic(epic1created.getId()), updatedEpic, "Эпик не обновился");
    }

    @Test
    void updateSubtaskAndEpicStatus(){
        Epic epic1 = new Epic("Test7 Epic1","description",Status.NEW);
        Epic epic1created = inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Test7 Subtask1","description",Status.NEW,epic1created.getId());
        Subtask subtask1Created = inMemoryTaskManager.createSubtask(subtask1);
        Subtask updatedSubtask = new Subtask(subtask1Created.getId(),"UpdatedSubtask1",
                "UpdatedDescription", Status.DONE, epic1created.getId());
        inMemoryTaskManager.updateSubtask(updatedSubtask);

        Subtask savedSubtask = inMemoryTaskManager.getSubtask(subtask1Created.getId());
        assertEquals(savedSubtask,updatedSubtask, "Сабтаск не обновился");

        Epic savedEpic = inMemoryTaskManager.getEpic(0);
        assertEquals(savedEpic.getStatus(), savedEpic.getStatus(), "Статус эпика не обновился");
    }

    @Test
    void deleteSubtask(){
        Epic epic1 = new Epic("Test8 Epic1","description",Status.NEW);
        Epic epic1created = inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Test8 Subtask1","description",Status.NEW,epic1created.getId());
        Subtask subtask1Created = inMemoryTaskManager.createSubtask(subtask1);

        assertTrue(inMemoryTaskManager.deleteSubtask(subtask1Created.getId()), "Сабтаска не удалилась");

        Epic epic = inMemoryTaskManager.getEpic(0);
        List<Subtask> emptyList = new ArrayList<>();
        List<Subtask> subtaskList = epic.getSubtasksList();
        assertEquals(emptyList,subtaskList, "Сабтаск не удалён из эпика");
    }

    @Test
    void deleteEpicAndLinkedSubtasks(){
        Epic epic1 = new Epic("Test9 Epic1","description",Status.NEW);
        Epic epic1created = inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Test9 Subtask1","description",Status.NEW,epic1created.getId());
        Subtask subtask1Created = inMemoryTaskManager.createSubtask(subtask1);

        assertTrue(inMemoryTaskManager.deleteEpic(epic1created.getId()), "Эпик не удалился");
        List<Subtask> emptyList = new ArrayList<>();
        assertEquals(emptyList ,inMemoryTaskManager.getSubtasks(), "Связанные сабтаски не удалились");
    }
}