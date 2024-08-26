package main.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasksList = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(Integer id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic(String name, String description, Status status, Duration duration, LocalDateTime localDateTime) {
        super(name, description, status, duration, localDateTime);
    }

    public Epic(Integer id, String name, String description, Status status, Duration duration, LocalDateTime localDateTime) {
        super(id, name, description, status, duration, localDateTime);
    }

    public void addSubtask(Subtask subtask) {
        subtasksList.add(subtask);
    }

    public List<Subtask> getSubtasksList() {
        return subtasksList;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    public void setSubtasksList(List<Subtask> subtasksList) {
        this.subtasksList = subtasksList;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", subtasksList=" + subtasksList +
                ", duration=" + duration +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
