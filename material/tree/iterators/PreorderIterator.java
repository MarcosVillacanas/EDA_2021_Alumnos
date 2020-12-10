package material.tree.iterators;

import material.Position;
import material.tree.Tree;

import java.util.*;
/**
 * Generic preorder iterator for trees.
 *
 * @param <E>
 * @author A. Duarte, J. Vélez, J. Sánchez-Oro, JD. Quintana
 */
public class PreorderIterator<E> implements Iterator<Position<E>> {

    Queue<Position<E>> queue;
    Tree<E> myTree;

    public PreorderIterator(Tree<E> tree) {
        this.queue = new LinkedList<>();
        this.myTree = tree;
        if (!this.myTree.isEmpty()) {
            this.queue.add(this.myTree.root());
        }
    }

    public PreorderIterator(Tree<E> tree, Position<E> pos) {
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

        Queue<Position<E>> aux = new LinkedList<>();
        for (Position<E> child : this.myTree.children(current)) {
            aux.add(child);
        }
        aux.addAll(this.queue);
        this.queue = aux;

        return current;
    }
}