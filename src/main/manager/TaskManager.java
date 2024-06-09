package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    // Получение значений задач
    List<Task> getTasks();
    List<Epic> getEpics();
    List<Subtask> getSubtasks();

    // Просмотр отдельных задач
    Task getTask(int id);
    Epic getEpic(int id);
    Subtask getSubtask(int id);

    //Создание новых задач
    Task createTask(Task task);
    Epic createEpic(Epic epic);
    Subtask createSubtask(Subtask subtask);

    //Обновление задач
    Task updateTask(Task task);
    Subtask updateSubtask(Subtask subtask);
    Epic updateEpic(Epic epic);

    //Удаление задач
    boolean isDeletedTask(int taskId);
    boolean isDeletedEpic(int epicId);
    boolean isDeletedSubtask(int subtaskId);
}
