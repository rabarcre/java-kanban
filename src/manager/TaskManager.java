package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {

    private int nextTaskId;
    private int nextEpicId;
    private int nextSubtaskId;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();


    // Получение значений задач
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getSub() {
        return new ArrayList<>(subtasks.values());
    }


    //Создание новых задач
    public Task createTask(Task task) {
        task.setId(getNextTaskId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(getNextEpicId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSub(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        subtask.setId(getNextSubtaskId());
        epic.addSubtask(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        return subtask;
    }


    //Обновление статуса эпика
    public void updateEpicStatus(Epic epic) {
        List<Subtask> subtaskList = epic.getSubtasksList();

        int subtasksAmmount = 0;
        int statusNew = 0;
        int statusDone = 0;
        int statusInProgress = 0;

        for (Subtask subtask : subtaskList) {
            subtasksAmmount++;

            if (subtask.getStatus() == Status.NEW) {
                statusNew++;
            } else if (subtask.getStatus() == Status.DONE) {
                statusDone++;
            } else if (subtask.getStatus() == Status.IN_PROGRESS) {
                statusInProgress++;
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
    public Task updateTask(Task task) {
        if (task.getId() == null) {
            return null;
        }

        tasks.put(task.getId(), task);
        return task;
    }

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

    public Epic updateEpic(Epic epic) {
        if (epic.getId() == null) {
            return null;
        }

        epics.put(epic.getId(), epic);
        return epic;
    }


    //Удаление задач
    public boolean deleteTask(int taskId) {
        return tasks.remove(taskId) != null;
    }

    public boolean deleteEpic(int epicId){
        List<Subtask> subtaskList = epics.get(epicId).getSubtasksList();
        for (Subtask subtask: subtaskList){
            int id = subtask.getId();
            subtasks.remove(id);
        }
        return epics.remove(epicId) != null;
    }

    public boolean deleteSubtask(int subtaskId){
        Subtask subtask = subtasks.get(subtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        List<Subtask> subtaskList = epic.getSubtasksList();
        subtaskList.remove(subtaskId);
        epic.setSubtasksList(subtaskList);
        updateEpic(epic);
        return subtasks.remove(subtaskId) != null;
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
