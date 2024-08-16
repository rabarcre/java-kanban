package main.manager;

import main.exception.ManagerSaveException;
import main.formatter.CSVFormatter;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static main.formatter.CSVFormatter.HEADER;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public static final String PATH_TO_FILE = "./src";
    public static final String FILENAME_CSV = "tasks.csv";
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }


    @Override
    public Task createTask(Task task) {
        Task rtrn = super.createTask(task);
        save();
        return rtrn;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic rtrn = super.createEpic(epic);
        save();
        return rtrn;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask rtrn = super.createSubtask(subtask);
        save();
        return rtrn;
    }

    @Override
    public Task updateTask(Task task) {
        Task rtrn = super.updateTask(task);
        save();
        return rtrn;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask rtrn = super.updateSubtask(subtask);
        save();
        return rtrn;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic rtrn = super.updateEpic(epic);
        save();
        return rtrn;
    }

    @Override
    public int deleteTask(int taskId) {
        int rtrn = super.deleteTask(taskId);
        save();
        return rtrn;
    }

    @Override
    public int deleteEpic(int epicId) {
        int rtrn = super.deleteEpic(epicId);
        save();
        return rtrn;
    }

    @Override
    public int deleteSubtask(int subtaskId) {
        int rtrn = super.deleteSubtask(subtaskId);
        save();
        return rtrn;
    }

    //Методы менеджера


    //Сохранение
    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            file = new File(PATH_TO_FILE, FILENAME_CSV);
            if (!Files.exists(file.toPath())) {
                Files.createFile(Paths.get(PATH_TO_FILE, FILENAME_CSV));
            }

            bw.write(HEADER);
            bw.newLine();

            for (Task task : getTasks()) {
                bw.write(CSVFormatter.toString(task) + "\n");
            }

            for (Epic epic : getEpics()) {
                bw.write(CSVFormatter.toString(epic) + "\n");
            }

            for (Subtask subtask : getSubtasks()) {
                bw.write(CSVFormatter.toString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении: " + e.getMessage());
        }
    }


    //Загрузка
    public void loadFromFile() {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (int i = 1; i < lines.size(); i++) {
                final Task task = CSVFormatter.fromString(lines.get(i));
                final int id = task.getId();

                if (task instanceof Subtask subtask) {
                    getSubtasksMap().put(id, subtask);
                    Epic epic = getEpicsMap().get(subtask.getEpicId());
                    epic.addSubtask(subtask);
                } else if (task instanceof Epic epic) {
                    getEpicsMap().put(id, epic);
                } else {
                    getTasksMap().put(id, task);
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при загрузке: " + e.getMessage());
        }
    }


}

