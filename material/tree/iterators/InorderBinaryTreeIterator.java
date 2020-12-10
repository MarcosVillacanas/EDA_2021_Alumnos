package material.tree.iterators;

import material.Position;
import material.tree.binarytree.BinaryTree;

import java.util.*;

public class InorderBinaryTreeIterator<E> implements Iterator<Position<E>> {

    BinaryTree<E> tree;
    Stack<Position<E>> stack;
    HashSet<Position<E>> visited;

    public InorderBinaryTreeIterator (BinaryTree<E> bT) {
        this.tree = bT;
        this.stack = new Stack<>();
        if (!this.tree.isEmpty()) {
            this.stack.push(this.tree.root());
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

            if (this.tree.hasRight(current)) {
                Position<E> right = this.tree.right(current);
                if (!visited.contains(right)) {
                    this.stack.push(right);
                    this.visited.add(right);
                }
            }

            this.stack.push(current);

            if (this.tree.hasLeft(current)) {
                Position<E> left = this.tree.left(current);
                if (!visited.contains(left)) {
                    this.stack.push(left);
                    this.visited.add(left);
                }
            }

        } while (current != this.stack.peek());

        return this.stack.pop();
    }
}
