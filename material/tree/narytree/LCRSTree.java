package material.tree.narytree;

import material.Position;
import material.tree.iterators.BFSIterator;

import java.util.*;

/**
 * A linked class for a tree where nodes have an arbitrary number of children.
 *
 * @author Raul Cabido, Abraham Duarte, Jose Velez, Jesús Sánchez-Oro
 * @param <E> the elements stored in the tree
 */
public class LCRSTree<E> implements NAryTree<E> {

    private class TreeNode<E> implements Position<E> {

        private E element;
        private TreeNode<E> leftChild;
        private TreeNode<E> rightSibling;
        private TreeNode<E> parent;
        private LCRSTree<E> tree;

        public TreeNode(E e, TreeNode<E> lc, TreeNode<E> rs, TreeNode<E> p, LCRSTree<E> t) {
            this.element = e;
            this.leftChild = lc;
            this.rightSibling = rs;
            this.parent = p;
            this.tree = t;
        }

        @Override
        public E getElement() { return this.element; }

        public TreeNode<E> getLeftChild() { return this.leftChild; }

        public List<TreeNode<E>> getChildren() {
            List<TreeNode<E>> children = new ArrayList<>();
            TreeNode<E> current = this.getLeftChild();
            if (current == null) {
                return children;
            }
            children.add(current);
            while (current.getRightSibling() != null) {
                current = current.getRightSibling();
                children.add(current);
            }
            return children;
        }

        public TreeNode<E> getRightSibling() {
            return rightSibling;
        }

        public TreeNode<E> getParent() { return this.parent; }

        public LCRSTree<E> getTree() { return this.tree; }

        public void setTree(LCRSTree<E> tree) {
            this.tree = tree;
        }

        public void setParent(TreeNode<E> parent) {
            this.parent = parent;
        }

        public void setLeftChild(TreeNode<E> leftChild) {
            this.leftChild = leftChild;
        }

        public void setRightSibling(TreeNode<E> rightSibling) {
            this.rightSibling = rightSibling;
        }

        public void setElement(E element) {
            this.element = element;
        }
    }

    private TreeNode<E> root;
    private int size;

    public LCRSTree() {
        this.root = null; // Not needed in Java, but we are evaluated by our Data Structures Abilities
        this.size = 0; // Noy by our Java knowledge
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
        if (this.isEmpty())
            throw new RuntimeException("The tree is empty");
        return this.root;
    }

    @Override
    public Position<E> parent(Position<E> V) throws RuntimeException {
        if (this.isEmpty()) {
            throw new RuntimeException("The tree is empty");
        }
        TreeNode<E> node = this.checkPosition(V);
        if (node.getParent() == null) {
            throw new RuntimeException("The node has not parent");
        }
        return node.getParent();
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> V) throws RuntimeException {
        TreeNode<E> node = this.checkPosition(V);
        return node.getChildren();
    }

    @Override
    public boolean isInternal(Position<E> V) throws RuntimeException { return !(isLeaf(V)); }

    @Override
    public boolean isLeaf(Position<E> V) throws RuntimeException {
        TreeNode<E> node = this.checkPosition(V);
        return node.getLeftChild() == null;
    }

    @Override
    public boolean isRoot(Position<E> V) throws RuntimeException {
        TreeNode<E> node = this.checkPosition(V);
        return node.getParent() == null;
    }

    @Override
    public Position<E> addRoot(E element) throws RuntimeException {
        if (this.root != null) {
            throw new RuntimeException("Tree already has a root");
        }
        TreeNode<E> node = new TreeNode<E>(element, null, null, null, this);
        this.root = node;
        this.size = 1;
        return this.root;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new BFSIterator<>(this);
    }

    @Override
    public E replace(Position<E> p, E e) {
        TreeNode<E> node = this.checkPosition(p);
        node.setElement(e);
        return e;
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        TreeNode<E> node1 = this.checkPosition(p1);
        TreeNode<E> node2 = this.checkPosition(p2);
        E temp = node1.getElement();
        node1.setElement(node2.getElement());
        node2.setElement(temp);
    }

