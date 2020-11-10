package material.tree.iterators;

import material.Position;
import material.tree.Tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Front iteartor for trees.
 *
 * @param <T>
 * @author jvelez, JD. Quintana
 */
public class FrontIterator<T> implements Iterator<Position<T>> {

    Queue<Position<T>> q;
    Tree<T> tree;
    Position<T> next;

    public FrontIterator(Tree<T> tree) {
        this.tree = tree;
        this.q = new LinkedList<>();
        if (!tree.isEmpty()) {
            q.add(tree.root());
        }
        this.next = this.lookForward();
    }

    public FrontIterator(Tree<T> tree, Position<T> node) {
        this.tree = tree;
        this.q = new LinkedList<>();
        q.add(node);
        this.next = this.lookForward();
    }

    @Override
    public boolean hasNext() {
        return this.next != null;
    }


    @Override
    public Position<T> next() {
        Position<T> next = this.next;
        this.next = this.lookForward();
        return next;
    }

    private Position<T> lookForward() {

        if (q.isEmpty()) {
            return null;
        }
        Position<T> current = q.poll();
        if (this.tree.isLeaf(current)) {
            return current;
        }
        for(Position<T> child : this.tree.children(current)) {
            q.add(child);
        }
        return this.lookForward();
    }
}
