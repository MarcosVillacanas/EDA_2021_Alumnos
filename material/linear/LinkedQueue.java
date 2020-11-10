package material.linear;

import material.Position;

public class LinkedQueue<E> implements Queue{

    private class Node<E> implements Position<E> {

        private E element;
        private Node<E> previous;

        public Node(E e, Node<E> p){
            this.element = e;
            this.previous = p;
        }

        @Override
        public E getElement() {
            return element;
        }

        public void setPrevious(Node<E> previous) {
            this.previous = previous;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public LinkedQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public Object front() {

        if (this.isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }

        return this.head.getElement();
    }

    @Override
    public void enqueue(Object element) {

        Node<E> elem = new Node<>((E)element, null);

        if (this.isEmpty()) {
            this.head = elem;
        }
        else {
            this.tail.setPrevious(elem);
        }
        this.tail = elem;
        this.size++;
    }

    @Override
    public Object dequeue() {

        if (this.isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }

        E element = this.head.getElement();
        this.head = this.head.previous;
        this.size--;

        if (this.size() == 0) { // solo había un elemento
            this.tail = null;
        }

        return element;
    }
}
