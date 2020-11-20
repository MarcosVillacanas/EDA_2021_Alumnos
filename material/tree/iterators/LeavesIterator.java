package material.tree.iterators;

import material.Position;
import material.tree.narytree.LinkedTree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class LeavesIterator<E> implements Iterator<Position<E>> {

    LinkedTree<E> tree;
    Queue<Position<E>> queue;

    public LeavesIterator(LinkedTree<E> t) {
        this.tree = t;
        this.queue = new LinkedList<>();
        if (!this.tree.isEmpty()) {
            this.queue.add(this.tree.root());
        }
    }

    @Override
    public boolean hasNext() {
        return !this.queue.isEmpty();
    }

    @Override
    public Position<E> next() {

        Position<E> next;
        do {
            next = this.queue.poll();
            for (Position<E> child : this.tree.children(next)) {
                this.queue.add(child);
            }
        } while (!this.tree.isLeaf(next));

        return next;
    }
}
