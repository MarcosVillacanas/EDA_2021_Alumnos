package material.linear;

public class ArrayQueue<E> implements Queue {
    private E [] array;
    private int ini; //primera posición ocupada
    private int fin; //primera posición libre
    private int size;
    private int maxSize;

    public ArrayQueue() {
        this.ini = 0;
        this.fin = 0;
        this.size = 0;
        this.maxSize = 16;
        this.array = (E[]) new Object[this.maxSize];
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

        return array[ini];
    }

    @Override
    public void enqueue(Object element) {

        if (this.size == this.maxSize) {
            E [] newArray = (E[]) new Object [this.maxSize * 2];
            System.arraycopy(this.array, this.ini, newArray, 0, this.maxSize - this.ini);
            System.arraycopy(this.array, 0, newArray, this.maxSize - this.ini, this.ini);
            this.array = newArray;
            this.ini = 0;
            this.fin = this.maxSize;
            this.maxSize *= 2;
        }

        this.array[this.fin] = (E) element;
        this.fin = (this.fin + 1) % this.maxSize;
        this.size++;
    }

    @Override
    public Object dequeue() {

        if (this.isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }

        E element = array[ini];
        ini = (ini + 1) % this.maxSize;
        this.size--;

        return element;
    }

}
