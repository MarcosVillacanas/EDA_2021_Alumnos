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

        public Node<E> getPrevious() {
            return this.previous;
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
        if (!this.isEmpty()) {
            return this.head.element;
        }
        return null;
    }

    @Override
    public void enqueue(Object element) {
        Node<E> node =  new Node(element, null);
        if (!this.isEmpty()) {
            this.tail.setPrevious(node);
        }
        else {
            this.head = node;
        }
        this.tail = node;
        this.size++;
    }

    @Override
    public Object dequeue() throws RuntimeException{
        if (!this.isEmpty()) {
            Object o = this.head.element;
            this.head = this.head.getPrevious();
            this.size--;
            return o;
        }
        throw new RuntimeException("Queue is empty");
    }
}
