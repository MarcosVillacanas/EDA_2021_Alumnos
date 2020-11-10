package material.tree.iterators;

import material.Position;
import material.tree.Tree;

import java.util.*;
import java.util.function.Predicate;

/**
 * Generic preorder iterator for trees.
 *
 * @param <E>
 * @author A. Duarte, J. Vélez, J. Sánchez-Oro, JD. Quintana
 */
public class PreorderIterator<E> implements Iterator<Position<E>> {

    Predicate<Position<E>> predicate;
    Stack<Position<E>> s;
    Tree<E> tree;
    Position<E> next;
    Set<Position<E>> visited;

    public PreorderIterator(Tree<E> tree) {
        this.predicate = null;
        this.s = new Stack<>();
        this.tree = tree;
        this.visited = new HashSet<>();
        if (!tree.isEmpty()) {
            this.s.push(tree.root());
            this.next = this.lookForward();
        }
    }

    public PreorderIterator(Tree<E> tree, Position<E> start) {
        this.predicate = null;
        s = new Stack<>();
        this.tree = tree;
        this.visited = new HashSet<>();
        s.push(start);
        this.next = this.lookForward();
    }

    public PreorderIterator(Tree<E> tree, Position<E> start, Predicate<Position<E>> predicate) {
        this.predicate = predicate;
        s = new Stack<>();
        this.tree = tree;
        this.visited = new HashSet<>();
        s.push(start);
        this.next = this.lookForward();
    }

    @Override
    public boolean hasNext() {
        return this.next != null;
    }

    @Override
    public Position<E> next() {
        Position<E> next = this.next;
        this.next = this.lookForward();
        return next;
    }

    private Position<E> lookForward() {
        if (this.s.isEmpty()) {
            return null;
        }
        Position<E> current = s.peek();
        if (!visited.contains(current)) {
            visited.add(current);
            return current;
        }
        for(Position<E> child : this.tree.children(current)) {
            if (!visited.contains(child)) {
                s.push(child);
                return this.lookForward();
            }
        }
        s.pop();
        return this.lookForward();
    }

}
