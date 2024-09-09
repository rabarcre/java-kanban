package main.manager;

import main.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final CusLinkedList history = new CusLinkedList();
    int id = 0;

    @Override
    public void add(Task task) {
        history.linkLast(task);
    }

    @Override
    public void remove(int id) {
        history.removeNode(history.getNode(id));
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    private class CusLinkedList {
        private final Map<Integer, Node> nodeMap = new HashMap<>();
        private Node head;
        private Node tail;

        private void linkLast(Task task) {
            Node newNode = new Node();
            newNode.setValue(task);
            int deletedTaskId = 0;
            boolean gotDeleted = false;

            if (!nodeMap.isEmpty()) {
                for (int i = 0; i < nodeMap.size(); i++) {
                    if (nodeMap.containsKey(i)) {
                        Task taskFrMap = nodeMap.get(i).getValue();
                        if (taskFrMap.getId().equals(task.getId()) && taskFrMap.getTaskType().equals(task.getTaskType())) {
                            deletedTaskId = i;
                            gotDeleted = true;
                            removeNode(nodeMap.get(i));
                        }
                    }
                }
            }

            newNode.setNext(null);
            if (head == null) {
                head = newNode;
                tail = newNode;
                newNode.setPrev(null);
            } else {
                newNode.setPrev(tail);
                tail.setNext(newNode);
                tail = newNode;
            }
            if (gotDeleted) {
                nodeMap.put(deletedTaskId, newNode);
            } else {
                nodeMap.put(id++, newNode);
            }
        }

        private void removeNode(Node node) {
            for (int i = 0; i < nodeMap.size(); i++) {
                if (nodeMap.get(i).equals(node)) {
                    nodeMap.remove(i);
                }
            }

            if (node.equals(head)) {
                head = null;
            }

            if (node.equals(tail)) {
                if (node.next != null) {
                    tail = node;
                } else {
                    tail = head;
                }
            }

            if (node.prev != null) {
                node.prev.next = node.next;
            }

            if (node.next != null) {
                node.next.prev = node.prev;
            }
        }

        private List<Task> getTasks() {
            List<Task> taskList = new ArrayList<>();
            Node node = head;
            while (node != null) {
                taskList.add(node.getValue());
                node = node.getNext();
            }
            return taskList;
        }

        private Node getNode(int id) {
            return nodeMap.get(id);
        }
    }

    private static class Node {
        Node prev;
        Node next;
        Task value;

        public void setValue(Task value) {
            this.value = value;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public Node getNext() {
            return next;
        }

        public Task getValue() {
            return value;
        }
    }
}
