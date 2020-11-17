package material.tree.iterators;

import material.Position;
import material.tree.Tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Front iteartor for trees.
 *
 * @param <E>
 * @author jvelez, JD. Quintana
 */
public class FrontIterator<E> implements Iterator<Position<E>> {

    Queue<Position<E>> queue;
    Tree<E> myTree;

    public FrontIterator(Tree<E> tree){
        this.queue = new LinkedList<>();
        this.myTree = tree;
        if (!this.myTree.isEmpty()) {
            this.queue.add(this.myTree.root());
        }
    }

    public FrontIterator(Tree<E> tree, Position<E> pos) {
        this.queue = new LinkedList<>();
        this.myTree = tree;
        this.queue.add(pos);
    }

    @Override
    public boolean hasNext() {
        return !this.queue.isEmpty();
    }

    @Override
    public Position<E> next() {
        Position<E> current = this.queue.poll();
        for (Position<E> child : this.myTree.children(current)) {
            this.queue.add(child);
        }
        return (this.myTree.isLeaf(current))? current : next();
    }
}