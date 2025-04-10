package manager;



public class DoublyLinkedList<T> {
    public Node<T> head;
    public Node<T> tail;
    private int size = 0;

    public void linkLast(T element) {
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }

    public void removeNode(Node<T> node) {
        if (node == null) return;
        final Node<T> next = node.next;
        final Node<T> prev = node.prev;
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
        size--;
    }

    public int getSize() {
        return size;
    }

    public Node<T> getHead() {
        return head;
    }
}

