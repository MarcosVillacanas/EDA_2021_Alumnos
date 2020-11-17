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
public class PostorderIterator<E> implements Iterator<Position<E>> {

    Stack<Position<E>> stack;
    Tree<E> myTree;
    HashSet<Position<E>> childrenVisited;

    public PostorderIterator(Tree<E> tree) {
        this.stack = new Stack<>();
        this.myTree = tree;
        this.childrenVisited = new HashSet<>();
        this.stack.push(this.myTree.root());
    }

    public PostorderIterator(Tree<E> tree, Position<E> pos) {
        this.stack = new Stack<>();
        this.myTree = tree;
        this.childrenVisited = new HashSet<>();
        this.stack.push(pos);
    }

    @Override
    public boolean hasNext() {
        return !this.stack.isEmpty();
    }

    @Override
    public Position<E> next() {
        Position<E> current = this.stack.pop();
        if (this.myTree.isLeaf(current) || childrenVisited.contains(current)) {
            return current;
        }
        else {
            this.stack.push(current);
            Stack<Position<E>> auxStack = new Stack<>();
            for (Position<E> child : this.myTree.children(current)) {
                auxStack.push(child);
            }
            while (!auxStack.isEmpty()) {
                this.stack.push(auxStack.pop());
            }
            childrenVisited.add(current);
            return next();
        }
    }
}