    public Position<E> add(E e, Position<E> p) throws RuntimeException {
        TreeNode<E> parent = this.checkPosition(p);
        TreeNode<E> child = new TreeNode<E>(e, null, null, parent, this);

        TreeNode<E> leftChildDest = parent.getLeftChild();
        if (leftChildDest == null) {
            parent.setLeftChild(child);
        }
        else {
            TreeNode<E> lastChildDest = leftChildDest;
            while (lastChildDest.getRightSibling() != null) {
                lastChildDest = lastChildDest.getRightSibling();
            }
            lastChildDest.setRightSibling(child);
        }
        child.setRightSibling(null);

        this.size++;
        return child;
    }

    // An implementation exercise for the exam could be this method returning the subtree instead of void
    // O(n) because of size recalculation, a subtree can be disconnected with O(1)
    public E remove(Position<E> p) {
        TreeNode<E> node = checkPosition(p);
        if (this.isRoot(node)) {
            this.size = 0;
            this.root = null;
        }
        else {
            TreeNode<E> leftChild = node.getParent().getLeftChild();
            if (leftChild.equals(node)) {
                node.getParent().setLeftChild(node.getRightSibling());
            }
            else {
                leftChild.setRightSibling(node.getRightSibling());
            }
            Iterator<Position<E>> ite = new BFSIterator<>(this, node);
            while (ite.hasNext()) {
                TreeNode<E> descendant = this.checkPosition(ite.next());
                descendant.setTree(null);
                this.size--;
            }
        }
        return node.getElement();
    }

    @Override
    public void moveSubtree (Position<E> pOrig, Position<E> pDest) throws RuntimeException{
        TreeNode<E> nOrig = this.checkPosition(pOrig);
        TreeNode<E> nDest = this.checkPosition(pDest);
        if (this.isRoot(pOrig)) {
            throw new RuntimeException("Root node can't be moved");
        }
        else if (nOrig == nDest) {
            throw new RuntimeException("Both positions are the same");
        }
        else if (isAncestor(nOrig, nDest)) {
            throw new RuntimeException("Target position can't be a sub tree of origin");
        }

        TreeNode<E> leftChildOrig = nOrig.getParent().getLeftChild();
        if (leftChildOrig.equals(nOrig)) {
            nOrig.getParent().setLeftChild(nOrig.getRightSibling());
        }
        else {
            leftChildOrig.setRightSibling(nOrig.getRightSibling());
        }

        nOrig.setParent(nDest);

        TreeNode<E> leftChildDest = nDest.getLeftChild();
        if (leftChildDest == null) {
            nDest.setLeftChild(nOrig);
        }
        else {
            TreeNode<E> lastChildDest = leftChildDest;
            while (lastChildDest.getRightSibling() != null) {
                lastChildDest = lastChildDest.getRightSibling();
            }
            lastChildDest.setRightSibling(nOrig);
        }
        nOrig.setRightSibling(null);
    };

    private boolean isAncestor (Position<E> pOrig, Position<E> pDest) {
        Queue<Position<E>> q = new ArrayDeque<>();
        q.add(pOrig);
        while (!q.isEmpty()) {
            Position<E> current = q.poll();
            if (current.equals(pDest)) {
                return true;
            }
            q.addAll(this.checkPosition(current).getChildren());
        }
        return false;
    }

    private TreeNode<E> checkPosition (Position<E> p) {
        if (!(p instanceof TreeNode)) { // Also checks is p == null
            throw new RuntimeException("Invalid Position");
        }
        TreeNode<E> node = (TreeNode<E>) p;
        if (node.getTree() != this) {
            throw new RuntimeException("The node is not from this tree");
        }
        return node;
    }
}
