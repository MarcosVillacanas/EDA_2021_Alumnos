package material.test.practica3;

import material.Position;
import material.tree.binarytree.*;
import static org.junit.jupiter.api.Assertions.*;

public class BinaryTreeUtilsTest {

    private BinaryTree<Integer> tree;
    private BinaryTreeUtils<Integer> utils;
    private Position<Integer>[] pos;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        tree = new LinkedBinaryTree<>();
        pos = new Position[12];
        pos[0] = tree.addRoot(0);
        pos[1] = tree.insertLeft(pos[0], 1);
        pos[2] = tree.insertRight(pos[0], 2);
        pos[3] = tree.insertLeft(pos[1], 3);
        pos[4] = tree.insertRight(pos[1], 4);
        pos[5] = tree.insertLeft(pos[2], 5);
        pos[6] = tree.insertRight(pos[2], 6);

        utils = new BinaryTreeUtils<>(tree);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // Not required
    }

    @org.junit.jupiter.api.Test
    void mirror() {
        BinaryTree<Integer> mirrored = utils.mirror();

        Position<Integer> mirroredRoot = mirrored.root();
        Position<Integer> mLeftChild = mirrored.left(mirroredRoot);
        Position<Integer> mRightChild = mirrored.right(mirroredRoot);

        Position<Integer> treeRoot = tree.root();
        Position<Integer> tLeftChild = tree.left(treeRoot);
        Position<Integer> tRightChild = tree.right(treeRoot);

        assertEquals(mirroredRoot.getElement(), treeRoot.getElement());
        assertEquals(mLeftChild.getElement(), tRightChild.getElement());
        assertEquals(mRightChild.getElement(), tLeftChild.getElement());
        assertEquals(mirrored.left(mLeftChild).getElement(), tree.right(tRightChild).getElement());
        assertEquals(mirrored.left(mRightChild).getElement(), tree.right(tLeftChild).getElement());
        assertEquals(mirrored.right(mLeftChild).getElement(), tree.left(tRightChild).getElement());
        assertEquals(mirrored.right(mRightChild).getElement(), tree.left(tLeftChild).getElement());
    }

    @org.junit.jupiter.api.Test
    void contains() {
        assertFalse(utils.contains(10));
        assertTrue(utils.contains(0));
        assertTrue(utils.contains(6));
    }

    @org.junit.jupiter.api.Test
    void level() {
        assertEquals(1, utils.level(pos[0]));
        assertEquals(2, utils.level(pos[2]));
        assertEquals(3, utils.level(pos[4]));
    }
}
