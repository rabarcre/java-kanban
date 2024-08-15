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

public class FileBackedTaskManager extends InMemoryTaskManager {

    public final static String PATH_TO_FILE = "./src";
    public final static String FILENAME_CSV = "tasks.csv";
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
    public int DeleteTask(int taskId) {
        int rtrn = super.DeleteTask(taskId);
        save();
        return rtrn;
    }

    @Override
    public int DeleteEpic(int epicId) {
        int rtrn = super.DeleteEpic(epicId);
        save();
        return rtrn;
    }

    @Override
    public int DeleteSubtask(int subtaskId) {
        int rtrn = super.DeleteSubtask(subtaskId);
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

            bw.write(CSVFormatter.getHeader());
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
                    subtasks.put(id, subtask);
                    Epic epic = epics.get(subtask.getEpicId());
                    epic.addSubtask(subtask);
                } else if (task instanceof Epic epic) {
                    epics.put(id, epic);
                } else {
                    tasks.put(id, task);
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при загрузке: " + e.getMessage());
        }
    }


}

