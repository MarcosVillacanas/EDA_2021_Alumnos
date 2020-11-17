/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package material.tree.iterators;

import material.Position;
import material.tree.Tree;
import material.tree.binarytree.LinkedBinaryTree;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Generic iterator for trees
 *
 * @param <E> the type of elements stored in the tree
 * @author A. Duarte, J. Vélez, J. Sánchez-Oro
 */

public class BFSIterator<E> implements Iterator<Position<E>> {

    Queue<Position<E>> queue;
    Tree<E> myTree;

    public BFSIterator(Tree<E> myTree) {
        this.queue = new LinkedList<>();
        this.myTree = myTree;
        this.queue.add(this.myTree.root());
    }

    public BFSIterator(Tree<E> myTree, Position<E> pos) {
        this.queue = new LinkedList<>();
        this.myTree = myTree;
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
        return current;
    }
}
