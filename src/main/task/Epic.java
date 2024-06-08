package main.task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasksList = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(String name, String description, Status status, List<Subtask> subtasksList) {
        super(name, description, status);
        this.subtasksList = subtasksList;
    }

    public Epic(Integer id, String name, String description, Status status, List<Subtask> subtasksList) {
        super(id, name, description, status);
        this.subtasksList = subtasksList;
    }

    public Epic(Integer id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public void addSubtask(Subtask subtask){
        subtasksList.add(subtask);
    }

    public List<Subtask> getSubtasksList() {
        return subtasksList;
    }

    public void setSubtasksList(List<Subtask> subtasksList) {
        this.subtasksList = subtasksList;
    }

    @Override
    public String toString() {
        return "Epic{"+
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", subtasksList=" + subtasksList +
                '}';
    }
}
