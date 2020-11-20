package material.tree.binarytree;

import javafx.geometry.Pos;
import material.Position;
import material.tree.iterators.BFSIterator;

import java.util.*;

public class ArrayBinaryTree<E> implements BinaryTree<E> {

    protected class BTNode<E> implements Position<E> {

        private E element;
        private int left, right, pos, parent;
        private ArrayBinaryTree<E> myTree;

        /**
         * Main constructor.
         *
         * @param element element stored in this node
         * @param parent  parent of this node
         * @param left    left child of this node
         * @param right   right child of this node
         */
        public BTNode(E element, int parent, int pos, int left, int right, ArrayBinaryTree<E> myTree) {
            setElement(element);
            setParent(parent);
            setPos(pos);
            setLeft(left);
            setRight(right);
            setMyTree(myTree);
        }

        /**
         * Returns the element stored at this node.
         *
         * @return the element stored at this node
         */
        @Override
        public E getElement() {
            return element;
        }

        /**
         * Sets the element stored at this node.
         *
         * @param o the element to be stored
         */
        public final void setElement(E o) {
            element = o;
        }

        /**
         * Returns the left child of this node.
         *
         * @return left child of this node
         */
        public final int getLeft() {
            return left;
        }

        /**
         * Sets the left child of this node.
         *
         * @param v the new left child of this node
         */
        public final void setLeft(int v) {
            left = v;
        }

        /**
         * Returns the right child of this node.
         *
         * @return the right child of this node
         */
        public final int getRight() {
            return right;
        }

        /**
         * Sets the right child of this node.
         *
         * @param v the new right child of this node
         */
        public final void setRight(int v) {
            right = v;
        }

        /**
         * Returns the parent of this position.
         *
         * @return the parent of this position
         */
        public final int getPos() {
            return pos;
        }

        /**
         * Sets the parent of this position
         *
         * @param v the new parent of this position
         */
        public final void setPos(int v) {
            pos = v;
        }

        /**
         * Returns the parent of this position.
         *
         * @return the parent of this position
         */
        public final int getParent() {
            return parent;
        }

        /**
         * Sets the parent of this position
         *
         * @param v the new parent of this position
         */
        public final void setParent(int v) {
            parent = v;
        }

        /**
         * Returns the parent of this position.
         *
         * @return the parent of this position
         */
        public final ArrayBinaryTree<E> getMyTree() {
            return myTree;
        }

        /**
         * Sets the parent of this position
         *
         * @param v the new parent of this position
         */
        public final void setMyTree(ArrayBinaryTree<E> v) {
            myTree = v;
        }
    }

    private int maxSize;
    private int size;
    private BTNode<E>[] tree;
    // starts at 1, not at 0

    public ArrayBinaryTree(int maxSize) {
        this.tree = new BTNode[maxSize];
        this.maxSize = maxSize;
    }

    private BTNode<E> checkPosition (Position<E> p) throws RuntimeException{
        if (p == null || ! (p instanceof BTNode)) {
            throw new RuntimeException("Invalid position");
        }
        BTNode<E> node = (BTNode<E>) p;
        if (node.getMyTree() != this) {
            throw new RuntimeException("The node is not from this tree");
        }
        return node;
    }

    private void resize () {
        BTNode<E>[] newArray = new BTNode[maxSize * 2];
        System.arraycopy(tree, 0, newArray, 0,  maxSize);
        maxSize *= 2;
        this.tree = newArray;
    }

