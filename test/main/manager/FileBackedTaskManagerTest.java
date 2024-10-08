package main.manager;

import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static main.manager.FileBackedTaskManager.PATH_TO_FILE;
import static main.manager.FileBackedTaskManager.FILENAME_CSV;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {
    private File file;
    private FileBackedTaskManager backedTaskManager;
    private final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();


    @BeforeEach
    public void before() {
        file = new File(PATH_TO_FILE, FILENAME_CSV);
        backedTaskManager = new FileBackedTaskManager(file);
    }

    @AfterEach
    public void after() {
        file.delete();
    }

    @Test
    void shouldBeSameData() {
        Task task = new Task("Тест1", "Описание Тест1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2020, 12, 10, 10, 0));
        Task savedTask = backedTaskManager.createTask(task);
        assertEquals(task.getName(), savedTask.getName(), "Поле Name в таске не совпадает");
        assertEquals(task.getDescription(), savedTask.getDescription(), "Поле Description в таске не совпадает");
        assertEquals(task.getStatus(), savedTask.getStatus(), "Поле Status в таске не совпадает");
        assertEquals(task.getStartTime(), savedTask.getStartTime(), "Поле StartTime в таске не совпадает");
        assertEquals(task.getEndTime(), savedTask.getEndTime(), "Поле EndTime в таске не совпадает");
        assertEquals(task.getDuration(), savedTask.getDuration(), "Поле Duration в таске не совпадает");


        Epic epic = new Epic("Тест1", "Описание Тест1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2020, 12, 10, 10, 1));
        Epic savedEpic = backedTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Тест1", "Описание Тест1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2020, 12, 10, 10, 2), savedEpic.getId());
        Subtask savedSubtask = backedTaskManager.createSubtask(subtask);
        assertEquals(epic.getName(), savedEpic.getName(), "Поле Name в эпике не совпадает");
        assertEquals(epic.getDescription(), savedEpic.getDescription(), "Поле Description в эпике не совпадает");
        assertEquals(epic.getStartTime(), savedEpic.getStartTime(), "Поле StartTime в эпике не совпадает");
        assertEquals(epic.getEndTime(), savedEpic.getEndTime(), "Поле EndTime в эпике не совпадает");
        assertEquals(epic.getDuration(), savedEpic.getDuration(), "Поле Duration в эпике не совпадает");

        assertEquals(subtask.getName(), savedSubtask.getName(), "Поле Name в сабтаске не совпадает");
        assertEquals(subtask.getDescription(), savedSubtask.getDescription(), "Поле Description в сабтаске не совпадает");
        assertEquals(subtask.getEpicId(), savedSubtask.getEpicId(), "Поле EpicId в сабтаске не совпадает");
        assertEquals(subtask.getStartTime(), savedSubtask.getStartTime(), "Поле StartTime в сабтаске не совпадает");
        assertEquals(subtask.getEndTime(), savedSubtask.getEndTime(), "Поле EndTime в сабтаске не совпадает");
        assertEquals(subtask.getDuration(), savedSubtask.getDuration(), "Поле Duration в сабтаске не совпадает");
    }

    @Test
    void loadFromFile() {
        Task task = new Task("Тест2", "Описание Тест2", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2020, 12, 10, 10, 0));
        Task savedTask = backedTaskManager.createTask(task);
        Epic epic = new Epic("Тест2", "Описание Тест2", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2020, 12, 10, 10, 1));
        Epic savedEpic = backedTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Тест2", "Описание Тест2", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2020, 12, 10, 10, 22), savedEpic.getId());
        Subtask savedSubtask = backedTaskManager.createSubtask(subtask);

        Task taskWSave = inMemoryTaskManager.createTask(task);
        Epic epicWSave = inMemoryTaskManager.createEpic(epic);
        Subtask subtaskWSave = inMemoryTaskManager.createSubtask(subtask);

        backedTaskManager.loadFromFile();

        assertEquals(1, backedTaskManager.getTasksMap().size(), "Загрузилось неправильное количество тасок");
        assertEquals(1, backedTaskManager.getEpicsMap().size(), "Загрузилось неправильное количество эпиков");
        assertEquals(1, backedTaskManager.getSubtasksMap().size(), "Загрузилось неправильное количество сабтасок");

        assertEquals(savedTask, backedTaskManager.getTasksMap().get(0), "Поля тасок до загрузки и после не совпадают");
        assertEquals(savedEpic, backedTaskManager.getEpicsMap().get(0), "Поля эпиков до загрузки и после не совпадают");
        assertEquals(savedSubtask, backedTaskManager.getSubtasksMap().get(0), "Поля сабтасок до загрузки и после не совпадают");
    }

}