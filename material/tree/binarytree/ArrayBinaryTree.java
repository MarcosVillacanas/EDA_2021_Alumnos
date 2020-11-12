package material.tree.binarytree;

import material.Position;
import material.tree.iterators.BFSIterator;

import java.util.*;

public class ArrayBinaryTree<E> implements BinaryTree<E> {

    protected class BTNode<E> implements Position<E> {

        private E element;
        private int left, right, pos, parent;
        // myTree

        /**
         * Main constructor.
         *
         * @param element element stored in this node
         * @param parent  parent of this node
         * @param left    left child of this node
         * @param right   right child of this node
         */
        public BTNode(E element, int parent, int pos, int left, int right) {
            setElement(element);
            setParent(parent);
            setPos(pos);
            setLeft(left);
            setRight(right);
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
    }

    private BTNode<E>[] tree;
    // starts at 1, not at 0

    public ArrayBinaryTree() {
        this.tree = new BTNode[17];
    }

    @Override
    public Position<E> left(Position<E> p) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        int left = node.getLeft();
        if (left == -1) {
            throw new RuntimeException("No left child");
        }
        return this.tree[left];
    }

    @Override
    public Position<E> right(Position<E> p) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        int right = node.getRight();
        if (right == -1) {
            throw new RuntimeException("No right child");
        }
        return this.tree[right];
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
        E oldE = node.getElement();
        node.setElement(e);
        return oldE;
    }

    @Override
    public Position<E> sibling(Position<E> p) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        int nodePos = node.getPos();
        int sibling = (nodePos % 2 == 0)? nodePos + 1 : nodePos - 1;
        if (tree[sibling] != null) {
            return tree[sibling];
        }
        throw new RuntimeException("No sibling");
    }

    @Override
    public Position<E> insertLeft(Position<E> p, E e) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        int leftChild = node.getLeft();

        if (leftChild != -1) {
            throw new RuntimeException("Node already has a left child");
        }

        leftChild = 2 * node.getPos();
        tree[leftChild] = new BTNode<>(e, node.getPos(), leftChild, -1, -1);

        return tree[leftChild];
    }

    @Override
    public Position<E> insertRight(Position<E> p, E e) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        int rightChild = node.getRight();

        if (rightChild != -1) {
            throw new RuntimeException("Node already has a right child");
        }

        rightChild = (2 * node.getPos()) + 1;
        tree[rightChild] = new BTNode<>(e, node.getPos(), rightChild, -1, -1);

        return tree[rightChild];
    }

    @Override
    public E remove(Position<E> p) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (node == root()) {
            tree[1] = null;
        }
        else {
            tree[node.getPos()] = null;
            if (tree[node.getParent()].getLeft() == node.getPos()) {
                tree[node.getParent()].setLeft(-1);
            }
            else {
                tree[node.getParent()].setRight(-1);
            }
        }
        return node.getElement();
    }

    @Override
    public void swap(Position<E> p1, Position<E> p2) {
        BTNode<E> node1 = this.checkPosition(p1);
        BTNode<E> node2 = this.checkPosition(p2);


    }

    @Override
    public BinaryTree<E> subTree(Position<E> v) {
        BTNode<E> node = this.checkPosition(v);
        remove(node);

        BinaryTree<E> newTree = new ArrayBinaryTree<>();
        Position<E> attachedRoot = newTree.addRoot(node.getElement());

        moveTree(node, attachedRoot, this, newTree);
        return newTree;
    }

    private void moveTree(Position<E> originRoot, Position<E> attachedRoot, BinaryTree<E> origin, BinaryTree<E> dest) {
        Queue<Position<E>> originTree = new LinkedList<>();
        Queue<Position<E>> destinationTree = new LinkedList<>();

        originTree.add(originRoot);
        destinationTree.add(attachedRoot);

        while (!originTree.isEmpty()) {
            Position<E> originNode = originTree.poll();
            Position<E> attachedParent = destinationTree.poll();

            if (origin.hasLeft(originNode)) {
                Position<E> originLeft = origin.left(originNode);
                Position<E> attachedLeftChild = dest.insertLeft(attachedParent, originLeft.getElement());
                originTree.add(originLeft);
                destinationTree.add(attachedLeftChild);
            }

            if (origin.hasRight(originNode)) {
                Position<E> originRight = origin.right(originNode);
                Position<E> attachedRightChild = dest.insertRight(attachedParent, originRight.getElement());
                originTree.add(originRight);
                destinationTree.add(attachedRightChild);
            }
        }
    }

    @Override
    public void attachLeft(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (tree == this) {
            throw new RuntimeException("Cannot attach a tree over himself");
        }
        else if (this.hasLeft(node)) {
            throw new RuntimeException("Already has a left child");
        }

        if (tree != null && !tree.isEmpty()) {

            Position<E> originRoot = tree.root();
            Position<E> attachedRoot = this.insertLeft(node, originRoot.getElement());

            moveTree(originRoot, attachedRoot, tree, this);
        }
    }

    @Override
    public void attachRight(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        BTNode<E> node = this.checkPosition(p);
        if (tree == this) {
            throw new RuntimeException("Cannot attach a tree over himself");
        }
        else if (this.hasRight(node)) {
            throw new RuntimeException("Already has a right child");
        }

        if (tree != null && !tree.isEmpty()) {

            Position<E> originRoot = tree.root();
            Position<E> attachedRoot = this.insertRight(node, originRoot.getElement());

            moveTree(originRoot, attachedRoot, tree, this);
        }
    }

    @Override
    public boolean isComplete() {
        boolean completion = true;
        Queue<Position<E>> nodes = new LinkedList<>();
        if (!isEmpty()) {
            nodes.add(root());
            while (completion && !nodes.isEmpty()) {
                Position<E> node = nodes.poll();
                completion = isLeaf(node) || (this.hasLeft(node) && this.hasRight(node));
            }
        }
        return completion;
    }

    @Override
    public int size() {
        int size = 0;
        Iterator<Position<E>> ite = iterator();
        while (ite.hasNext()) {
            size++;
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return tree[1] == null;
    }

    @Override
    public Position<E> root() throws RuntimeException {
        if (tree[1] != null) {
            return tree[1];
        }
        throw new RuntimeException("No root");
    }

    @Override
    public Position<E> parent(Position<E> v) throws RuntimeException {
        BTNode<E> node = this.checkPosition(v);
        if (node.getPos() != 1) {
            return tree[node.getParent()];
        }
        throw new RuntimeException("Root has no parent");
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        BTNode<E> node = this.checkPosition(v);
        List<Position<E>> children = new ArrayList<>();
        if (this.hasLeft(node)) {
            children.add(tree[node.getLeft()]);
        }
        if (this.hasRight(node)) {
            children.add(tree[node.getRight()]);
        }
        return children;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        BTNode<E> node = this.checkPosition(v);
        return (this.hasLeft(node) || this.hasRight(node));
    }

    @Override
    public boolean isLeaf(Position<E> v) throws RuntimeException {
        return !(this.isInternal(v));
    }

    @Override
    public boolean isRoot(Position<E> v) {
        BTNode<E> node = this.checkPosition(v);
        return node.getPos() == 1;
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        if (tree[1] != null) {
            throw new RuntimeException("Tree already has a root");
        }
        tree[1] = new BTNode<>(e, -1, 1, -1, -1);
        return tree[1];
    }

    @Override
    public Iterator<Position<E>> iterator() { return new BFSIterator<>(this); }

    private BTNode<E> checkPosition(Position<E> p) {
        if (p == null || !(p instanceof ArrayBinaryTree.BTNode)) {
            throw new RuntimeException("The position is invalid");
        }
        return (BTNode<E>) p;
    }
}