    @Override
    public Position<E> left(Position<E> p) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (!this.hasLeft(node)) {
            throw new RuntimeException("The node has not left child");
        }
        return tree[node.getLeft()];
    }

    @Override
    public Position<E> right(Position<E> p) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (!this.hasRight(node)) {
            throw new RuntimeException("The node has not right child");
        }
        return tree[node.getRight()];
    }

    @Override
    public boolean hasLeft(Position<E> p) {
        BTNode<E> node = this.checkPosition(p);
        return node.getLeft() != -1;
    }

    @Override
    public boolean hasRight(Position<E> p) {
        BTNode<E> node = this.checkPosition(p);
        return node.getRight() != -1;
    }

    @Override
    public E replace(Position<E> p, E e) {
        BTNode<E> node = this.checkPosition(p);
        E oldElem = node.getElement();
        node.setElement(e);
        return oldElem;
    }

    @Override
    public Position<E> sibling(Position<E> p) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (node.equals(this.root())) {
            throw new RuntimeException("The node has not sibling");
        }
        BTNode<E> parent = tree[node.getParent()];
        return (parent.getLeft() == node.getPos())? tree[parent.getRight()] : tree[parent.getLeft()];
    }

    @Override
    public Position<E> insertLeft(Position<E> p, E e) throws RuntimeException {
        BTNode<E> parent = this.checkPosition(p);
        if (this.hasLeft(parent)) {
            throw new RuntimeException("The node already has a left child");
        }
        int newPos = parent.getPos() * 2;
        if (newPos > maxSize - 1) {
            this.resize();
        }
        tree[newPos] = new BTNode<>(e, parent.getPos(), newPos, -1, -1, this);
        parent.setLeft(newPos);
        this.size++;
        return tree[newPos];
    }

    @Override
    public Position<E> insertRight(Position<E> p, E e) throws RuntimeException {
        BTNode<E> parent = this.checkPosition(p);
        if (this.hasRight(parent)) {
            throw new RuntimeException("This node already has a right child");
        }
        int newPos = (parent.getPos() * 2) + 1;
        if (newPos > maxSize - 1) {
            this.resize();
        }
        tree[newPos] = new BTNode<>(e, parent.getPos(), newPos, -1, -1, this);
        parent.setRight(newPos);
        this.size++;
        return tree[newPos];
    }

    @Override
    public E remove(Position<E> p) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (!node.equals(this.root())) {
            BTNode<E> parent = tree[node.getParent()];
            if (parent.getLeft() == node.getPos()) {
                parent.setLeft(-1);
            }
            else {
                parent.setRight(-1);
            }
        }
        Iterator<Position<E>> ite = new BFSIterator<>(this, node);
        while (ite.hasNext()) {
            BTNode<E> next = this.checkPosition(ite.next());
            next.setMyTree(null);
            this.size--;
        }
        return node.getElement();
    }

    @Override
    public void swap(Position<E> p1, Position<E> p2) {
        BTNode<E> node1 = this.checkPosition(p1);
        BTNode<E> node2 = this.checkPosition(p2);
        E temp = node1.getElement();
        node1.setElement(node2.getElement());
        node2.setElement(temp);
    }

    private void copyTree (Position<E> originP, BinaryTree<E> originTree,
                           Position<E> destP, BinaryTree<E> destTree) {

        Queue<Position<E>> origins = new LinkedList<>();
        Queue<Position<E>> destinations = new LinkedList<>();

        origins.add(originP);
        destinations.add(destP);

        while (!origins.isEmpty()) {
            Position<E> originCurrent = origins.poll();
            Position<E> destCurrent = destinations.poll();
            this.size++;

            if (originTree.hasLeft(originCurrent)) {
                destinations.add(destTree.insertLeft(destCurrent, this.left(originCurrent).getElement()));
                origins.add(this.left(originCurrent));
            }
            if (originTree.hasRight(originCurrent)) {
                destinations.add(destTree.insertRight(destCurrent, this.right(originCurrent).getElement()));
                origins.add(this.right(originCurrent));
            }
        }
    }

    @Override
    public BinaryTree<E> subTree(Position<E> v) {
        BTNode<E> node = this.checkPosition(v);
        ArrayBinaryTree<E> newTree = new ArrayBinaryTree<>(maxSize);
        this.copyTree(node, this, newTree.addRoot(node.getElement()), newTree);
        return newTree;
    }

    @Override
    public void attachLeft(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (this.hasLeft(node)) {
            throw new RuntimeException("The node already has left child");
        }
        this.copyTree(tree.root(), tree, this.insertLeft(node, tree.root().getElement()), this);
    }

    @Override
    public void attachRight(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (this.hasRight(node)) {
            throw new RuntimeException("The node already has right child");
        }
        this.copyTree(tree.root(), tree, this.insertRight(node, tree.root().getElement()), this);
    }

    @Override
    public boolean isComplete() {
        return (this.level() * 2) - 1 == this.size();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Position<E> root() throws RuntimeException {
        if (this.isEmpty()) {
            throw new RuntimeException("The tree is empty");
        }
        return tree[1];
    }

    @Override
    public Position<E> parent(Position<E> v) throws RuntimeException {
        BTNode<E> node = this.checkPosition(v);
        if (node.equals(this.root())) {
            throw new RuntimeException("The node has not parent");
        }
        return tree[node.getParent()];
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        BTNode<E> node = this.checkPosition(v);
        LinkedList<Position<E>> l = new LinkedList<>();
        if (this.hasLeft(node)) {
            l.add(tree[node.getLeft()]);
        }
        if (this.hasRight(node)) {
            l.add(tree[node.getRight()]);
        }
        return l;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        BTNode<E> node = this.checkPosition(v);
        return !isLeaf(node);
    }

    @Override
    public boolean isLeaf(Position<E> v) throws RuntimeException {
        BTNode<E> node = this.checkPosition(v);
        return ! (this.hasLeft(node) || this.hasRight(node));
    }

    @Override
    public boolean isRoot(Position<E> v) {
        BTNode<E> node = this.checkPosition(v);
        return node.equals(this.root());
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        if (!this.isEmpty()) {
            throw new RuntimeException("The tree already has a root");
        }
        tree[1] = new BTNode<>(e, -1, 1, -1, -1, this);
        this.size = 1;
        return tree[1];
    }

    @Override
    public int level() {
        int maxLevel = 0;
        if (!this.isEmpty()) {
            HashMap<Position<E>, Integer> levels = new HashMap<>();
            levels.put(this.root(), 1);
            Queue<Position<E>> queue = new LinkedList<>();
            queue.add(this.root());
            while (!queue.isEmpty()) {
                Position<E> current = queue.poll();
                int level = levels.get(current) + 1;
                for (Position<E> child : this.children(current)) {
                    levels.put(child,  level);
                    queue.add(child);
                }
                maxLevel = Math.max(maxLevel, level);
            }
        }
        return maxLevel;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new BFSIterator<>(this);
    }

}
