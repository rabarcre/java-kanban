package main.task;


import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private Integer id;
    private String name;
    private String description;
    private Status status;
    protected Duration duration;
    protected LocalDateTime start;
    protected LocalDateTime end;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(Integer id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, Status status, Duration duration, LocalDateTime start) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.start = start;
        getEndTime();
    }

    public Task(Integer id, String name, String description, Status status, Duration duration, LocalDateTime start) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.start = start;
        getEndTime();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getEpicId() {
        return null;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
        this.end = start.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return start;
    }

    public void setStartTime(LocalDateTime start) {
        this.start = start;
        this.end = start.plus(duration);
    }

    public LocalDateTime getEndTime() {
        end = start.plus(duration);
        return end;
    }

    public void setEndTime(LocalDateTime end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
