package task;

public class Task {
    private Integer id;
    private String name;
    private String descriprion;
    private Status status;

    public Task(String name, String descriprion, Status status) {
        this.name = name;
        this.descriprion = descriprion;
        this.status = status;
    }

    public Task(Integer id, String name, String descriprion, Status status) {
        this.id = id;
        this.name = name;
        this.descriprion = descriprion;
        this.status = status;
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

    public String getDescriprion() {
        return descriprion;
    }

    public void setDescriprion(String descriprion) {
        this.descriprion = descriprion;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", descriprion='" + descriprion + '\'' +
                ", status=" + status +
                '}';
    }
}
