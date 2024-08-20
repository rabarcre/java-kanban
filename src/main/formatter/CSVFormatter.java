package main.formatter;

import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

public class CSVFormatter {
    public static final String HEADER = "id,type,name,status,description,epicId";

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder()
                .append(task.getId()).append(",")
                .append(task.getTaskType()).append(",")
                .append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",");

        if (task.getTaskType().equals(TaskType.SUBTASK)) {
            sb.append(",").append(task.getEpicId());
        }

        return sb.toString();
    }

    public static Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];

        if (type.equals(TaskType.TASK)) {
            return new Task(id, name, description, status);
        } else if (type.equals(TaskType.SUBTASK)) {
            Subtask subtask = new Subtask(name, description, status);
            subtask.setId(id);
            subtask.setEpicId(Integer.parseInt(parts[6]));
            return subtask;
        } else {
            Epic epic = new Epic(name, description, status);
            epic.setId(id);
            epic.setStatus(status);
            return epic;
        }
    }

}
