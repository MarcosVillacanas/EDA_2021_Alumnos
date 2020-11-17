package material.tree.narytree;

import material.Position;
import material.tree.Tree;
import material.tree.iterators.BFSIterator;

import java.util.*;

/**
 * A linked class for a tree where nodes have an arbitrary number of children.
 *
 * @param <E> the type of the elements in the tree
 * @author Raul Cabido, Abraham Duarte, Jose Velez, J. Sanchez-Oro, J. D. Quintana
 */
public class LinkedTree<E> implements NAryTree<E> {

    /**
     * Inner class which represents a node of the tree
     *
     * @param <T> the type of the elements stored in a node
     */
    private class TreeNode<T> implements Position<T> {

        private T element; // The element stored in the position
        private TreeNode<T> parent; // The parent of the node
        private List<TreeNode<T>> children; // The children of the node
        private LinkedTree<T> myTree; // A reference to the tree where the node belongs

        /**
         * Constructor of the class
         *
         * @param t the tree where the node is stored
         * @param e the element to store in the node
         * @param p the parent of the node
         * @param c the list of children of the node
         */
        public TreeNode(T e, TreeNode<T> p, List<TreeNode<T>> c, LinkedTree<T> t) {
            this.element = e;
            this.parent = p;
            this.children = c;
            this.myTree = t;
        }

        @Override
        public T getElement() {
            return element;
        }

        /**
         * Sets the element stored at this position
         *
         * @param o the element to store in the node
         */
        public final void setElement(T o) {
            element = o;
        }

        /**
         * Accesses to the list of children of this node
         *
         * @return the list of children
         */
        public List<TreeNode<T>> getChildren() {
            return children;
        }

        /**
         * Sets the children of this node
         *
         * @param c the list of nodes to be used as children of this position
         */
        public final void setChildren(List<TreeNode<T>> c) {
            children = c;
        }

        /**
         * Accesses to the parent of this node
         *
         * @return the parent of this node
         */
        public TreeNode<T> getParent() {
            return parent;
        }

        /**
         * Sets the parent of this node
         *
         * @param v the node to be used as parent
         */
        public final void setParent(TreeNode<T> v) {
            parent = v;
        }

        /**
         * Consults the tree in which this node is stored
         *
         * @return a reference to the tree where the node belongs
         */
        public LinkedTree<T> getMyTree() {
            return myTree;
        }

        /**
         * Sets the tree where this node belongs
         *
         * @param myTree the tree where this node belongs
         */
        public void setMyTree(LinkedTree<T> myTree) {
            this.myTree = myTree;
        }
    }

    private TreeNode<E> root; // The root of the tree
    private int size; // The number of nodes in the tree

    private TreeNode<E> checkPosition(Position<E> p) {
        if (p == null || !(p instanceof TreeNode)) {
            throw new RuntimeException("Invalid position");
        }
        TreeNode<E> node = (TreeNode<E>) p;
        if (node.getMyTree() != this) {
            throw new RuntimeException("The node is not from this tree");
        }
        return node;
    }

    @Override
    public E replace(Position<E> p, E e) {
        TreeNode<E> node = this.checkPosition(p);
        E oldElement = node.getElement();
        node.setElement(e);
        return oldElement;
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        TreeNode<E> node1 = this.checkPosition(p1);
        TreeNode<E> node2 = this.checkPosition(p2);

        if (node1.equals(node2)) {
            throw new RuntimeException("Both positions are the same");
        }
        else {
            if (node1.equals(this.root())) {
                TreeNode<E> node2Parent = node2.getParent();

                node2Parent.getChildren().remove(node2);
                node2Parent.getChildren().add(node1);
                node2.setParent(null);
                this.root = node2;

                node1.setParent(node2Parent);
            }
            else if (node2.equals(this.root())) {
                TreeNode<E> node1Parent = node1.getParent();

                node1Parent.getChildren().remove(node1);
                node1Parent.getChildren().add(node2);
                node1.setParent(null);
                this.root = node1;

                node2.setParent(node1Parent);
            }
            else {
                TreeNode<E> node1Parent = node1.getParent();

                node1Parent.getChildren().remove(node1);
                node1Parent.getChildren().add(node2);
                node1.setParent(node2.getParent());

                node2.getParent().getChildren().remove(node2);
                node2.getParent().getChildren().add(node1);
                node2.setParent(node1Parent);
            }
        }
    }

    @Override
    public Position<E> add(E element, Position<E> p) {
        TreeNode<E> parent = this.checkPosition(p);
        TreeNode<E> child = new TreeNode<>(element, parent, new LinkedList<>(), this);
        parent.getChildren().add(child);
        this.size++;
        return child;
    }

    @Override
    public E remove(Position<E> p) {
        TreeNode<E> node = this.checkPosition(p);
        if (this.root().equals(node)) {
            this.root = null;
            this.size = 0;
        }
        else {
            node.getParent().getChildren().remove(node);
            Iterator<Position<E>> ite = new BFSIterator<>(this, node);
            while (ite.hasNext()) {
                Position<E> next = ite.next();
                TreeNode<E> descendant = this.checkPosition(next);
                descendant.setMyTree(null);
                this.size--;
            }
        }
        return node.getElement();
    }

    @Override
    public void moveSubtree(Position<E> pOrig, Position<E> pDest) throws RuntimeException {
        TreeNode<E> nodeOrig = this.checkPosition(pOrig);
        TreeNode<E> nodeDest = this.checkPosition(pDest);

        if (nodeOrig.equals(this.root()) || nodeOrig.equals(this.root())) {
            throw new RuntimeException("Root node can't be moved");
        }

        if (nodeOrig.equals(nodeDest)) {
            throw new RuntimeException("Both positions are the same");
        }

        if (this.isAncestor(nodeOrig, nodeDest)) {
            throw new RuntimeException("Target position can't be a sub tree of origin");
        }

        nodeDest.getChildren().add(nodeOrig);
        nodeOrig.getParent().getChildren().remove(nodeOrig);
        nodeOrig.setParent(nodeDest);
    }

    private boolean isAncestor(TreeNode<E> nodeOrig, TreeNode<E> nodeDest) {
        Iterator<Position<E>> ite = new BFSIterator<>(this, nodeOrig);
        while (ite.hasNext()) {
            Position<E> next = ite.next();
            if (nodeDest.equals(next)) {
                return true;
            }
        }
        return false;
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
        return this.root;
    }

    @Override
    public Position<E> parent(Position<E> v) throws RuntimeException {
        TreeNode<E> node = this.checkPosition(v);
        if (this.root().equals(node)) {
            throw new RuntimeException("The node has not parent");
        }
        return node.getParent();
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        TreeNode<E> node = this.checkPosition(v);
        return node.getChildren();
    }

    @Override
    public boolean isInternal(Position<E> v) {
        return !this.isLeaf(v);
    }

    @Override
    public boolean isLeaf(Position<E> v) throws RuntimeException {
        TreeNode<E> node = this.checkPosition(v);
        return node.getChildren().isEmpty();
    }

    @Override
    public boolean isRoot(Position<E> v) {
        TreeNode<E> node = this.checkPosition(v);
        return this.root().equals(node);
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        if (!this.isEmpty()) {
            throw new RuntimeException("Tree already has a root");
        }
        this.root = new TreeNode<>(e, null, new LinkedList<>(), this);
        this.size++;
        return this.root;
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
    public Iterator<Position<E>> iterator() {
        return new BFSIterator<>(this);
    }

}

