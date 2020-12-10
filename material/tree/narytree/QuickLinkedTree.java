package material.tree.narytree;

import material.Position;
import material.tree.iterators.BFSIterator;

import java.util.*;

public class QuickLinkedTree<E> implements NAryTree<E> {

    private class TreeNode<T> implements Position<T> {

        T element;
        TreeNode<T> parent;
        List<TreeNode<T>> children;
        QuickLinkedTree<T> myTree;

        public TreeNode(T element, TreeNode<T> parent, List<TreeNode<T>> children, QuickLinkedTree<T> myTree) {
            this.element = element;
            this.parent = parent;
            this.children = children;
            this.myTree = myTree;
        }

        public void setElement(T element) {
            this.element = element;
        }

        public TreeNode<T> getParent() {
            return parent;
        }

        public void setParent(TreeNode<T> parent) {
            this.parent = parent;
        }

        public List<TreeNode<T>> getChildren() {
            return children;
        }

        public void setChildren(List<TreeNode<T>> children) {
            this.children = children;
        }

        public QuickLinkedTree<T> getMyTree() {
            return myTree;
        }

        public void setMyTree(QuickLinkedTree<T> myTree) {
            this.myTree = myTree;
        }

        @Override
        public T getElement() {
            return null;
        }
    }

    TreeNode<E> root;
    HashMap<E, TreeNode<E>> map;
    int size;

    public QuickLinkedTree() {
        this.map = new HashMap<>();
        this.size = 0;
    }

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
        E oldElem = node.getElement();
        node.setElement(e);
        return oldElem;
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        TreeNode<E> node1 = this.checkPosition(p1);
        TreeNode<E> node2 = this.checkPosition(p2);
        E temp = node1.getElement();
        node1.setElement(node2.getElement());
        node2.setElement(temp);
    }

    @Override
    public Position<E> add(E element, Position<E> p) throws RuntimeException{
        TreeNode<E> parent = this.checkPosition(p);
        if (!map.containsKey(element)) {
            TreeNode<E> node = new TreeNode<>(element, parent, new LinkedList<>(), this);
            parent.getChildren().add(node);
            map.put(element, node);
            this.size++;
        }
        throw new RuntimeException("The tree already contains that key");
    }

    @Override
    public E remove(Position<E> p) {
        TreeNode<E> node = this.checkPosition(p);
        if (!node.equals(this.root())) {
            node.getParent().getChildren().remove(node);
        }
        Iterator<Position<E>> ite = new BFSIterator<>(this, node);
        while (ite.hasNext()) {
            TreeNode<E> next = this.checkPosition(ite.next());
            map.remove(next.getElement());
            next.setMyTree(null);
            this.size--;
        }
        return node.getElement();
    }

    public Position<E> search (E elem) throws RuntimeException {
        if (map.containsKey(elem)) {
            return map.get(elem);
        }
        throw new RuntimeException("The node has not been found");
    }

    @Override
    public void moveSubtree(Position<E> pOrig, Position<E> pDest) throws RuntimeException {
        TreeNode<E> nodeOrig = this.checkPosition(pOrig);
        TreeNode<E> nodeDest = this.checkPosition(pDest);

        if (nodeOrig.equals(this.root())) {
            throw new RuntimeException("The root can't be moved");
        }
        else if (nodeOrig.equals(nodeDest)) {
            throw new RuntimeException("The nodes are the same");
        }
        else if (this.isAncestor(nodeOrig, nodeDest)) {
            throw new RuntimeException("The ancestor of a node can't become it's descendant");
        }
        nodeOrig.getParent().getChildren().remove(nodeOrig);
        nodeDest.getChildren().add(nodeOrig);
        nodeOrig.setParent(nodeDest);
    }

    private boolean isAncestor(Position<E> nodeOrig, Position<E> nodeDest) {
        while (!(nodeDest.equals(nodeOrig) || nodeDest.equals(this.root()))) {
            nodeDest = this.checkPosition(nodeDest).getParent();
        }
        return nodeDest.equals(nodeOrig);
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
        if (node.equals(this.root())) {
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
    public boolean isInternal(Position<E> v) { return !this.isLeaf(v); }

    @Override
    public boolean isLeaf(Position<E> v) throws RuntimeException {
        TreeNode<E> node = this.checkPosition(v);
        return (node.getChildren().size() == 0);
    }

    @Override
    public boolean isRoot(Position<E> v) {
        TreeNode<E> node = this.checkPosition(v);
        return node.equals(this.root());
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        if (!this.isEmpty()) {
            throw new RuntimeException("The tree already has a root");
        }
        this.root = new TreeNode<>(e, null, new LinkedList<>(), this);
        return this.root();
    }

    @Override
    public int level() {
        int maxLevel = 0;
        if (!this.isEmpty()) {
            HashMap<Position<E>, Integer> levels = new HashMap<>();
            Queue<Position<E>> queue = new LinkedList<>();

            levels.put(this.root(), 1);
            queue.add(this.root());

            while (!queue.isEmpty()) {
                Position<E> current = queue.poll();
                maxLevel = Math.max(levels.get(current), maxLevel);
                for (Position<E> child : this.children(current)) {
                    levels.put(child, levels.get(current) + 1);
                    queue.add(child);
                }
            }
        }
        return maxLevel;
    }

    @Override
    public Iterator<Position<E>> iterator() { return new BFSIterator<>(this); }
}
