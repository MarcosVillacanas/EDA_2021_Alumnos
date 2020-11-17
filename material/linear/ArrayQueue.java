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
    public Object front() throws RuntimeException{
        if (!this.isEmpty()) {
            return this.array[this.ini];
        }
        throw new RuntimeException("Queue is empty");
    }

    @Override
    public void enqueue(Object element) {
        if (this.fin == -1) {
            E[] newArray = (E[]) new Object[this.maxSize * 2];
            System.arraycopy(this.array, this.ini, newArray, 0, this.maxSize - this.ini);
            System.arraycopy(this.array, 0, newArray, this.maxSize - ini, this.maxSize);
            this.array = newArray;
            this.ini = 0;
            this.fin = this.maxSize;
            this.maxSize *= 2;
        }

        this.array[this.fin] = (E) element;
        this.fin = (this.fin + 1) % (this.maxSize);

        if (this.fin == this.ini) {
            this.fin = -1;
        }

        this.size++;
    }


    @Override
    public Object dequeue() {
        if (!this.isEmpty()) {
            Object o = this.array[this.ini];

            if (this.fin == -1) {
                this.fin = this.ini;
            }

            this.ini = (this.ini + 1) % (this.maxSize - 1);
            this.size--;
            return o;
        }
        throw new RuntimeException("Queue is empty");
    }

}
