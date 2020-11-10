package material.tree.iterators;

import material.Position;
import material.tree.Tree;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;

/**
 * Generic preorder iterator for trees.
 *
 * @param <E>
 * @author A. Duarte, J. Vélez, J. Sánchez-Oro, JD. Quintana
 */
public class PostorderIterator<E> implements Iterator<Position<E>> {

    Stack<Position<E>> s;
    Tree<E> tree;
    Position<E> next;
    Set<Position<E>> visited;

    public PostorderIterator(Tree<E> tree) {
        this.s = new Stack<>();
        this.tree = tree;
        this.visited = new HashSet<>();
        if (!tree.isEmpty()) {
            this.s.push(tree.root());
            this.next = this.lookForward();
        }
    }

    public PostorderIterator(Tree<E> tree, Position<E> start) {
        s = new Stack<>();
        this.tree = tree;
        this.visited = new HashSet<>();
        s.push(start);
        this.next = this.lookForward();
    }

    public PostorderIterator(Tree<E> tree, Position<E> start, Predicate<Position<E>> predicate) {
        throw new RuntimeException("Not yet implemented");
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
        for(Position<E> child : this.tree.children(current)) {
            if (!visited.contains(child)) {
                s.push(child);
                return this.lookForward();
            }
        }
        visited.add(current);
        s.pop();
        return current;
    }
}
