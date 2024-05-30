package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasksList = new ArrayList<>();

    public Epic(String name, String descriprion, Status status) {
        super(name, descriprion, status);
    }

    public Epic(String name, String descriprion, Status status, List<Subtask> subtasksList) {
        super(name, descriprion, status);
        this.subtasksList = subtasksList;
    }

    public Epic(Integer id, String name, String descriprion, Status status, List<Subtask> subtasksList) {
        super(id, name, descriprion, status);
        this.subtasksList = subtasksList;
    }

    public Epic(Integer id, String name, String descriprion, Status status) {
        super(id, name, descriprion, status);
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
                ", descriprion='" + getDescriprion() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", subtasksList=" + subtasksList +
                '}';
    }
}
