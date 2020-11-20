package material.tree.narytree;

import material.Position;
import material.tree.iterators.BFSIterator;

import java.util.*;

public class LinkedTreeFastLeavesAccess<E> implements NAryTree<E> {

    private class TreeNode<T> implements Position<T> {

        T element;
        TreeNode<T> parent;
        List<TreeNode<T>> children;
        LinkedTreeFastLeavesAccess<E> myTree;

        public TreeNode(T elem, TreeNode<T> parent, List<TreeNode<T>> children, LinkedTreeFastLeavesAccess<E> myTree) {
            this.element = elem;
            this.parent = parent;
            this.children = children;
            this.myTree = myTree;
        }

        @Override
        public T getElement() {
            return this.element;
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

        public LinkedTreeFastLeavesAccess<E> getMyTree() {
            return myTree;
        }

        public void setMyTree(LinkedTreeFastLeavesAccess<E> myTree) {
            this.myTree = myTree;
        }
    }

    private TreeNode<E> root;
    private int size;
    private HashSet<Position<E>> leaves;

    public LinkedTreeFastLeavesAccess() {
        this.size = 0;
        this.leaves = new HashSet<>();
    }

    private TreeNode<E> checkPosition (Position<E> p) throws RuntimeException {
        if (p == null || ! (p instanceof TreeNode)) {
            throw new RuntimeException("Invalid position");
        }
        TreeNode<E> node = (TreeNode<E>) p;
        if (node.getMyTree() != this) {
            throw new RuntimeException("This node is not from this tree");
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
    public Position<E> add(E element, Position<E> p) {
        TreeNode<E> parent = this.checkPosition(p);
        TreeNode<E> child = new TreeNode<>(element, parent, new LinkedList<>(), this);

        this.leaves.remove(parent);
        this.leaves.add(child);

        parent.getChildren().add(child);
        this.size++;

        return child;
    }

    @Override
    public E remove(Position<E> p) {
        TreeNode<E> node = this.checkPosition(p);
        if (!this.isRoot(node)) {
            node.getParent().getChildren().remove(node);
            if (node.getParent().getChildren().isEmpty()) {
                this.leaves.add(node.getParent());
            }
        }
        Iterator<Position<E>> ite = this.iterator();
        while (ite.hasNext()) {
            TreeNode<E> next = this.checkPosition(ite.next());
            next.setMyTree(null);
            this.leaves.remove(next);
            this.size--;
        }
        return node.getElement();
    }

    @Override
    public void moveSubtree(Position<E> pOrig, Position<E> pDest) throws RuntimeException {
        TreeNode<E> nodeOrigin = this.checkPosition(pOrig);
        TreeNode<E> nodeDest = this.checkPosition(pDest);

        if (this.isRoot(nodeOrigin)) {
            throw new RuntimeException("Root can't be moved");
        }
        else if (nodeOrigin.equals(nodeDest)) {
            throw new RuntimeException("The nodes are the same");
        }
        else if (this.isAncestor(nodeOrigin, nodeDest)) {
            throw new RuntimeException("Destination node can't be an origin node successor");
        }

        nodeDest.getChildren().add(nodeOrigin);
        nodeOrigin.getParent().getChildren().remove(nodeOrigin);

        if (nodeOrigin.getParent().getChildren().isEmpty()) {
            this.leaves.add(nodeOrigin.getParent());
        }

        nodeOrigin.setParent(nodeDest);

        this.leaves.remove(nodeDest);
    }

    private boolean isAncestor(TreeNode<E> nodeOrigin, TreeNode<E> nodeDest) {
        Iterator<Position<E>> ite = new BFSIterator<>(this, nodeOrigin);
        while (ite.hasNext()) {
            if (ite.next().equals(nodeDest)) {
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
        if (node.equals(this.root())) {
            throw new RuntimeException("The node has no parent");
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
        TreeNode<E> node = this.checkPosition(v);
        return !this.isLeaf(node);
    }

    @Override
    public boolean isLeaf(Position<E> v) throws RuntimeException {
        TreeNode<E> node = this.checkPosition(v);
        return node.getChildren().isEmpty();
    }

    @Override
    public boolean isRoot(Position<E> v) {
        TreeNode<E> node = this.checkPosition(v);
        return node.equals(this.root);
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        if (this.isEmpty()) {
            this.root = new TreeNode<>(e, null, new LinkedList<>(), this);
            this.leaves.add(this.root);
            this.size = 1;
            return this.root;
        }
        throw new RuntimeException("There is already a root");
    }

    @Override
    public int level() {
        int maxLevel = 0;
        if (!this.isEmpty()) {
            HashMap<Position<E>, Integer> levels = new HashMap<>();
            Queue<Position<E>> queue = new LinkedList<>();

            levels.put(this.root(), 0);
            queue.add(this.root());

            while (!queue.isEmpty()) {
                Position<E> current = queue.poll();
                int currentLevel = levels.get(current);
                maxLevel = Math.max(maxLevel, currentLevel);

                for (Position<E> child : this.children(current)) {
                    queue.add(child);
                    levels.put(child, currentLevel + 1);
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
