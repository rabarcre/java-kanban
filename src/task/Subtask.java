package task;

public class Subtask extends Task {
    int epicId;


    public Subtask(String name, String descriprion, Status status, int epicId) {
        super(name, descriprion, status);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String descriprion, Status status, int epicId) {
        super(id, name, descriprion, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", descriprion='" + getDescriprion() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}
