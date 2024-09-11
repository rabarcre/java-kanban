package main.formatter;

import main.exception.TypeExistsException;
import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class CSVFormatter {
    public static final String HEADER = "id,type,name,status,description,epicId";

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder()
                .append(task.getId()).append(",")
                .append(task.getTaskType()).append(",")
                .append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",")
                .append(task.getDuration()).append(",")
                .append(task.getStartTime());

        if (task.getTaskType() != null) {
            if (task.getTaskType().equals(TaskType.SUBTASK)) {
                sb.append(",").append(task.getEpicId());
            }
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
        Duration duration = Duration.parse(parts[5]);
        LocalDateTime localDateTime = LocalDateTime.parse(parts[6]);

        switch (type) {
            case TASK:
                return new Task(id, name, description, status, duration, localDateTime);
            case SUBTASK:
                Subtask subtask = new Subtask(name, description, status, duration, localDateTime);
                subtask.setId(id);
                subtask.setEpicId(Integer.parseInt(parts[7]));
                return subtask;
            case EPIC:
                Epic epic = new Epic(name, description, status, duration, localDateTime);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            default:
                throw new TypeExistsException("Тип задачи " + type + " не существует");
        }
    }

}
