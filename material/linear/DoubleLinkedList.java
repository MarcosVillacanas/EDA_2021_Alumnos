package material.linear;

import material.Position;


/**
 * Realization of a PositionList using a doubly-linked list of nodes.
 *
 * @author A. Duarte, J. Vélez, J. Sánchez-Oro
 * @param <E> the generic type of the list
 */
public class DoubleLinkedList<E> implements List<E> {

    private class DNode<T> implements Position<T> {

        private DNode<T> prev, next; // References to the nodes before and after
        private T element; // Element stored in this position
        private final DoubleLinkedList<T> myList; //Reference to the object that contains each node

        /**
         * Constructor
         */
        public DNode(DNode<T> prev, DNode<T> next, T elem, DoubleLinkedList<T> myList) {
            this.prev = prev;
            this.next = next;
            this.element = elem;
            this.myList = myList;
        }

        @Override
        public T getElement() {
            return element;
        }

        /**
         * Accesses to the next element
         *
         * @return the next element in the list
         */
        public DNode<T> getNext() {
            return next;
        }

        /**
         * Accesses to the previous element
         *
         * @return the previous element in the list
         */
        public DNode<T> getPrev() {
            return prev;
        }

        /**
         * Modifies the next element
         *
         * @param newNext the new next element
         */
        public void setNext(DNode<T> newNext) {
            next = newNext;
        }

        /**
         * Modifies the previous element
         *
         * @param newPrev the new previous element
         */
        public void setPrev(DNode<T> newPrev) {
            prev = newPrev;
        }

        /**
         * Modifies the current element
         *
         * @param newElement The new current element
         */
        public void setElement(T newElement) {
            element = newElement;
        }

        /**
         * Accesses to the list where the DNode belongs to
         *
         * @return the list of the DNode
         */
        DoubleLinkedList<T> getMyList() {
            return this.myList;
        }
    }

    protected int size; // Number of elements in the list
    protected DNode<E> header, trailer; // Special sentinels

    /**
     * Constructor that creates an empty list; O(1) time
     */
    public DoubleLinkedList() {
        this.size = 0;
        this.header = null;
        this.trailer = null;
    }

    /**
     * Checks if position is valid for this list and converts it to DNode if it
     * is valid; O(1) time
     *
     * @param p the position to check
     * @return the position converted to DNode
     * @throws RuntimeException
     */
    private DNode<E> checkPosition(Position<E> p) throws RuntimeException {
        if (p==null || !(p instanceof DNode)) {
            throw new RuntimeException("Invalid position");
        }
        DNode<E> node = (DNode<E>) (p);
        if (node.myList != this) {
            throw new RuntimeException("Position does not belong to this list");
        }
        return node;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public Position<E> first() throws RuntimeException {
        if (this.isEmpty()) {
            throw new RuntimeException("Empty list does not have first element");
        }
        return this.header;
    }

    @Override
    public Position<E> last() throws RuntimeException {
        if (this.isEmpty()) {
            throw new RuntimeException("Empty list does not have last element");
        }
        return this.trailer;
    }

    @Override
    public Position<E> prev(Position<E> p) throws RuntimeException {
        DNode<E> node = this.checkPosition(p);
        if (node.getPrev() == null) {
            throw new RuntimeException("Previous node not found");
        }
        return node.getPrev();
    }

    @Override
    public Position<E> next(Position<E> p) throws RuntimeException {
        DNode<E> node = this.checkPosition(p);
        if (node.getNext() == null) {
            throw new RuntimeException("Next node not found");
        }
        return node.getNext();
    }

    
    @Override
    public Position<E> addBefore(Position<E> p, E element) throws RuntimeException {
        DNode<E> node = this.checkPosition(p);
        DNode<E> newNode;
        if (node == this.first()) {
            newNode = new DNode<>(null, this.header, element, this);
            this.header = newNode;
        }
        else {
            newNode = new DNode<>(node.getPrev(), node, element, this);
            node.getPrev().setNext(newNode);
        }
        node.setPrev(newNode);
        this.size++;
        return newNode;
    }

    
    @Override
    public Position<E> addAfter(Position<E> p, E element) throws RuntimeException {
        DNode<E> node = this.checkPosition(p);
        DNode<E> newNode;
        if (node == this.last()) {
            newNode = new DNode<>(this.trailer, null, element, this);
            this.trailer = newNode;
        }
        else {
            newNode = new DNode<>(node, node.getNext(), element, this);
            node.getNext().setPrev(newNode);
        }
        node.setNext(newNode);
        this.size++;
        return newNode;
    }

    @Override
    public Position<E> addFirst(E element) {
        DNode<E> newNode = new DNode<>(null, this.header, element, this);
        if (!this.isEmpty()) {
            this.header.setPrev(newNode);
        }
        else {
            this.trailer = newNode;
        }
        this.size++;
        this.header = newNode;
        return newNode;
    }

    @Override
    public Position<E> addLast(E element) {
        DNode<E> newNode = new DNode<>(this.trailer, null, element, this);
        if (!this.isEmpty()) {
            this.trailer.setNext(newNode);
        }
        else {
            this.header = newNode;
        }
        this.size++;
        this.trailer = newNode;
        return newNode;
    }
    
    @Override
    public E remove(Position<E> p) throws RuntimeException {
        DNode<E> node = this.checkPosition(p);
        if (node == this.header) {
            this.header = node.getNext();
            if (this.header != null) {
                this.header.setPrev(null);
            }
        }
        else if (node == this.trailer) {
            this.trailer = node.getPrev();
            if (this.trailer != null) {
                this.trailer.setNext(null);
            }
        }
        else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
        }
        this.size--;
        return node.getElement();
    }

    
    @Override
    public E set(Position<E> p, E element) throws RuntimeException {
        DNode<E> node = this.checkPosition(p);
        E oldElement = node.getElement();
        node.setElement(element);
        return oldElement;
    }

    // Convenience methods
    /**
     * Checks if a position is the first one in the list; O(1) time
     *
     * @param p the position to check
     * @return TRUE if p is the first position, FALSE otherwise
     */
    public boolean isFirst(Position<E> p) throws RuntimeException {
        DNode<E> node = this.checkPosition(p);
        return node == this.first();
    }

    /**
     * Checks if a position is the last one in the list; O(1) time
     *
     * @param p the position to check
     * @return TRUE if p is the last position, FALSE otherwise
     */
    public boolean isLast(Position<E> p) throws RuntimeException {
        DNode<E> node = this.checkPosition(p);
        return node == this.last();
    }

    /**
     * Swaps the elements of two give positions; O(1) time
     *
     * @param a the first position to swap
     * @param b the second position to swap
     */
    public void swapElements(Position<E> a, Position<E> b) throws RuntimeException {
        DNode<E> nodeA = this.checkPosition(a);
        DNode<E> nodeB = this.checkPosition(b);

        if (nodeA.getPrev() != null) {
            nodeA.getPrev().setNext(nodeB);
        }
        else {
            this.header = nodeB;
        }
        if (nodeA.getNext() != null) {
            nodeA.getNext().setPrev(nodeB);
        }
        else {
            this.trailer = nodeB;
        }

        if (nodeB.getPrev() != null) {
            nodeB.getPrev().setNext(nodeA);
        }
        else {
            this.header = nodeA;
        }
        if (nodeB.getNext() != null) {
            nodeB.getNext().setPrev(nodeA);
        }
        else {
            this.trailer = nodeA;
        }
    }
}
