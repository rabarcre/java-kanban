package main.manager;

import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import main.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private int nextTaskId;
    private int nextEpicId;
    private int nextSubtaskId;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();


    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //Получение Map задач
    public Map<Integer, Task> getTasksMap() {
        return tasks;
    }

    public Map<Integer, Epic> getEpicsMap() {
        return epics;
    }

    public Map<Integer, Subtask> getSubtasksMap() {
        return subtasks;
    }


    // Получение значений задач
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }


    // Просмотр отдельных задач
    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }


    //Создание новых задач
    @Override
    public Task createTask(Task task) {
        task.setId(getNextTaskId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(getNextEpicId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        subtask.setId(getNextSubtaskId());
        epic.addSubtask(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        return subtask;
    }


    //Обновление статуса эпика
    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtaskList = epic.getSubtasksList();

        int subtasksAmmount = 0;
        int statusNew = 0;
        int statusDone = 0;

        for (Subtask subtask : subtaskList) {
            subtasksAmmount++;

            if (subtask.getStatus() == Status.NEW) {
                statusNew++;
            } else if (subtask.getStatus() == Status.DONE) {
                statusDone++;
            }
        }
        if (subtasksAmmount == statusNew) {
            epic.setStatus(Status.NEW);
        } else if (subtasksAmmount == statusDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }


    //Обновление задач
    @Override
    public Task updateTask(Task task) {
        if (task.getId() == null) {
            return null;
        }

        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        if (subtask.getId() == null) {
            return null;
        }

        Epic epic = epics.get(subtask.getEpicId());
        List<Subtask> subtaskList = new ArrayList<>();

        subtaskList.add(subtask);
        epic.setSubtasksList(subtaskList);

        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        return subtask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (epic.getId() == null) {
            return null;
        }

        epics.put(epic.getId(), epic);
        return epic;
    }


    //Удаление задач
    @Override
    public int deleteTask(int taskId) {
        if (tasks.remove(taskId) != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteEpic(int epicId) {
        List<Subtask> subtaskList = epics.get(epicId).getSubtasksList();
        for (Subtask subtask : subtaskList) {
            int id = subtask.getId();
            subtasks.remove(id);
        }
        if (epics.remove(epicId) != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteSubtask(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        List<Subtask> subtaskList = epic.getSubtasksList();
        subtaskList.remove(subtaskId);
        epic.setSubtasksList(subtaskList);
        updateEpic(epic);
        if (subtasks.remove(subtaskId) != null) {
            return 1;
        }
        return 0;
    }


    private int getNextTaskId() {
        return nextTaskId++;
    }

    private int getNextEpicId() {
        return nextEpicId++;
    }

    private int getNextSubtaskId() {
        return nextSubtaskId++;
    }
}
