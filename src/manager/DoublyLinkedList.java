package manager;

import taskclasses.Task;

import java.util.HashMap;

public class DoublyLinkedList {
    private HashMap<Integer, Node> historyOfTask = new HashMap<>();
    private Node head;
    private Node tail;

    public void linkLast(Task element) {
        historyOfTask.remove(element.getId());
        final Node newNode = new Node(tail, element, null);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        historyOfTask.put(element.getId(), newNode);
    }

    public void removeNode(int id) {
        Node node = historyOfTask.get(id);
        if (node == null) return;
        final Node next = node.next;
        final Node prev = node.prev;
        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        node.data = null;
        historyOfTask.remove(id);
    }

    public Node getHead() {
        return head;
    }

}

