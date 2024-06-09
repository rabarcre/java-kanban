package main.manager;

import main.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
   List<Task> taskList = new ArrayList<>();
   final int historyMaxSize = 10;

    @Override
    public void add(Task task){
        if (taskList.size() >= historyMaxSize) {
            taskList.remove(0);
        }
        taskList.add(task);
    }

    @Override
    public List<Task> getHistory(){
        return taskList;
    }
}
