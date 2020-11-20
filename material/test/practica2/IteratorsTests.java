package material.test.practica2;

import material.Position;
import material.tree.binarytree.BinaryTree;
import material.tree.binarytree.LinkedBinaryTree;
import material.tree.iterators.*;
import material.tree.narytree.LinkedTree;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class IteratorsTests {

    private LinkedTree<Integer> tree;
    private Position<Integer>[] pos;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        tree = new LinkedTree<>();
        pos = new Position[12];
        pos[0] = tree.addRoot(0);
        pos[1] = tree.add(1, pos[0]);
        pos[2] = tree.add(2, pos[0]);
        pos[3] = tree.add(3, pos[0]);
        pos[4] = tree.add(4, pos[0]);
        pos[5] = tree.add(5, pos[1]);
        pos[6] = tree.add(6, pos[2]);
        pos[7] = tree.add(7, pos[2]);
        pos[8] = tree.add(8, pos[3]);
        pos[9] = tree.add(9, pos[7]);
        pos[10] = tree.add(10, pos[7]);
        pos[11] = tree.add(11, pos[7]);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // Not required
    }

    @org.junit.jupiter.api.Test
    void BFSIterator() {
        Iterator<Position<Integer>> ite = new BFSIterator<>(this.tree);
        String result = "";
        while (ite.hasNext()) {
            result += ite.next().getElement();
        }
        assertEquals(result, "01234567891011");
    }

    @org.junit.jupiter.api.Test
    void FrontIterator() {
        Iterator<Position<Integer>> ite = new FrontIterator<>(this.tree);
        String result = "";
        while (ite.hasNext()) {
            result += ite.next().getElement();
        }
        assertEquals(result, "456891011");
    }

    @org.junit.jupiter.api.Test
    void PreorderIterator() {
        Iterator<Position<Integer>> ite = new PreorderIterator<>(this.tree);
        String result = "";
        while (ite.hasNext()) {
            result += ite.next().getElement();
        }
        assertEquals(result, "01526791011384");
    }

    @org.junit.jupiter.api.Test
    void PostorderIterator() {
        Iterator<Position<Integer>> ite = new PostorderIterator<>(this.tree);
        String result = "";
        while (ite.hasNext()) {
            result += ite.next().getElement();
        }
        assertEquals(result, "51691011728340");
    }

    @org.junit.jupiter.api.Test
    void InorderBinaryTreeIterator() {
        BinaryTree<Integer> bTree = new LinkedBinaryTree<>();
        Position<Integer> [] bPos = new Position[12];
        bPos[0] = bTree.addRoot(0);
        bPos[1] = bTree.insertLeft(bPos[0], 1);
        bPos[2] = bTree.insertRight(bPos[0], 2);
        bPos[3] = bTree.insertLeft(bPos[2], 3);
        bPos[4] = bTree.insertRight(bPos[2], 4);
        bPos[5] = bTree.insertRight(bPos[3], 5);
        bPos[6] = bTree.insertLeft(bPos[5], 6);
        bPos[7] = bTree.insertRight(bPos[5], 7);


        Iterator<Position<Integer>> ite = new InorderBinaryTreeIterator<>(bTree);
        String result = "";
        while (ite.hasNext()) {
            result += ite.next().getElement();
        }
        assertEquals(result, "10365724");
    }

    @org.junit.jupiter.api.Test
    void InorderReverseBinaryTreeIterator() {
        BinaryTree<Integer> bTree = new LinkedBinaryTree<>();
        Position<Integer> [] bPos = new Position[12];
        bPos[0] = bTree.addRoot(0);
        bPos[1] = bTree.insertLeft(bPos[0], 1);
        bPos[2] = bTree.insertRight(bPos[0], 2);
        bPos[3] = bTree.insertLeft(bPos[2], 3);
        bPos[4] = bTree.insertRight(bPos[2], 4);
        bPos[5] = bTree.insertRight(bPos[3], 5);
        bPos[6] = bTree.insertLeft(bPos[5], 6);
        bPos[7] = bTree.insertRight(bPos[5], 7);


        Iterator<Position<Integer>> ite = new InorderReverseBinaryTreeIterator<>(bTree);
        String result = "";
        while (ite.hasNext()) {
            result += ite.next().getElement();
        }
        assertEquals(result, "42756301");
    }
}
