package main;

import main.manager.Managers;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import main.task.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        testTasks();
    }


    private static void testTasks() {
        TaskManager taskManager = Managers.getDefault();

        System.out.println("Тест 1: Отсутствие тасок");
        List<Task> tasks = taskManager.getTasks();
        System.out.println("Таски отсутствуют :" + tasks.isEmpty() + "\n");

        System.out.println("Тест 2: Создание таски");
        Task task1 = new Task("Убраться", "убрать квартиру", Status.NEW);
        Task task2 = new Task("Отдохнуть", "описание", Status.IN_PROGRESS);
        Task task1Created = taskManager.createTask(task1);
        Task task2Created = taskManager.createTask(task2);
        System.out.println("Таски присутствуют:\n" + taskManager.getTasks() + "\n");

        System.out.println("Тест 3: Обновление тасок");
        Task task3 = new Task(task1Created.getId(), "Погулять", "Сходить в лес", Status.IN_PROGRESS);
        Task task3Updated = taskManager.updateTask(task3);
        System.out.println("У обновлённой таски должны быть обновлённыее поля:\n" + taskManager.getTasks() + "\n");

        System.out.println("Тест 4: Удаление тасок");
        boolean isDeleted = taskManager.DeleteTask(task3Updated.getId()) == 1;
        System.out.println("Удаление прошло успешно: " + isDeleted);
        System.out.println("Удалённая таска должна отсутствовать:\n" + taskManager.getTasks() + "\n");

        System.out.println("Тест 5: Отсутствие эпика");
        List<Epic> epics = taskManager.getEpics();
        System.out.println("Эпики отсутствуют:" + epics.isEmpty() + "\n");

        System.out.println("Тест 6: Отсутствие сабтаска");
        List<Subtask> subtasks = taskManager.getSubtasks();
        System.out.println("Сабтаски отсутствуют:" + subtasks.isEmpty() + "\n");

        System.out.println("Тест 7: Создание эпика");
        Epic epic1 = new Epic("Эпик1", "Описание Эпика1", Status.NEW);
        Epic epic1Created = taskManager.createEpic(epic1);
        System.out.println("Эпики присутствуют:\n" + taskManager.getEpics() + "\n");

        System.out.println("Тест 8: Создание и добавление сабтасок в эпик");
        Subtask subtask1 = new Subtask("Сабтаск1", "Описание сабтаск1", Status.NEW, epic1Created.getId());
        Subtask subtask1Created = taskManager.createSubtask(subtask1);
        System.out.println("Эпик с сабтаском: " + taskManager.getEpics());
        System.out.println("Сабтаска: " + taskManager.getSubtasks() + "\n");

        System.out.println("Тест 9: Обновление эпика и статуса эпика");
        Epic epic2 = new Epic(epic1Created.getId(), "Обновлённый Эпик", " Обновлённое описание Эпика", Status.IN_PROGRESS);
        Epic epic2Updated = taskManager.updateEpic(epic2);
        System.out.println("Эпик должен обновится:\n" + taskManager.getEpics() + "\n");

        System.out.println("Тест 10: Обновление сабтаска");
        Subtask subtask2 = new Subtask(subtask1Created.getId(), "Обновлённый Сабтаск", "Обновлённое описание Сабтаски", Status.DONE, epic1Created.getId());
        Subtask subtask2Updated = taskManager.updateSubtask(subtask2);
        System.out.println(taskManager.getEpics());
        System.out.println("Обновлённый сабтаск: " + taskManager.getSubtasks() + "\n");

        System.out.println("Тест 11: Удаление сабтаски");
        isDeleted = taskManager.DeleteSubtask(subtask2Updated.getId()) == 1;
        System.out.println("Удаление сабтаски прошло успешно: " + isDeleted);
        System.out.println("Удалённая сабтаска должна отсутствовать: " + taskManager.getSubtasks() + "\n");

        System.out.println("Тест 12: Удаление эпика");
        isDeleted = taskManager.DeleteEpic(epic2Updated.getId()) == 1;
        System.out.println("Удаление эпика прошло успешно: " + isDeleted);
        System.out.println("Удалённый эпик должен отсутствовать: " + taskManager.getEpics() + "\n");

        System.out.println("Тест 13: Удаление эпика с сабтасками");
        Epic epic3 = new Epic("Эпик3", "Описание Эпик3", Status.DONE);
        Epic epic3Created = taskManager.createEpic(epic3);
        Subtask subtask3 = new Subtask("Сабтаск3", "Описание Сабтаск3", Status.DONE, epic3Created.getId());
        Subtask subtask3Created = taskManager.createSubtask(subtask3);
        System.out.println("Эпик создан: " + taskManager.getEpics());
        isDeleted = taskManager.DeleteEpic(epic3Created.getId()) == 1;
        System.out.println("Удаление эпика прошло успешно: " + isDeleted);
        System.out.println("Удалённый эпик должен отсутствовать: " + taskManager.getEpics());
        System.out.println("Сабтаска эпика должна отсутствовать: " + taskManager.getSubtasks() + "\n");

    }
}
