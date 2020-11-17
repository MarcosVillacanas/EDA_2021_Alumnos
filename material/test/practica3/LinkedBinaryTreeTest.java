package material.test.practica3;

import material.Position;
import material.tree.binarytree.BinaryTree;
import material.tree.binarytree.LinkedBinaryTree;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinkedBinaryTreeTest {

    private LinkedBinaryTree<Integer> tree;
    private Position<Integer>[] pos;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        tree = new LinkedBinaryTree<>();
        pos = new Position[9];
        pos[0] = tree.addRoot(0);
        pos[1] = tree.insertLeft(pos[0], 1);
        pos[2] = tree.insertRight(pos[0], 2);
        pos[3] = tree.insertLeft(pos[1], 3);
        pos[4] = tree.insertLeft(pos[2], 4);
        pos[5] = tree.insertRight(pos[2], 5);
        pos[6] = tree.insertLeft(pos[4], 6);
        pos[7] = tree.insertRight(pos[4], 7);
        pos[8] = tree.insertRight(pos[7], 8);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // Not required
    }

    @org.junit.jupiter.api.Test
    void size() {
        assertEquals(9, tree.size());
        tree.remove(pos[2]);
        assertEquals(3, tree.size());
        tree.remove(pos[0]);
        assertEquals(0, tree.size());
    }

    @org.junit.jupiter.api.Test
    void isEmpty() {
        assertFalse(tree.isEmpty());
        tree.remove(pos[0]);
        assertTrue(tree.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void isInternal() {
        for (int i = 0; i < pos.length; i++) {
            if (i == 3 || i == 6 || i == 8 || i == 5) {
                assertFalse(tree.isInternal(pos[i]));
            } else {
                assertTrue(tree.isInternal(pos[i]));
            }
        }
    }

    @org.junit.jupiter.api.Test
    void isLeaf() {
        for (int i = 0; i < pos.length; i++) {
            if (i == 3 || i == 6 || i == 8 || i == 5) {
                assertTrue(tree.isLeaf(pos[i]));
            } else {
                assertFalse(tree.isLeaf(pos[i]));
            }
        }
    }

    @org.junit.jupiter.api.Test
    void isRoot() {
        assertTrue(tree.isRoot(pos[0]));
        for (int i = 1; i < pos.length; i++) {
            assertFalse(tree.isRoot(pos[i]));
        }
    }

    @org.junit.jupiter.api.Test
    void root() {
        assertEquals(pos[0], tree.root());
        for (int i = 1; i < pos.length; i++) {
            assertNotEquals(pos[i], tree.root());
        }
    }

    @org.junit.jupiter.api.Test
    void rootExcept() {
        tree.remove(pos[0]);
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> tree.root());
        assertEquals("The tree is empty", thrown.getMessage());
    }

    @org.junit.jupiter.api.Test
    void parent() {

        assertEquals(pos[0], tree.parent(pos[1]));
        assertEquals(pos[0], tree.parent(pos[2]));

        assertEquals(pos[2], tree.parent(pos[4]));
        assertEquals(pos[2], tree.parent(pos[5]));
    }

    @org.junit.jupiter.api.Test
    void parentExcept() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> tree.parent(pos[0]));
        assertEquals("The node has not parent", thrown.getMessage());
    }

    @org.junit.jupiter.api.Test
    void children() {
        int n = 0;
        for (Position<Integer> child : tree.children(pos[0])) {
            assertEquals(pos[0], tree.parent(child));
            assertTrue(child == pos[1] || child == pos[2]);
            n++;
        }
        assertEquals(2, n);
        n = 0;
        for (Position<Integer> child : tree.children(pos[7])) {
            assertEquals(pos[7], tree.parent(child));
            assertTrue(child == pos[8]);
            n++;
        }
        assertEquals(1, n);
        n = 0;
        for (Position<Integer> child : tree.children(pos[5])) {
            n++;
        }
        assertEquals(0, n);
    }

    @org.junit.jupiter.api.Test
    void replace() {
        tree.replace(pos[0], 500);
        assertEquals(500, (int) tree.root().getElement());
    }

    @org.junit.jupiter.api.Test
    void addRoot() {
        tree.remove(pos[0]);
        Position<Integer> p = tree.addRoot(200);
        assertEquals(p, tree.root());
    }

    @org.junit.jupiter.api.Test
    void addRootExcept() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> tree.addRoot(100));
        assertEquals("The tree already has a root", thrown.getMessage());
    }

    @org.junit.jupiter.api.Test
    void swapElements() {
        tree.swap(pos[1], pos[0]);
        assertEquals(1, (int) tree.root().getElement());
        assertEquals(1, (int) pos[0].getElement());
        assertEquals(0, (int) pos[1].getElement());
        assertNotEquals(pos[1], tree.root());
        assertEquals(pos[0], tree.root());
    }

    @org.junit.jupiter.api.Test
    void insert() {
        Position<Integer> p100 = tree.insertLeft(pos[7], 100);
        assertEquals(pos[7], tree.parent(p100));
        int n = 0;
        for (Position<Integer> child : tree.children(pos[0])) {
            n++;
        }
        assertEquals(2, n);
        Position<Integer> p200 = tree.insertRight(pos[5], 200);
        assertEquals(pos[5], tree.parent(p200));
        assertTrue(tree.isInternal(pos[5]));
    }

    @org.junit.jupiter.api.Test
    void remove() {
        tree.remove(pos[2]);
        assertEquals(3, tree.size());
        RuntimeException thrown;
        for (int i = 4; i <= 8; i++) {
            int finI = i;
            thrown = assertThrows(RuntimeException.class,
                    () -> tree.parent(pos[finI]));
            assertEquals("The node is not from this tree", thrown.getMessage());
        }
    }

    @org.junit.jupiter.api.Test
    void iterator() {
        int next = 0;
        for (Position<Integer> position : tree) {
            assertEquals(pos[next], position);
            next++;
        }
    }

    @org.junit.jupiter.api.Test
    void subtree() {

        BinaryTree<Integer> newTree = new LinkedBinaryTree<>();
        Position<Integer> [] newTrePos = new Position[6];
        newTrePos[0] = newTree.addRoot(2);
        newTrePos[1] = newTree.insertLeft(newTrePos[0], 4);
        newTrePos[2] = newTree.insertRight(newTrePos[0], 5);
        newTrePos[3] = newTree.insertLeft(newTrePos[1], 6);
        newTrePos[4] = newTree.insertRight(newTrePos[1], 7);
        newTrePos[5] = newTree.insertRight(newTrePos[4], 8);

        Iterator<Position<Integer>> iteNewTree = newTree.iterator();
        String result = "";
        while (iteNewTree.hasNext()) {
            result += iteNewTree.next().getElement();
        }

        BinaryTree<Integer> test = tree.subTree(pos[2]);

        Iterator<Position<Integer>> iteTest = test.iterator();
        String resultTest = "";
        while (iteTest.hasNext()) {
            resultTest += iteTest.next().getElement();
        }

        assertEquals(result, resultTest);
    }

}
