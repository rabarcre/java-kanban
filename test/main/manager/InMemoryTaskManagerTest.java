package main.manager;

import main.exception.ManagerOverlappingException;
import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
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

    @Test
    void startAndEndTimeMustBeDifferent() {
        Epic epic = new Epic("Test10 epic", "description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 0));
        Epic epicCreated = inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test10 Subtask", "description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 1), epicCreated.getId());
        Subtask subtaskCreated = inMemoryTaskManager.createSubtask(subtask);

        assertNotEquals(epicCreated.getStartTime(), epicCreated.getEndTime(),
                "Время за которое выполнился таск одинаковое");
    }

    @Test
    void shouldFindOverlappingTasks() {
        Task task = new Task("Test11 task", "Description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 10, 0));
        Task taskCreated = inMemoryTaskManager.createTask(task);
        assertThrows(ManagerOverlappingException.class, () -> {
            Task taskOverlap = inMemoryTaskManager.createTask(task);
        }, "Можно создать несколько тасок с одинаковым временем");
    }

    @Test
    void shouldReturnFormattedList() {
        Task task2 = new Task("Test11 task", "Description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 9, 0));
        Task task3 = new Task("Test11 task", "Description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 10, 12, 0));
        Task task1 = new Task("Test11 task", "Description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2020, 12, 9, 10, 0));
        Task taskCreated2 = inMemoryTaskManager.createTask(task2);
        Task taskCreated3 = inMemoryTaskManager.createTask(task3);
        Task taskCreated1 = inMemoryTaskManager.createTask(task1);

        List<Task> sortedList = new ArrayList<>(inMemoryTaskManager.getPrioritizedTasks());
        Task firstSortedTask = sortedList.get(0);
        Task secondSortedTask = sortedList.get(1);
        Task thirdSortedTask = sortedList.get(2);

        assertEquals(taskCreated1, firstSortedTask,
                "Список неправильно отсортирован, первый элемент должен быть:\n" + taskCreated1
                        + "\n Первый элемент в данном списке: \n" + firstSortedTask);
        assertEquals(taskCreated2, secondSortedTask,
                "Список неправильно отсортирован, второй элемент должен быть:\n" + taskCreated2
                        + "\n Второй элемент в данном списке: \n" + secondSortedTask);
        assertEquals(taskCreated3, thirdSortedTask,
                "Список неправильно отсортирован, третий элемент должен быть:\n" + taskCreated3
                        + "\n Третий элемент в данном списке: \n" + thirdSortedTask);

    }
}