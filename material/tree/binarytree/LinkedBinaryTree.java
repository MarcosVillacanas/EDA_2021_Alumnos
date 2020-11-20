package material.tree.binarytree;

import javafx.geometry.Pos;
import material.Position;
import material.tree.iterators.BFSIterator;
import material.tree.iterators.InorderBinaryTreeIterator;

import java.util.*;

/**
 * @param <E>
 * @author A. Duarte, J. Vélez
 * @see BinaryTree
 */
public class LinkedBinaryTree<E> implements BinaryTree<E> {

    protected class BTNode<T> implements Position<T> {

        private T element;
        private BTNode<T> left, right, parent;
        private LinkedBinaryTree<E> myTree;
        //myTree

        /**
         * Main constructor.
         *
         * @param element element stored in this node
         * @param parent  parent of this node
         * @param left    left child of this node
         * @param right   right child of this node
         */
        public BTNode(T element, BTNode<T> parent, BTNode<T> left, BTNode<T> right, LinkedBinaryTree<E> tree) {
            setElement(element);
            setParent(parent);
            setLeft(left);
            setRight(right);
            setMyTree(tree);
        }

        /**
         * Returns the element stored at this node.
         *
         * @return the element stored at this node
         */
        @Override
        public T getElement() {
            return element;
        }

        /**
         * Sets the element stored at this node.
         *
         * @param o the element to be stored
         */
        public final void setElement(T o) {
            element = o;
        }

        /**
         * Returns the left child of this node.
         *
         * @return left child of this node
         */
        public final BTNode<T> getLeft() {
            return left;
        }

        /**
         * Sets the left child of this node.
         *
         * @param v the new left child of this node
         */
        public final void setLeft(BTNode<T> v) {
            left = v;
        }

        /**
         * Returns the right child of this node.
         *
         * @return the right child of this node
         */
        public final BTNode<T> getRight() {
            return right;
        }

        /**
         * Sets the right child of this node.
         *
         * @param v the new right child of this node
         */
        public final void setRight(BTNode<T> v) {
            right = v;
        }

        /**
         * Returns the parent of this position.
         *
         * @return the parent of this position
         */
        public final BTNode<T> getParent() {
            return parent;
        }

        /**
         * Sets the parent of this position
         *
         * @param v the new parent of this position
         */
        public final void setParent(BTNode<T> v) {
            parent = v;
        }

        public LinkedBinaryTree<E> getMyTree () { return this.myTree; }

        public void setMyTree (LinkedBinaryTree<E> tree) { this.myTree = tree; }
    }

    private BTNode<E> root;
    private int size;

    /**
     * Creates an empty binary tree.
     */
    public LinkedBinaryTree() {
        root = null;
    }

    private BTNode<E> checkPosition (Position<E> p) throws RuntimeException {
        if (p == null || ! (p instanceof BTNode)) {
            throw new RuntimeException("Invalid position");
        }
        BTNode<E> node = (BTNode) p;
        if (node.getMyTree() != this) {
            throw new RuntimeException("The node is not from this tree");
        }
        return node;
    }

