package main.manager;

import main.exception.ManagerOverlappingException;
import main.exception.TaskExistsException;
import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import main.task.Task;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    private int nextTaskId;
    private int nextEpicId;
    private int nextSubtaskId;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));


    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    private void checkEpicTime(Epic epic) {
        if (epic.getSubtasksList().isEmpty()) {
            return;
        }
        List<Subtask> subtaskList = epic.getSubtasksList();
        if ((subtaskList.getFirst().getStartTime() != null) && (subtaskList.getLast().getEndTime() != null)) {
            subtaskList = subtaskList.stream()
                    .sorted(Comparator.comparing(Task::getStartTime)).toList();

            epic.setStartTime(subtaskList.getFirst().getStartTime());
            epic.setEndTime(subtaskList.getLast().getEndTime());
            for (Subtask subtask : subtaskList) {
                epic.setDuration(epic.getDuration().plus(subtask.getDuration()));
            }

        }
    }

    public boolean isNotOverlapping(Task task) {
        if (task.getStartTime() == null) {
            return true;
        }
        if (task.getDuration() == Duration.ZERO) {
            return true;
        }
        int sizeBefore = prioritizedTasks.size();
        prioritizedTasks.add(task);
        return prioritizedTasks.size() != sizeBefore;
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
        if (tasks.get(id) == null) {
            throw new TaskExistsException("Таски с id:" + id + "не существует");
        }
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        if (epics.get(id) == null) {
            throw new TaskExistsException("Эпика с id:" + id + "не существует");
        }
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        if (subtasks.get(id) == null) {
            throw new TaskExistsException("Сабтаски с id:" + id + "не существует");
        }
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }


    //Создание новых задач
    @Override
    public Task createTask(Task task) {
        task.setId(getNextTaskId());
        if (isNotOverlapping(task)) {
            tasks.put(task.getId(), task);
            return task;
        } else {
            throw new ManagerOverlappingException("Таски пересекаются: " + task.getName());
        }
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
        if (isNotOverlapping(subtask)) {
            epic.addSubtask(subtask);
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epic);
            checkEpicTime(epic);
            return subtask;
        } else {
            throw new ManagerOverlappingException("Таски пересекаются: " + subtask.getName());
        }
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
        checkEpicTime(epic);
        return subtask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (epic.getId() == null) {
            return null;
        }

        checkEpicTime(epic);
        epics.put(epic.getId(), epic);
        return epic;
    }


    //Удаление задач
    @Override
    public int deleteTask(int taskId) {
        Task task = tasks.get(taskId);
        if (tasks.get(taskId) == null) {
            throw new TaskExistsException("Таски с id:" + taskId + "не существует");
        }
        if (tasks.remove(taskId) != null) {
            prioritizedTasks.remove(task);
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteEpic(int epicId) {
        if (epics.get(epicId) == null) {
            throw new TaskExistsException("Эпика с id:" + epicId + "не существует");
        }
        List<Subtask> subtaskList = epics.get(epicId).getSubtasksList();
        for (Subtask subtask : subtaskList) {
            int id = subtask.getId();
            prioritizedTasks.remove(subtasks.get(id));
            subtasks.remove(id);
        }
        if (epics.remove(epicId) != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteSubtask(int subtaskId) {
        if (subtasks.get(subtaskId) == null) {
            throw new TaskExistsException("Сабтаски с id:" + subtaskId + "не существует");
        }
        Subtask subtask = subtasks.get(subtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        List<Subtask> subtaskList = epic.getSubtasksList();
        subtaskList.remove(subtaskId);
        epic.setSubtasksList(subtaskList);
        updateEpic(epic);
        checkEpicTime(epic);
        prioritizedTasks.remove(subtasks.get(subtaskId));
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
