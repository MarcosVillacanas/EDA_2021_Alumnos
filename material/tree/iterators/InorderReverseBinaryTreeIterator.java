package material.tree.iterators;

import javafx.geometry.Pos;
import material.Position;
import material.tree.binarytree.BinaryTree;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class InorderReverseBinaryTreeIterator<E> implements Iterator<Position<E>> {

    BinaryTree<E> tree;
    Stack<Position<E>> stack;
    HashSet<Position<E>> visited;

    public InorderReverseBinaryTreeIterator (BinaryTree<E> t) {
        this.tree = t;
        this.stack = new Stack<>();
        if (this.tree.isEmpty()) {
            this.stack.add(this.tree.root());
        }
        this.visited = new HashSet<>();
    }

    @Override
    public boolean hasNext() {
        return !this.stack.isEmpty();
    }

    @Override
    public Position<E> next() {

        Position<E> current;

        do {
            current = this.stack.pop();

            if (this.tree.hasLeft(current)) {
                Position<E> left = this.tree.left(current);
                if (!this.visited.contains(left)) {
                    this.stack.push(left);
                    visited.add(left);
                }
            }

            this.stack.push(current);

            if (this.tree.hasRight(current)) {
                Position<E> right = this.tree.right(current);
                if (!this.visited.contains(right)) {
                    this.stack.push(right);
                    visited.add(right);
                }
            }
        } while (this.stack.peek() != current);

        return this.stack.pop();
    }
}
