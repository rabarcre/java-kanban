package main.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
    }

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, Duration duration, LocalDateTime localDateTime) {
        super(name, description, status, duration, localDateTime);
    }

    public Subtask(String name, String description, Status status, Duration duration, LocalDateTime localDateTime, int epicId) {
        super(name, description, status, duration, localDateTime);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, Status status, Duration duration, LocalDateTime localDateTime, int epicId) {
        super(id, name, description, status, duration, localDateTime);
        this.epicId = epicId;
    }

    @Override
    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", epicId=" + epicId +
                ", duration=" + duration +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