    @Override
    public Position<E> left(Position<E> p) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (this.hasLeft(node)) {
            return node.getLeft();
        }
        else {
            throw new RuntimeException("This node has not left child");
        }
    }

    @Override
    public Position<E> right(Position<E> p) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (this.hasRight(node)) {
            return node.getRight();
        }
        else {
            throw new RuntimeException("This node has not right child");
        }
    }

    @Override
    public boolean hasLeft(Position<E> p) {
        BTNode<E> node = this.checkPosition(p);
        return node.getLeft() != null;
    }

    @Override
    public boolean hasRight(Position<E> p) {
        BTNode<E> node = this.checkPosition(p);
        return node.getRight() != null;
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
            throw new RuntimeException("The root does not have a sibling");
        }
        else {
            BTNode<E> parent = node.getParent();
            BTNode<E> sibling = (parent.getLeft() == node)? parent.getRight() : parent.getLeft();
            if (sibling != null) {
                return sibling;
            }
            else {
                throw new RuntimeException("This node does not have a sibling");
            }
        }
    }

    @Override
    public Position<E> insertLeft(Position<E> p, E e) throws RuntimeException {
        BTNode<E> parent = this.checkPosition(p);
        if (this.hasLeft(parent)) {
            throw new RuntimeException("This node already has a left child");
        }
        parent.setLeft(new BTNode<>(e, parent, null, null, this));
        this.size++;
        return parent.getLeft();
    }

    @Override
    public Position<E> insertRight(Position<E> p, E e) throws RuntimeException {
        BTNode<E> parent = this.checkPosition(p);
        if (this.hasRight(parent)) {
            throw new RuntimeException("This node already has a right child");
        }
        parent.setRight(new BTNode<>(e, parent, null, null, this));
        this.size++;
        return parent.getRight();
    }

    @Override
    public E remove(Position<E> p) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (node.equals(this.root())) {
            this.root = null;
        }
        else {
            BTNode<E> parent = node.getParent();
            if (parent.getLeft() == node) {
                parent.setLeft(null);
            }
            else {
                parent.setRight(null);
            }
        }
        Iterator<Position<E>> ite = new BFSIterator<>(this, node);
        while (ite.hasNext()) {
            Position<E> aux = ite.next();
            BTNode<E> next = this.checkPosition(aux);
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

    private int copyTree (Position<E> originPoint, BinaryTree<E> originTree,
                           Position<E> newTreePoint, BinaryTree<E> newTree) {

        int copySize = 0;

        Queue<Position<E>> originPositions = new LinkedList<>();
        Queue<Position<E>> newTreePositions = new LinkedList<>();

        originPositions.add(originPoint);
        newTreePositions.add(newTreePoint);

        while (!originPositions.isEmpty()) {

            Position<E> currentOrigin = originPositions.poll();
            Position<E> currentNewTree = newTreePositions.poll();
            copySize++;

            if (originTree.hasLeft(currentOrigin)) {
                newTreePositions.add(newTree.insertLeft(currentNewTree, originTree.left(currentOrigin).getElement()));
                originPositions.add(originTree.left(currentOrigin));
            }
            if (originTree.hasRight(currentOrigin)) {
                newTreePositions.add(newTree.insertRight(currentNewTree, originTree.right(currentOrigin).getElement()));
                originPositions.add(originTree.right(currentOrigin));
            }
        }
        return copySize;
    }

    @Override
    public BinaryTree<E> subTree(Position<E> v) {
        BTNode<E> node = this.checkPosition(v);
        BinaryTree<E> newTree = new LinkedBinaryTree<>();

        this.copyTree(node, this, newTree.addRoot(node.getElement()), newTree);
        return newTree;
    }

    @Override
    public void attachLeft(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (this.hasLeft(node)) {
            throw new RuntimeException("The node already has a left child");
        }
        size += this.copyTree(tree.root(), tree, this.insertLeft(node, tree.root().getElement()), this);
    }

    @Override
    public void attachRight(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (this.hasRight(node)) {
            throw new RuntimeException("The node already has a right child");
        }
        size += this.copyTree(tree.root(), tree, this.insertRight(node, tree.root().getElement()), this);
    }

    @Override
    public boolean isComplete() {
        return (this.level() * 2) - 1 == this.size();
    }

    @Override
    public int level() {
        HashMap<Position<E>, Integer> levels = new HashMap<>();
        int maxLevel = 0;
        if (!this.isEmpty()) {
            Queue<Position<E>> queue = new LinkedList<>();
            queue.add(this.root());
            while (!queue.isEmpty()) {
                Position<E> p = queue.poll();
                int currentLevel = (this.isRoot(p))? 1 :  levels.get(this.parent(p)) + 1;
                maxLevel = Math.max(maxLevel, currentLevel);
                levels.put(p, currentLevel);
                for (Position<E> child : this.children(p)) {
                    queue.add(child);
                }
            }
        }
        return maxLevel;
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
    public Position<E> root() throws RuntimeException {
        if (this.isEmpty()) {
            throw new RuntimeException("The tree is empty");
        }
        return this.root;
    }

    @Override
    public Position<E> parent(Position<E> v) throws RuntimeException {
        BTNode<E> node = this.checkPosition(v);
        if (node.equals(this.root())) {
            throw new RuntimeException("The node has not parent");
        }
        return node.getParent();
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        BTNode<E> node = this.checkPosition(v);
        LinkedList<Position<E>> l = new LinkedList<>();
        if (this.hasLeft(node)) {
            l.add(node.getLeft());
        }
        if (this.hasRight(node)) {
            l.add(node.getRight());
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
        return node.getParent() == null;
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        if (!this.isEmpty()) {
            throw new RuntimeException("The tree already has a root");
        }
        this.root = new BTNode<>(e, null, null, null, this);
        this.size = 1;
        return this.root;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new BFSIterator<>(this);
    }
}
