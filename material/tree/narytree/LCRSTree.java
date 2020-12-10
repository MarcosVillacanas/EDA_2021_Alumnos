package material.tree.narytree;

import material.Position;
import material.tree.Tree;
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

    private TreeNode<E> checkPosition(Position<E> p) throws RuntimeException {
        if (p == null || !(p instanceof TreeNode)) {
            throw new RuntimeException("Invalid position");
        }
        TreeNode<E> node = (TreeNode<E>) p;
        if (node.getTree() != this) {
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
    public void swapElements(Position<E> p1, Position<E> p2) throws RuntimeException{
        TreeNode<E> node1 = this.checkPosition(p1);
        TreeNode<E> node2 = this.checkPosition(p2);
        E temp = node1.getElement();
        node1.setElement(node2.getElement());
        node2.setElement(temp);
    }

    @Override
    public Position<E> add(E element, Position<E> p) {
        TreeNode<E> parent = this.checkPosition(p);
        TreeNode<E> child = new TreeNode<>(element, null, null, parent, this);

        TreeNode<E> descendant = parent.getLeftChild();
        if (descendant == null) {
            parent.setLeftChild(child);
        }
        else {
            while (descendant.getRightSibling() != null) {
                descendant = descendant.getRightSibling();
            }
            descendant.setRightSibling(child);
        }

        this.size++;
        return child;
    }

    @Override
    public E remove(Position<E> p) {
        TreeNode<E> node = this.checkPosition(p);
        if (node.equals(this.root())){
            this.root = null;
            this.size = 0;
        }
        else {
            TreeNode<E> parent = node.getParent();
            TreeNode<E> descendant = parent.getLeftChild();
            if (descendant.equals(node)) {
                parent.setLeftChild(node.getRightSibling());
            }
            else {
                while (!descendant.getRightSibling().equals(node)) {
                    descendant = descendant.getRightSibling();
                }
                descendant.setRightSibling(node.getRightSibling());
            }
        }
        Iterator<Position<E>> ite = new BFSIterator<>(this, node);
        while (ite.hasNext()) {
            TreeNode<E> next = this.checkPosition(ite.next());
            next.setTree(null);
            this.size--;
        }
        return node.getElement();
    }

    @Override
    public void moveSubtree(Position<E> pOrig, Position<E> pDest) throws RuntimeException {
        TreeNode<E> nodeOrig = this.checkPosition(pOrig);
        TreeNode<E> nodeDest = this.checkPosition(pDest);

        if (nodeOrig.equals(this.root()) || nodeDest.equals(this.root())) {
            throw new RuntimeException("Root node can't be moved");
        }
        else if (nodeOrig.equals(nodeDest)) {
            throw new RuntimeException("Both positions are the same");
        }
        else if (isAncestor(nodeOrig, nodeDest)) {
            throw new RuntimeException("Target position can't be a sub tree of origin");
        }

        // Delete origin surrounding links

        TreeNode<E> descendant = nodeOrig.getParent().getLeftChild();
        if (descendant.equals(nodeOrig)) {
            nodeOrig.getParent().setLeftChild(descendant.getRightSibling());
        }
        else {
            while (!descendant.getRightSibling().equals(nodeOrig)) {
                descendant = descendant.getRightSibling();
            }
            descendant.setRightSibling(nodeOrig.getRightSibling());
        }

        // Add origin links in destination

        if (nodeDest.getLeftChild() == null) {
            nodeDest.setLeftChild(nodeOrig);
        }
        else {
            descendant = nodeDest.getLeftChild();
            while (descendant.getRightSibling() != null) {
                descendant = descendant.getRightSibling();
            }
            descendant.setRightSibling(nodeOrig);
        }
        nodeOrig.setParent(nodeDest);
    }

    private boolean isAncestor(TreeNode<E> nodeOrig, TreeNode<E> nodeDest) {
        Iterator<Position<E>> ite = new BFSIterator<>(this, nodeOrig);
        while (ite.hasNext()) {
            if (ite.next().equals(nodeDest)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() { return this.size; }

    @Override
    public boolean isEmpty() { return this.size == 0; }

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
    public Iterable<? extends Position<E>> children (Position<E> v) {
        TreeNode<E> node = this.checkPosition(v);
        LinkedList<TreeNode<E>> l = new LinkedList<>();
        TreeNode<E> descendant = node.getLeftChild();
        while (descendant != null) {
            l.add(descendant);
            descendant = descendant.getRightSibling();
        }
        return l;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        return !this.isLeaf(v);
    }

    @Override
    public boolean isLeaf(Position<E> v) throws RuntimeException {
        TreeNode<E> node = this.checkPosition(v);
        return node.getLeftChild() == null;
    }

    @Override
    public boolean isRoot(Position<E> v) {
        TreeNode<E> node = this.checkPosition(v);
        return node.getParent() == null;
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        if (!this.isEmpty()) {
            throw new RuntimeException("The tree already has a root");
        }
        this.root = new TreeNode<>(e, null, null, null, this);
        this.size = 1;
        return this.root();
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
    public Iterator<Position<E>> iterator() { return new BFSIterator<>(this); }
}
